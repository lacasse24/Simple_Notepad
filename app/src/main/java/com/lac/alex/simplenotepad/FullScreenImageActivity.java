package com.lac.alex.simplenotepad;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.File;

public class FullScreenImageActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        ImageView fullScreenImageView = (ImageView) findViewById(R.id.fullScreenImageView);
        Intent callingActivityIntent = getIntent();


        File imgFile = new  File(callingActivityIntent.getData().toString());

        if(imgFile.exists())
        {

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            fullScreenImageView.setImageBitmap(myBitmap);

        }
    }
}
