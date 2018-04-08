package com.lac.alex.simplenotepad;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Alex on 2017-06-24.
 */

public class SingleNoteFragment extends Fragment
{
    public static final String EXTRA_NOTE_ID = "com.lac.alex.simplenotepad.note_id";
    public static final int REQUEST_IMAGE_CAPTURE = 69;

    private String m_CurrentPhotoPath;
    private Note m_Note;
    private EditText m_Title;
    private EditText m_Content;
    private ImageView m_ImageTaken;
    private Button m_TakePhoto;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        getActivity().setTitle("  " + "Détails");



        Intent intent = getActivity().getIntent();
        UUID NoteID = (UUID)intent.getSerializableExtra(EXTRA_NOTE_ID); //Gets the ID of the note with the key
        m_Note = NoteList.get(getActivity()).getNote(NoteID);




        ImageButton fab = (ImageButton)getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_single_note, container, false);

        m_Title = (EditText) v.findViewById(R.id.NoteTitle);
        m_Content = (EditText) v.findViewById(R.id.NoteContent);
        m_ImageTaken = (ImageView) v.findViewById(R.id.ImgCamera);
        m_ImageTaken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FullScreenImageActivity.class);
                intent.setData(Uri.parse(m_Note.getImgPath()));
                startActivity(intent);
            }
        });
        registerForContextMenu(m_ImageTaken);

        m_ImageTaken.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {

                return false;
            }
        });

        if(m_Note.getImgPath() != null)
        {
            File imgFile = new  File(m_Note.getImgPath());

            if(imgFile.exists())
            {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                m_ImageTaken.setImageBitmap(myBitmap);

            }
        }

        m_Title.setText(m_Note.getTitle());     // No need to verify if it's an empty string or not.
        m_Content.setText(m_Note.getContent());

        if(m_Title.getText().toString().equals(""))
        {
            m_Title.requestFocus();
        }
        else
        {
            m_Content.requestFocus();
        }

        m_TakePhoto = (Button)v.findViewById(R.id.btn_takepicture);
        m_TakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeImageFromCamera();
            }
        });



        return v;
    }

    //A context menu is a floating menu that appears when the user performs a long-click on an element. It provides actions that affect the selected content or context frame.
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.note_list_menu_context, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.menu_context_delete)
        {
            m_ImageTaken.setImageDrawable(null);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_single_note,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.menu_item_save_note)
        {
            SaveNote(true);
        }
        else
            if(id == R.id.menu_item_share_note)
            {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,getNoteSummary());
                i.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.note_summary_subject));
                i = Intent.createChooser(i, getString(R.string.send_summary));  // WAY BETTER WITH A CHOOSER
                startActivity(i);
            }

        return super.onOptionsItemSelected(item);
    }

    private void SaveNote(boolean MessageOn)
    {
        //Note invalide
        if(m_Title.getText().toString().equals("") && m_Content.getText().toString().equals(""))
        {
            NoteList.get(getActivity()).deleteNote(m_Note);
            return;
        }


            m_Note.setTitle(m_Title.getText().toString());
            m_Note.setContent(m_Content.getText().toString());

            m_Note.setDate(new Date());
            if (m_CurrentPhotoPath != null) {
                m_Note.setImgPath(m_CurrentPhotoPath);
            }
            NoteList.get(getActivity()).saveNotes(MessageOn);

    }

    private String getNoteSummary()
    {
        String Summary = "";

        SimpleDateFormat sdf = new SimpleDateFormat( "dd MMMM yyyy ");

        Summary +=  "Résumé de votre note du : "  +  sdf.format(new Date()) + "\n\r" +"\n\r"
                +   " Titre : " + m_Title.getText().toString() +"\n\r"
                +   " Contenu : " + m_Content.getText().toString();

        return Summary;
    }

    /***********************************FILE AND CAMERA PART ***********************************************/
    public void takeImageFromCamera()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null)
        {
            // Create the File where the photo should go
            File photoFile = null;
            try
            {
                photoFile = createImageFile();
            }
            catch (IOException ex)
            {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null)
            {
                Uri photoURI = FileProvider.getUriForFile(getContext(), "com.lac.alex.simplenotepad", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        m_CurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // Met la photo dans l'ImageView
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            File imgFile = new  File(m_CurrentPhotoPath);

            if(imgFile.exists())
            {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                m_ImageTaken.setImageBitmap(myBitmap);

            }

        }
    }
    /***********************************FILE AND CAMERA PART ***********************************************/

    @Override
    public void onPause()
    {
        super.onPause();
        SaveNote(false);
    }

}
