package com.lac.alex.simplenotepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


public class LoginActivity extends AppCompatActivity
{
    private EditText Identifiant ;
    private EditText Mdp;
    private ProgressBar Pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button Connexion = (Button)findViewById(R.id.btn_login);
        TextView Inscription = (TextView)findViewById(R.id.txt_signup);
        Identifiant = (EditText)findViewById(R.id.txtIdentifiant);
        Mdp = (EditText)findViewById(R.id.txtMdp);
        Pbar = (ProgressBar)findViewById(R.id.progressBar_cyclic);

        //Pour afficher l'icône de Simple Notepad
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        this.setTitle("  " + "Simple Notepad");


        Inscription.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getBaseContext(), SignupActivity.class);
                startActivity(i);
            }
        });

        Connexion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               if(Verification())
               {
                    LoginWorker verification = new LoginWorker(getApplicationContext(),Pbar);
                    verification.execute(Identifiant.getText().toString(), Mdp.getText().toString());
                }
            }
        });


    }

    public boolean Verification()
    {
        boolean verif = true;

        if (Identifiant.getText().toString().equals("")) {
            Identifiant.setError("Identifiant invalide ! ");
            verif = false;
        }
        if (Mdp.getText().toString().equals("")) {
            Mdp.setError("Mot de passe invalide ! ");
            verif = false;
        }


        return verif;
    }


}
