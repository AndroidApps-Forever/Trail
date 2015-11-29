package com.example.trail;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.trail.Adapters.HomeScreenListAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FetchAllContacts extends AppCompatActivity {

    private static String sUserId;
    private ArrayList<ParseUser> mUsers;
    public static final String USER_ID_KEY = "userId";
    private ListView lvChat;
    HomeScreenListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_all_contacts);

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null) {
            setSupportActionBar(toolbar);
        }    // Show menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        sUserId = HOLDER.sUserId;
        setUpUserList();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpUserList() {
        lvChat = (ListView) findViewById(R.id.listView3);
        mUsers = new ArrayList<>();
        receiveUser();
        lvChat.setTranscriptMode(1);

        mAdapter = new HomeScreenListAdapter(FetchAllContacts.this, sUserId, mUsers);
        lvChat.setAdapter(mAdapter);
        lvChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String rUserId = mUsers.get(position).getObjectId();
                String userName = mUsers.get(position).getUsername();
                System.out.println(" Setting adapter");
                System.out.println("receiver: " + rUserId);
                Intent i = new Intent(FetchAllContacts.this, ChatActivity.class);
                i.putExtra("Receiver_id", rUserId);
                i.putExtra("UserName", userName);
                //i.putExtra("Sender_id", sUserId);
                startActivity(i);
            }
        });
    }

    private void receiveUser() {
        // Construct query to execute
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        System.out.println("Excuting query");
        query.orderByDescending("updatedAt");
        query.whereNotEqualTo("objectId", sUserId);
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
                    //mMessages.clear();
                    System.out.println("Adding users");
                    mUsers.addAll(users);
                    mAdapter.notifyDataSetChanged(); // update adapter
                } else {
                    Log.d("message", "Error: " + e.getMessage());
                }
            }
        });
    }
}
