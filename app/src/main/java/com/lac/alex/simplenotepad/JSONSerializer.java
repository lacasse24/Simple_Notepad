package com.lac.alex.simplenotepad;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Alex on 2017-06-19.
 */

public class JSONSerializer extends Object
{
    private Context m_Context;
    private String m_Filename;

    public JSONSerializer(Context context, String file)
    {
        m_Context = context;
        m_Filename = file;
    }


    public void saveNotes(ArrayList<Note> Notes) throws JSONException, IOException
    {
        // Build an array in JSON
        JSONArray array = new JSONArray();

        for (Note n : Notes)
        {
            array.put(n.toJSON());
        }


        // Write the JSON array to disk
        Writer writer = null;
        try
        {
            OutputStream out = m_Context.openFileOutput(m_Filename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        }
        finally
        {
            if (writer != null)
                writer.close();
        }
    }

    public ArrayList<Note> loadNotes() throws IOException, JSONException
    {
        ArrayList<Note> notes = new ArrayList<Note>();
        BufferedReader reader = null;

        try {
            //Open and read the file into a StringBuilder
            InputStream in = m_Context.openFileInput(m_Filename);
            reader = new BufferedReader(new InputStreamReader(in));

            StringBuilder jsonString = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) // Put every single lines of the JSON file into a string builder
            {
                jsonString.append(line);
            }

            //Parse the JSON using JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue(); // Returns the next value from the input.

            //Build the array of notes from JSONObjects
            for (int i = 0; i < array.length(); i++)
                notes.add(new Note((array.getJSONObject(i))));
        }
        catch(FileNotFoundException e)
        {
        }
        finally {
            if(reader != null)
                reader.close();
        }

        return notes;
    }

}
