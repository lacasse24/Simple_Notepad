package com.lac.alex.simplenotepad;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Alex on 2017-06-19.
 */

public class NoteListFragment extends ListFragment
{
    private ArrayList<Note> m_NotesList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Obligatory if you want to inflate your Option menu ...
        // To change the toolbar title
        getActivity().setTitle("  " + "Simple Notepad");
        setRetainInstance(true);
        //Get the singleton and then get the list of notes
        m_NotesList = NoteList.get(getActivity()).getNotes();

        NoteAdapter Adapter = new NoteAdapter(m_NotesList);
        setListAdapter(Adapter);

        ImageButton fab = (ImageButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                newNote();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = super.onCreateView(inflater,container,savedInstanceState);


        // Use contextual action bar on Honeycomb and higher
        ListView listView = (ListView)v.findViewById(android.R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);


        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener()
        {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {}

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu)
            {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.note_list_menu_context, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item)
            {
                if(item.getItemId() == R.id.menu_context_delete)
                {
                    NoteAdapter adapter = (NoteAdapter) getListAdapter();
                    NoteList noteList = NoteList.get(getActivity());

                    for (int i = adapter.getCount() - 1; i >= 0; i--)
                    {
                        if (getListView().isItemChecked(i)) {
                            noteList.deleteNote(adapter.getItem(i));
                        }
                    }

                    mode.finish();  //dispose of the action menu
                    adapter.notifyDataSetChanged();  //actualise the UI
                    return true;
                }

                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu,inflater);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        ((NoteAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_item_new_note) //HERE !! Tu dois sauvegarder l'information(tes instances) et la loader
        {
            newNote();
        }
            else
                if(id == R.id.ic_menu_info)
                 {
                     AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                     builder.setTitle("Développé par : ");
                     builder.setMessage(" Alex Lacasse" + "\n\r" + "\n\r");
                     builder.setPositiveButton("Fermer", new DialogInterface.OnClickListener()
                      {
                            public void onClick(DialogInterface dialog, int id)
                          {

                         }
                     });
                     builder.show();
                 }

        return true;
    }

    @Override
    public void onListItemClick(ListView parent, View v, int position, long id)
    {
        Note n = (Note)((NoteAdapter)getListAdapter()).getItem(position);
        Intent i = new Intent(getActivity(),SingleNoteActivity.class);
        i.putExtra(SingleNoteFragment.EXTRA_NOTE_ID,n.getId()); // Pass the UUID of the note, then it will be possible for the Activity's fragment to retrieve the right note from the Array
        startActivity(i);
    }

    public void newNote()
    {
        Intent i;

        Note note = new Note();
        NoteList.get(getActivity()).addNote(note);
        i = new Intent(getActivity(), SingleNoteActivity.class); //getActivity() pass its hosting activity as the Context object that the intent constructor requires
        i.putExtra(SingleNoteFragment.EXTRA_NOTE_ID, note.getId());
        startActivityForResult(i,100);  // 100 is the request code
    }

/***********************INNER CLASS*****************************************************************/
//When the listView needs a view to display, it will have a conversation with its adapter (this class)
    private class NoteAdapter extends ArrayAdapter<Note>
    {
        public NoteAdapter (ArrayList<Note> notes)
        {
            super(getActivity(),0,notes); // 0 for the layout ID because we use our own layout not a pre-defined one by the Android SDK.
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            //If we weren't given a view, inflate one.
            // We irst check if a recycled view was passed in or not.
            if(convertView == null)
            {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_item,null);
            }

            //Configure the view for a note
            Note n = getItem(position); // getItem(int) gets the note for the current position in the list.

            TextView titleTextView = (TextView) convertView.findViewById(R.id.note_list_item_titleTextView);
            titleTextView.setText(n.getTitle());

            TextView dateTextView = (TextView) convertView.findViewById(R.id.note_list_item_dateTextView);
            dateTextView.setText(n.getDate().toString());

            return convertView; // return the view object to the listview
        }
    }
    /***************************************************************************************************************************/
}
