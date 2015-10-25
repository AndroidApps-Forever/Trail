package com.example.trail;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ChatsActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FloatingActionButton fab;
    TextView navHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

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
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.edit_profile){
                    Log.d("Item Click : ", menuItem.getTitle().toString());
                    Toast.makeText(getApplicationContext(), "Edit Profile", Toast.LENGTH_SHORT);
                    drawerLayout.closeDrawers();
                    return true;
                }
                if(id == R.id.settings){
                    Log.d("Item Click : ", menuItem.getTitle().toString());
                    Toast.makeText(getApplicationContext(), "Change Settings", Toast.LENGTH_SHORT);
                    drawerLayout.closeDrawers();
                    Intent i = new Intent(ChatsActivity.this, SettingsActivity.class);
                    startActivity(i);
                    return true;
                }
                if(id == R.id.about){
                    Log.d("Item Click : ", menuItem.getTitle().toString());
                    Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT);
                    drawerLayout.closeDrawers();
                    Intent i = new Intent(ChatsActivity.this, AboutActivity.class);
                    startActivity(i);
                    return true;
                }
                else{
                    Toast.makeText(getApplicationContext(), "Activity not created yet", Toast.LENGTH_SHORT);
                    return false;
                }
            }
        });

        navHeader = (TextView)findViewById(R.id.text_header);
        navHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"Calling Header", Toast.LENGTH_LONG).show();
            }
        });
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
        getMenuInflater().inflate(R.menu.menu_chats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id  = item.getItemId();

        System.out.println("ID" + id);
        if(id == android.R.id.home) {
            Toast.makeText(getApplicationContext(), "Drawer Open", Toast.LENGTH_SHORT);
            if(drawerLayout!=null) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
