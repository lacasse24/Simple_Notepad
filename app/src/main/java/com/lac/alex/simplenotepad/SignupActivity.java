package com.lac.alex.simplenotepad;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity
{
    private EditText Nom;
    private EditText Mdp;
    private EditText Email;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Nom = (EditText) findViewById(R.id.txtNom);
        Mdp = (EditText) findViewById(R.id.txtMdp);
        Email = (EditText) findViewById(R.id.txtEmail);
        TextView DejaMembre = (TextView) findViewById(R.id.txtMembre);
        Button Soumettre = (Button) findViewById(R.id.btnSignup);

        //Pour afficher l'icône de Simple Notepad
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        this.setTitle("  " + "Simple Notepad");

        Soumettre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Verification()) {
                    Toast.makeText(getApplicationContext(), "Inscription en cours...", Toast.LENGTH_SHORT).show();
                    new SignupWorker(getApplicationContext()).execute(Nom.getText().toString(), Mdp.getText().toString(), Email.getText().toString());
                }
            }
        });

        DejaMembre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean Verification()
    {
        boolean verif = true;

        if (Nom.getText().toString().equals("")) {
            Nom.setError("Un identifiant est nécessaire pour l'inscription ! ");
            verif = false;
        }
        if (Mdp.getText().toString().equals("")) {
            Mdp.setError("Es-tu saoul ? Entre un mot de passe !");
            verif = false;
        }
        if (Email.getText().toString().equals("") || isEmailValid(Email.getText().toString()) == false) {
            Email.setError("Adresse de messagerie invalide !");
            verif = false;
        }

        return verif;
    }

}
