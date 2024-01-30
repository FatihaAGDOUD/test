package com.example.renthome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        int delayMillis = 4500;

        // Utilisation de Handler pour créer un délai
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Créer une intention pour passer à NextActivity
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);

                // Fermer l'activité actuelle pour éviter de revenir en arrière
                finish();
            }
        }, delayMillis);

        // Récupérer le TextView
        TextView textViewEasyRent = findViewById(R.id.textViewEasyRent);

        // Appliquer la SpannableString au TextView
        textViewEasyRent.setText(getSpannableString("EasyRent"));
    }

    private SpannableString getSpannableString(String text) {
        SpannableString spannableString = new SpannableString(text);

        // Appliquer la couleur blanche à la partie "Easy"
        spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Appliquer la couleur holo_blue_bright à la partie "Rent"
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#27D6DF")), 4, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }
}