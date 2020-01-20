package com.retindode.screens;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ContainerActivity extends AppCompatActivity {

    public static ContainerActivity containerActivity;
    private static boolean hideToolbar;
    private static Fragment fragment;
    private static String toolbarTitle;
    private static String subtitle;

    public static void setFragment(Fragment fragment, String toolbarTitle) {
        ContainerActivity.fragment = fragment;
        ContainerActivity.toolbarTitle = toolbarTitle;
    }

    public static void setFragment(Fragment setMode, String title, String subtitle) {
        setFragment(setMode, title);
        ContainerActivity.subtitle = subtitle;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragment = null;
        toolbarTitle = null;
        subtitle = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        String toolbarTitle = ContainerActivity.toolbarTitle;

        setupToolbar(toolbarTitle);
        containerActivity = this;

        if (hideToolbar) {
            getSupportActionBar().hide();
        }

        //Removing shadow from Toolbar
        try {
            getSupportActionBar().setElevation(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadFragment(fragment);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

    }


    private void loadFragment(Fragment myFrag) {
        FragmentManager fragMan = getSupportFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();
        fragTransaction.add(R.id.content, myFrag, "fragment");
        fragTransaction.commit();
    }

    private void setupToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.MyToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (subtitle != null) {
            getSupportActionBar().setSubtitle(subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public static void setHideToolbar(boolean hideToolbar) {
        ContainerActivity.hideToolbar = hideToolbar;
    }
}
