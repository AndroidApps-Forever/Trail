package com.example.trail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;

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
    }
}
