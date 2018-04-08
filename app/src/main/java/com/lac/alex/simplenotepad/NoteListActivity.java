package com.lac.alex.simplenotepad;

import android.support.v4.app.Fragment;

public class NoteListActivity extends ActivityFragmentFactory
{
    @Override
    protected Fragment createFragment()
    {
        return new NoteListFragment();
    }

}
