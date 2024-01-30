package com.example.renthome;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentLoginTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentLoginTab extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentLoginTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentLoginTab.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentLoginTab newInstance(String param1, String param2) {
        FragmentLoginTab fragment = new FragmentLoginTab();
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
        View rootView = inflater.inflate(R.layout.fragment_login_tab, container, false);

        TextView textViewEasyRent = rootView.findViewById(R.id.textViewEasyRent);
        textViewEasyRent.setText(getSpannableString("EasyRent"));

        Button loginButton = rootView.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer les valeurs saisies
                EditText emailEditText = rootView.findViewById(R.id.login_email);
                EditText passwordEditText = rootView.findViewById(R.id.login_password);

                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Vérifier les données dans la base de données Firebase
                if (checkCredentials(email, password)) {
                    // Les informations sont correctes, vous pouvez effectuer l'action appropriée ici
                    // Par exemple, ouvrir une nouvelle activité
                } else {
                    // Les informations ne sont pas correctes, afficher un message d'erreur
                    if (email.isEmpty() || password.isEmpty()) {
                        // Afficher un message d'erreur
                        Toast.makeText(getContext(), "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
                    }else {
                            Toast.makeText(getContext(), "Identifiants incorrects", Toast.LENGTH_SHORT).show();

                        }
                        return;

                                  }
            }
        });

        return rootView;
    }

    private boolean checkCredentials(String email, String password) {
        // Initialiser la référence à la base de données Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("utilisateurs");

        // Lire les données de la base de données
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String userEmail = userSnapshot.child("email").getValue(String.class);
                    String userPassword = userSnapshot.child("password").getValue(String.class);

                    if (userEmail.equals(email) && userPassword.equals(password)) {
                        String userRole = userSnapshot.child("role").getValue(String.class);

                        // Vous pouvez utiliser le rôle pour rediriger l'utilisateur vers une activité spécifique
                        if ("Locataire".equals(userRole)) {
                            // Redirection vers l'activité du locataire
                            startActivity(new Intent(getContext(), LocataireActivity.class));
                        } else if ("Propriétaire".equals(userRole)) {
                            // Redirection vers l'activité du propriétaire
                            startActivity(new Intent(getContext(), ProprietaireActivity.class));
                        } else if ("Admin".equals(userRole)) {
                            // Redirection vers l'activité de l'administrateur
                            startActivity(new Intent(getContext(), AdminActivity.class));
                        }

                        // Terminer l'activité actuelle (facultatif)
                        getActivity().finish();

                        return ;
                    }

                }

                // Aucune correspondance trouvée

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gérer l'erreur de lecture de la base de données
            }
        });

        // Par défaut, retourner false
        return false;
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