package com.example.trail;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class aboutActivity extends AppCompatActivity {

    TextView aboutTitle, aboutText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        aboutTitle = (TextView)findViewById(R.id.title_about);
        aboutText = (TextView)findViewById(R.id.text_about);

        setValuesofText();

    }

    private void setValuesofText() {
        aboutTitle.setText("About Smoothie");
        aboutText.setText("Trail is app following the concept of \"Follow your friends " +
                "where-ever they go\". The app was developed as the course project under the " +
                "supervision of Prof. Vinayak Nayak in the course Mobile Computing.");
    }

}
