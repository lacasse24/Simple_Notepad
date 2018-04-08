package com.lac.alex.simplenotepad;

import android.support.v4.app.Fragment;

public class SingleNoteActivity extends ActivityFragmentFactory
{
    @Override
    protected Fragment createFragment()
    {
        return new SingleNoteFragment();
    }

}
