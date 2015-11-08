package com.example.trail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class GroupchatActivity extends AppCompatActivity {
    Button btnMap, btnChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnMap = (Button)findViewById(R.id.mapBtn);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GroupchatActivity.this, MapsGeofenceActivity.class);
                startActivity(i);
            }
        });

        btnChat = (Button)findViewById(R.id.chatBtn1);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(GroupchatActivity.this, GroupchatActivity.class);
                startActivity(i1);
            }
        });
    }

}
