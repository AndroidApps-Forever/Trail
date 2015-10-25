package com.example.trail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setupNavigationView();
        setupToolbar();
    }

    private void setupNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation);
//        edit_profile
    }


    private void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null) {
            setSupportActionBar(toolbar);
        }    // Show menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            if(drawerLayout!=null) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        if(id == R.id.action_find_location){
            Toast.makeText(getApplicationContext(), "Find your location", Toast.LENGTH_SHORT);
            return true;
        }
        if(id == R.id.about){
            Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT);
            Intent i = new Intent(this, aboutActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
