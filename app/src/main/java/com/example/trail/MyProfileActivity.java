package com.example.trail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

public class MyProfileActivity extends AppCompatActivity {

    private ImageView imageViewRound;
    ListView list;
    String[] groupname ={
            "Group1",
            "Group2",
            "Group3",
    };

    int[] imgid={
            R.drawable.grppic,
            R.drawable.grppic,
            R.drawable.grppic,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        imageViewRound=(ImageView)findViewById(R.id.imageView_round);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.profpic);

        imageViewRound.setImageBitmap(icon);

        CustomListAdapter adapter=new CustomListAdapter(this, groupname, imgid);
        list=(ListView)findViewById(R.id.listView);
        list.setAdapter(adapter);

    }

    public void changeProfilePic(View v)
    {
        Intent i = new Intent(this, ChangeProfilePicture.class);
        startActivity(i);
    }
}
