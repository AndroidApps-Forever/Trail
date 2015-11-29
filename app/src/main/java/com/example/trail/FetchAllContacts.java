package com.example.trail;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
    private ActionMode mActionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_all_contacts);

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
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
        //receiveUser();
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

        /*lvChat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //update the list
                lvChat.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                mActionMode = startActionMode(new ActionModeCallback());
                return true;
            }
        });
    }*/
    /*private class ActionModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // inflate contextual menu
            mode.getMenuInflater().inflate(R.menu.contextual_contacts_list_view, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        //Called when the button create group is created.
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Toast.makeText(FetchAllContacts.this, "Create Group", Toast.LENGTH_LONG).show();
            SparseBooleanArray checked = lvChat.getCheckedItemPositions();
            if(checked.size() < 5) {
                ArrayList<String> selectedItems = new ArrayList<String>();
                for (int i = 0; i < checked.size(); i++) {
                    // Item position in adapter
                    int position = checked.keyAt(i);
                    // Add sport if it is checked i.e.) == TRUE!
                    if (checked.valueAt(i))
                        selectedItems.add(mUsers.get(position).getObjectId());

                    //PERFORM TASKS ON THE CLICK OF CREATE GROUP
                    Intent intent = new Intent(FetchAllContacts.this, ChatActivity.class);
                    //Create a group in data base
                    //i.putExtra("sUserId", group Id)
                    //i.putExtra()
                    // close action mode
                    mode.finish();
                    return true;
                }
            }
            else{
                Toast.makeText(FetchAllContacts.this, "Only 5 people in a group allowed", Toast.LENGTH_LONG).show();
                return false;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

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
}}*/
    }
}