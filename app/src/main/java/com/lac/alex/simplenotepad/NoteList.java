package com.lac.alex.simplenotepad;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Alex on 2017-06-19.
 */

public class NoteList
{
    private static final String TAG = "NoteList";
    private static final String FILENAME = "notes.json";

    private ArrayList<Note> m_NoteList;
    private JSONSerializer m_Serializer;

    private Context m_AppContext;  // The Context class is an “Interface to global information about an application environment".

    private static NoteList sNoteList;  // My array of "Note" objects (also my Singleton)

    // Create an instance of the class. If there's already one then it will return it (typical singleton).
    public static NoteList get (Context context)
    {
        if (sNoteList == null) // If there's not yet an instance, it will call the constructor to create one.
        {
            sNoteList = new NoteList(context.getApplicationContext());
        }

        return sNoteList;
    }

    private NoteList(Context AppContext)
    {
        m_AppContext = AppContext;
        m_NoteList = new ArrayList<Note>();
        m_Serializer = new JSONSerializer(m_AppContext,FILENAME);

        //If there are no notes to load, we create an empty list, if not we load them
        try
        {
            m_NoteList = m_Serializer.loadNotes();
        }
        catch (Exception e)
        {
            m_NoteList = new ArrayList<Note>();
            Log.e(TAG, "Error loading notes: ", e);
        }
    }



    public ArrayList<Note> getNotes() //Return all of the Notes
    {
        return m_NoteList;
    }

    public void deleteNote(Note note)
    {
        m_NoteList.remove(note);
    }

    public void addNote(Note note)
    {
        m_NoteList.add(note);
    }

    public Note getNote(UUID id)  // Return a specific crime
    {
        for(Note n : m_NoteList)
        {
            if(n.getId().equals(id))
                return n;
        }
        return null;

    }

    public boolean saveNotes(boolean MessageOn)
    {
        try
        {
            m_Serializer.saveNotes(m_NoteList);
            if(MessageOn == true)
            {
                Toast print = Toast.makeText(m_AppContext, "Votre note a été sauvegardée.", Toast.LENGTH_SHORT);
                print.show();
            }
            return true;
        }
        catch (Exception e)
        {
            Toast print = Toast.makeText(m_AppContext,"Impossible de sauvegarder la note, veuillez réessayer.",Toast.LENGTH_SHORT);
            print.show();
            return false;
        }
    }
}

