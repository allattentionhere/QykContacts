package com.allattentionhere.qykcontacts.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.allattentionhere.qykcontacts.Fragments.ContactsFragment;
import com.allattentionhere.qykcontacts.Fragments.FavoritesFragment;
import com.allattentionhere.qykcontacts.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FrameLayout fl_content;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    int selected_id = -1;
    boolean isNewItemClicked = true;
    android.support.v4.app.FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();
        init();
        setListener();
        fragmentManager = this.getSupportFragmentManager();
        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    private void setListener() {
        navigationView.setNavigationItemSelectedListener(this);

        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (isNewItemClicked) {
                    switch (selected_id) {
                        case R.id.nav_contacts:
                            fragmentManager.beginTransaction().replace(R.id.fl_content, ContactsFragment.newInstance()).commit();
                            break;
                        case R.id.nav_favorites:
                            fragmentManager.beginTransaction().replace(R.id.fl_content, FavoritesFragment.newInstance()).commit();
                            break;

                    }
                    isNewItemClicked=false;
                }
            }
            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }

    private void init() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        this.getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, ContactsFragment.newInstance()).commit();
    }

    private void initLayout() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fl_content = (FrameLayout) findViewById(R.id.fl_content);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        if (selected_id == item.getItemId()) {
            isNewItemClicked = false;
        } else {
            isNewItemClicked = true;
        }
        selected_id = item.getItemId();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK){
            this.getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, ContactsFragment.newInstance()).commit();
        }

    }
}
