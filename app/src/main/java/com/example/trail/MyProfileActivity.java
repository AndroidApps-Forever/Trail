package com.example.trail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.trail.Adapters.CustomListAdapter;

import java.util.ArrayList;

public class MyProfileActivity extends AppCompatActivity {

    private ImageView imageViewRound;
    ListView list;
    String[] groupname ={
            "Group1",
            "Group2",
            "Group3",
    };

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;     //recycler

    int[] imgid={
            R.drawable.grppic,
            R.drawable.grppic,
            R.drawable.grppic,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_my_profile);

        imageViewRound=(ImageView)findViewById(R.id.imageView_round);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.profpic);

        imageViewRound.setImageBitmap(icon);

        CustomListAdapter adapter=new CustomListAdapter(this, groupname, imgid);
        //list=(ListView)findViewById(R.id.listView);
        //list.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar!=null) {
            setSupportActionBar(toolbar);
        }    // Show menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        ArrayList<ViewOnCard> people = new ArrayList<ViewOnCard>();

        people.add(0,new ViewOnCard("Name","Person's name"));
        people.add(1,new ViewOnCard("Phone","9876543210"));
        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(people);
        mRecyclerView.setAdapter(mAdapter);


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



    public void changeProfilePic(View v)
    {
        Intent i = new Intent(this, ChangeProfilePicture.class);
        startActivity(i);
    }
}
