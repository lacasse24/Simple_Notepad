package com.lac.alex.simplenotepad;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Alex on 2018-02-07.
 */

public class LoginWorker extends AsyncTask<String,String,String>
{
    private Context context;
    private Boolean isSuccess = false;
    private final static String SERVER_IP = "http://192.168.0.181";
    private ProgressBar Pbar;

    public LoginWorker(Context context, ProgressBar bar)
    {
        this.Pbar = bar;
        this.context = context;
    }

    @Override
    protected void onPreExecute()
    {
        Pbar.setVisibility(View.VISIBLE);
    }


    @Override
    protected String doInBackground(String... params)
    {

        String Nom = params[0];
        String Mdp = params[1];
        String Lien;
        String Donnee;
        BufferedReader bufferedReader;
        String result;

            try
            {
                if(Nom.trim().equals("")|| Mdp.trim().equals(""))
                {
                    result = "Veuillez remplir les deux champs ! ";
                    Toast.makeText(context, "Veuillez remplir tous les champs ! ", Toast.LENGTH_SHORT).show();
                }

                Donnee = "?Nom=" + URLEncoder.encode(Nom, "UTF-8");
                Donnee += "&Mdp=" + URLEncoder.encode(Mdp, "UTF-8");


                Lien = SERVER_IP + "/Login.php" + Donnee;
                URL url = new URL(Lien);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                result = bufferedReader.readLine();

                return result;
            }
            catch (Exception e)
            {
                return new String("Exception: " + e.getMessage());
            }
    }

    @Override
    protected void onPostExecute(String result)
    {
        Pbar.setVisibility(View.INVISIBLE);
        String jsonStr = result;
        if (jsonStr != null)
        {
            try
            {
                JSONObject jsonObj = new JSONObject(jsonStr);
                String query_result = jsonObj.getString("Reponse_BD");
                if (query_result.equals("SUCCESS"))
                {
                    Toast.makeText(context, "Bienvenue ! ", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, NoteListActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
                else if (query_result.equals("FAILURE"))
                {
                    Toast.makeText(context, "Informations invalides ! ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(context, "Connexion impossible à la base de données.", Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Toast.makeText(context, "Veuillez remplir tous les champs ! ", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(context, "Aucune donnée JSON.", Toast.LENGTH_SHORT).show();
        }
    }

}

