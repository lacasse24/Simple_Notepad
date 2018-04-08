package com.lac.alex.simplenotepad;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Alex on 2017-06-27.
 */

public abstract class ActivityFragmentFactory extends AppCompatActivity
{
    protected abstract Fragment createFragment();

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_container_of_fragment);
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.drawable.ic_launcher);


            if (fragment == null)
            {
                fragment = createFragment(); // Permet de créer le type de fragment qu'on veut selon le type !
                fm.beginTransaction().add(R.id.fragmentContainer,fragment).commit();
            }
        }
}

