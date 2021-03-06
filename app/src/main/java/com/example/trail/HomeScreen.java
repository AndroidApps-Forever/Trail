package com.example.trail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trail.Adapters.HomeScreenListAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener,
        ConnectionCallbacks, OnConnectionFailedListener, ResultCallback<Status>
{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FloatingActionButton fab;
    TextView navHeader;

    private SimpleCursorAdapter adapter;

    //private ListView allChats;
    private static final String TAG = ChatActivity.class.getName();
    private static String sUserId;
    private ArrayList<ParseUser> mUsers = new ArrayList<>();;
    private GoogleApiClient mGoogleApiClient ;
    boolean mSignOutClicked;

    private ListView lvChat;
    HomeScreenListAdapter mAdapter;

    ArrayList<String> personId = new ArrayList<String>();

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newchatsdialog = new Intent(HomeScreen.this, FetchAllContacts.class);
                startActivity(newchatsdialog);
            }
        });
        buildGoogleApiClient();
        setupNavigationView();
        setupToolbar();

        if (ParseUser.getCurrentUser() != null) {
            System.out.println("getting current user!");
         // start with existing user
            startWithCurrentUser();
        }

        //handler.postDelayed(runnable, 100);
    }

    private void buildGoogleApiClient() {
        System.out.println("Building client");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void startWithCurrentUser() {
        sUserId = ParseUser.getCurrentUser().getObjectId();
        HOLDER.sUserId = sUserId;
        System.out.println("Current User Id: " + sUserId);
        setUpUserList();
    }

    private void setUpUserList() {
        lvChat = (ListView) findViewById(R.id.lvChats);
        lvChat.setTranscriptMode(1);
        receiveUser();
        System.out.println("Till here...............");
        mAdapter = new HomeScreenListAdapter(HomeScreen.this, sUserId, mUsers);
        lvChat.setAdapter(mAdapter);
        lvChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String rUserId = mUsers.get(position).getObjectId();
                String userName = mUsers.get(position).getUsername();
                System.out.println(" Setting adapter");
                System.out.println("receiver: " + rUserId);
                Intent i = new Intent(HomeScreen.this, ChatActivity.class);
                i.putExtra("Receiver_id", rUserId);
                i.putExtra("UserName", userName);
                //i.putExtra("Sender_id", sUserId);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpUserList();
    }

    private void refreshUser() {

        receiveUser();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        receiveUser();
//    }

    private void receiveUser() {
        // Construct query to execute
        List<ParseQuery<Message>> queries = new ArrayList<ParseQuery<Message>>();
        ParseQuery<Message> query1FromMsg = ParseQuery.getQuery(Message.class);
        query1FromMsg.whereEqualTo("userId", HOLDER.sUserId);
        ParseQuery<Message> query2FromMsg = ParseQuery.getQuery(Message.class);
        query2FromMsg.whereEqualTo("receiverId", HOLDER.sUserId);
        queries.add(query1FromMsg);
        queries.add(query2FromMsg);

        ParseQuery<Message> superquery = ParseQuery.or(queries);
        superquery.orderByDescending("updatedAt");
        superquery.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> list, ParseException e) {
                Set<String> hset = new HashSet<String>();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getUserId().equals(HOLDER.sUserId)) {
                        if (!hset.contains(list.get(i).getReceiverId())) {
                            hset.add(list.get(i).getReceiverId());
                        }
                    } else if (list.get(i).getReceiverId().equals(HOLDER.sUserId)) {
                        if (!hset.contains(list.get(i).getUserId())) {
                            hset.add(list.get(i).getUserId());
                        }
                    }
                }
                for (Object aHset : hset) {
                    personId.add(aHset + "");
                }
            }
        });
        System.out.println("Person Ids: " + personId);
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        System.out.println("Excuting query");
        query.orderByDescending("updatedAt");
        query.whereContainedIn("objectId",personId);
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
                    //mMessages.clear();
                    System.out.println("Adding users");
                    mUsers.clear();
                    System.out.println("test1");
                    mUsers.addAll(users);
                    System.out.println("test2");
                    mAdapter.notifyDataSetChanged(); // update adapter
                    System.out.println("test3");
                } else {
                    Log.d("message", "Error: " + e.getMessage());
                }
            }
        });
    }

    private void setupNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.me){
                    Toast.makeText(getApplicationContext(), "Opening My profile", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(HomeScreen.this, MyProfile.class);
                    startActivity(i);
                    drawerLayout.closeDrawers();
                    return true;
                }
                if (id == R.id.about) {
                    Log.d("Item Click : ", menuItem.getTitle().toString());
                    Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(HomeScreen.this, AboutActivity.class);
                    startActivity(i);
                    drawerLayout.closeDrawers();
                    return true;
                }
                if (id == R.id.createGeofence){
                    drawerLayout.closeDrawers();
                    Intent i = new Intent(HomeScreen.this, MapsGeofenceActivity.class);
                    startActivity(i);
                    return true;
                }
                if(id == R.id.sign_out){
                    System.out.println("Logout");
                    Toast.makeText(getApplicationContext(), "Clicked on logout", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(HomeScreen.this, MainActivity.class);
                    //mGoogleApiClient.connect();
                    System.err.println("LOG OUT ^^^^^^^^^ SUCESS");
                    drawerLayout.closeDrawers();
                    ParseUser.logOut();
                    startActivity(i);
                    return true;
                }
                else {
                    Toast.makeText(getApplicationContext(), "Activity not created yet", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });

        navHeader = (TextView)findViewById(R.id.text_header);
        navHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Calling Header", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null) {
            setSupportActionBar(toolbar);
        }    // Show menu icon
        final ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_black);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_homescreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id  = item.getItemId();

        System.out.println("ID" + id);
        if(id == android.R.id.home) {
            Toast.makeText(getApplicationContext(), "Drawer Open", Toast.LENGTH_SHORT).show();
            if(drawerLayout!=null) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mSignOutClicked = false;
        Log.i(TAG, "Connected to GoogleApiClient");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(Status status) {

    }
}
