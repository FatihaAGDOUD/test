package com.example.renthome;


import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupTabFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignupTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignupTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupTabFragment newInstance(String param1, String param2) {
        SignupTabFragment fragment = new SignupTabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_signup_tab, container, false);

        // Récupérer le TextView
        TextView textViewEasyRent = rootView.findViewById(R.id.textViewEasyRent);

        // Appliquer la SpannableString au TextView
        textViewEasyRent.setText(getSpannableString("EasyRent"));

        // Récupérer le Spinner depuis le layout du fragment
        Spinner spinnerRole = rootView.findViewById(R.id.spinnerrole);

        // Définir les options du Spinner
        String[] roles = {"Choisir votre rôle","Locataire", "Propriétaire"};

        // Créer un adaptateur ArrayAdapter avec les options et un layout simple
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_list, roles);

        // Spécifier le layout pour le menu déroulant
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Appliquer l'adaptateur au Spinner
        spinnerRole.setAdapter(adapter);

        // Définir un écouteur pour le Spinner
        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Mettre à jour le titre du Spinner avec la valeur sélectionnée
                String selectedRole = roles[position];
                requireActivity().setTitle(selectedRole);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Ne rien faire ici
            }
        });

        // Récupérer le bouton d'inscription
        Button signupButton = rootView.findViewById(R.id.signup_button);

        // Ajouter un écouteur de clic au bouton
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Récupérer les valeurs des champs
                String email = ((EditText) rootView.findViewById(R.id.signup_email)).getText().toString();
                String password = ((EditText) rootView.findViewById(R.id.signup_password)).getText().toString();
                String confirmPassword = ((EditText) rootView.findViewById(R.id.signup_confirm)).getText().toString();
                String role = ((Spinner) rootView.findViewById(R.id.spinnerrole)).getSelectedItem().toString();

                // Vérifier si les champs sont remplis
                // Vérifier si les champs sont remplis


                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || role.equals("Choisir votre rôle")) {
                    // Afficher un message d'erreur
                    showToast("Veuillez remplir tous les champs.");
                    return;
                }


                if (!verifierEmail(email)) {
                    showToast("Veuillez saisir une adresse e-mail valide.");
                    return;
                }

                // Vérifier si les mots de passe correspondent
                if (!password.equals(confirmPassword)) {
                    showToast("Les mots de passe ne correspondent pas.");
                    return;
                }



                // Enregistrez les données dans Firebase
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("utilisateurs");
                DatabaseReference userRef = myRef.child(email.replace(".", "_"));
                userRef.child("email").setValue(email);
                userRef.child("password").setValue(password);
                userRef.child("role").setValue(role);

                showToast("Inscription réussie !");

                // Effacer le contenu des champs
                ((EditText) rootView.findViewById(R.id.signup_email)).setText("");
                ((EditText) rootView.findViewById(R.id.signup_password)).setText("");
                ((EditText) rootView.findViewById(R.id.signup_confirm)).setText("");
                ((Spinner) rootView.findViewById(R.id.spinnerrole)).setSelection(0); // Remettre la sélection par défaut


            }

            private void showToast(String s) {
                Toast.makeText(requireContext(),s, Toast.LENGTH_SHORT).show();

            }
            private  boolean verifierEmail(String email) {
                // Expression régulière pour vérifier une adresse e-mail
                String patternString = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

                // Créer un objet Pattern et un objet Matcher
                Pattern pattern = Pattern.compile(patternString);
                Matcher matcher = pattern.matcher(email);

                // Vérifier si l'adresse e-mail correspond au motif
                return matcher.matches();
            }
        });

        return rootView ;



    }

    private SpannableString getSpannableString(String text) {
        SpannableString spannableString = new SpannableString(text);

        // Appliquer la couleur blanche à la partie "Easy"
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#303030")), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Appliquer la couleur holo_blue_bright à la partie "Rent"
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#27D6DF")), 4, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }
   

}