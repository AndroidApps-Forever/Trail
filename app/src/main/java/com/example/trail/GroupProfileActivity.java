package com.example.trail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.trail.Adapters.CustomListAdapter;

public class GroupProfileActivity extends AppCompatActivity {

    private ImageView roundImageView;
    ListView list;
    String[] members ={
            "Member1",
            "Member2",
            "Member3",
    };

    int[] imgid={
            R.drawable.profilepic,
            R.drawable.profilepic,
            R.drawable.profilepic,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_group_profile);

        roundImageView=(ImageView)findViewById(R.id.imageView_round);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.group);

        roundImageView.setImageBitmap(icon);

        CustomListAdapter adapter=new CustomListAdapter(this, members, imgid);
        list=(ListView)findViewById(R.id.listView);
        list.setAdapter(adapter);
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null) {
            setSupportActionBar(toolbar);
        }    // Show menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


        //return super.onOptionsItemSelected(item);
    }
}
