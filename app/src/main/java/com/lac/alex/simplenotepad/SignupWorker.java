package com.lac.alex.simplenotepad;

import android.content.Context;
import android.os.AsyncTask;
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

public class SignupWorker extends AsyncTask<String, Void, String>
{

    private Context context;
    private final static String SERVER_IP = "http://192.168.0.181";

    public SignupWorker(Context context)
    {
        this.context = context;
    }

    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... arg0)
    {
        String Nom = arg0[0];
        String Mdp = arg0[1];
        String Email = arg0[2];
        String Lien;
        String Donnee;
        BufferedReader bufferedReader;
        String result;

        try
        {
            if(Nom.equals("")|| Mdp.equals("") || Email.equals(""))
            {
                Toast.makeText(context, "Veuillez remplir tous les champs ! ", Toast.LENGTH_SHORT).show();
            }

            Donnee = "?Nom=" + URLEncoder.encode(Nom, "UTF-8");
            Donnee += "&Mdp=" + URLEncoder.encode(Mdp, "UTF-8");
            Donnee += "&Email=" + URLEncoder.encode(Email, "UTF-8");


            Lien = SERVER_IP + "/Signup.php" + Donnee;
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
        String jsonStr = result;
        if (jsonStr != null)
        {
            try
            {
                JSONObject jsonObj = new JSONObject(jsonStr);
                String query_result = jsonObj.getString("Reponse_BD");
                if (query_result.equals("SUCCESS"))
                {
                    Toast.makeText(context, "Inscription effectuée ! ", Toast.LENGTH_SHORT).show();
                }
                else if (query_result.equals("FAILURE"))
                {
                    Toast.makeText(context, "Inscription echouée ! ", Toast.LENGTH_SHORT).show();
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