package com.example.trail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.trail.Adapters.ChatListAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anjali on 24-11-2015.
 */
public class ChatActivity extends AppCompatActivity {

    private static final String TAG = ChatActivity.class.getName();
    private static String sUserId, rUserId;

    public static final String USER_ID_KEY = "userId";
    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;

    private EditText etMessage;
    private Button btSend;
    private ListView lvChat;
    private ArrayList<Message> mMessages;
    private ChatListAdapter mAdapter;
    // Keep track of initial load to scroll to the bottom of the ListView
    private boolean mFirstLoad;
    private String[] users = new String[2];
    // Create a handler which can run code periodically
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent i = getIntent();
        rUserId = i.getStringExtra("Receiver_id");
        sUserId = i.getStringExtra("Sender_id");
        //System.out.println(rUserId + " " + sUserId);
        users[0] = rUserId;
        users[1] = sUserId;
        startWithCurrentUser();
        receiveMessage();
        // Run the runnable object defined every 100ms
        handler.postDelayed(runnable, 1000);
    }

    private void startWithCurrentUser() {
        setupMessagePosting();
    }

    private void setupMessagePosting() {
        // Find the text field and button
        etMessage = (EditText) findViewById(R.id.etMessage);
        btSend = (Button) findViewById(R.id.btSend);
        lvChat = (ListView) findViewById(R.id.lvChat);
        mMessages = new ArrayList<Message>();
        // Automatically scroll to the bottom when a data set
        // change notification is received and only if the last item is
        // already visible on screen. Don't scroll to the bottom otherwise.
        lvChat.setTranscriptMode(1);
        mFirstLoad = true;
        mAdapter = new ChatListAdapter(ChatActivity.this, sUserId, mMessages);
        lvChat.setAdapter(mAdapter);
        btSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String body = etMessage.getText().toString();
                receiveMessage();
                // Use Message model to create new messages now
                if (body != null) {
                    Message message = new Message();
                    message.setUserId(sUserId);
                    message.setReceiverId(rUserId);
                    message.setBody(body);
                    message.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            receiveMessage();
                        }
                    });
                    etMessage.setText("");
                }
            }
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            handler.postDelayed(this, 1000);
        }
    };

    private void refreshMessages() {
        receiveMessage();
    }

    private void receiveMessage() {
        // Construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        // Configure limit and sort order
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.orderByAscending("createdAt");
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.whereContainedIn("userId", Arrays.asList(users));
        query.whereContainedIn("receiverId", Arrays.asList(users));
        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    mMessages.clear();
                    mMessages.addAll(messages);
                    mAdapter.notifyDataSetChanged(); // update adapter
                    // Scroll to the bottom of the list on initial load
                    if (mFirstLoad) {
                        lvChat.setSelection(mAdapter.getCount() - 1);
                        mFirstLoad = false;
                    }
                } else {
                    Log.d("message", "Error: " + e.getMessage());
                }
            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chats1, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id  = item.getItemId();

        System.out.println("ID" + id);

        if(id == R.id.action_find_location){
            Toast.makeText(getApplicationContext(), "Opening location", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MapsChatActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        receiveMessage();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
