package com.example.trail;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
<<<<<<< HEAD
=======
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
>>>>>>> f5545e5185bb1805ac98a8807db409fc2fb0004c
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends Activity implements View.OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 0;
    private static final String TAG = "MainActivity";

    private GoogleApiClient mGoogleApiClient;
    //Bundle b = new Bundle();
    SharedPreferences sp;

    private boolean mSignInClicked;

    private ConnectionResult mConnectionResult;
    String sUserId;
    Person currentPerson;
    String personName, personPhotoUrl, personGooglePlusProfile, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SignInButton btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);

        btnSignIn.setOnClickListener(this);

        // Initializing google plus api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
        sp = getSharedPreferences(HOLDER.SHARED_PREFERENCES_NAME, Context.MODE_APPEND);
    }


    protected void onStart() {
        Log.v(TAG, "Start");
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        Log.v(TAG, "Stop");
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "Application Destroyed");
        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle bundle) {

        Toast.makeText(this, "User is connected, Yay!", Toast.LENGTH_LONG).show();
        setProfileInformation();
        mSignInClicked = false;
    }


    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
        //updateUI(false);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d(TAG, "onConnectionFailed:" + result);

        if (!result.hasResolution()) {
            System.out.println("We're going to give error dialog.");
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
        } else {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        //System.out.println("Hello");
        Log.v(TAG, "ActivityResult: " + requestCode);
        if ((requestCode == RC_SIGN_IN) && (responseCode == RESULT_OK)) {
            mSignInClicked = true;
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                // Signin button clicked
                signInWithGplus();
                break;
            /*case R.id.btn_sign_out:
                // Signout button clicked
                signOutFromGplus();
                break;*/
        }
    }

    private void signOutFromGplus() {
        //if(Holder.FLAG==1) {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
        System.out.println("Logout!");
    }

    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }

    }

    private void resolveSignInError() {
        System.out.println("M connection result " + mConnectionResult);
        if (mConnectionResult.hasResolution()) {
            System.out.println("Here1");
            try {

                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                System.out.println("Here2");
                mGoogleApiClient.connect();
            }
        }
    }

    public void setProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                personName = currentPerson.getDisplayName();
                personPhotoUrl = currentPerson.getImage().getUrl();
                personGooglePlusProfile = currentPerson.getUrl();

                email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                HOLDER.user = currentPerson;
                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);
                SharedPreferences.Editor editor = sp.edit();
                System.out.println("currentPerson" + currentPerson.toString());
                // ----------------Putting in shared preference.
                editor.putString("personName", personName);
                editor.putString("photoUrl", personPhotoUrl);
                editor.putString("profile", personGooglePlusProfile);
                editor.putString("currentEmail", email);
                editor.apply();
                signOutFromGplus();
                // I check in the data base if(my email id of user exist in data base)
                checkUser();
//                Intent intent = new Intent(this, HomeScreen.class);
//                startActivity(intent);

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkUser() {
        //check if there already is a user
        String usernametxt, passwordtxt;
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            //if there is a current user.
            Intent intent = new Intent(MainActivity.this, HomeScreen.class);
            startActivity(intent);
            finish();
        } else {
            //signIn to get the current user
            // Retrieve the text entered from the EditText
            usernametxt = personName;
            passwordtxt = "password";
            System.out.println("userName: " + usernametxt);
            // Send data to Parse.com for verification
            ParseUser.logInInBackground(usernametxt, passwordtxt,
                    new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            System.out.println("user: " + user);
                            //System.out.println("exception: " + e.toString());
                            if (user != null) {
                                sUserId = ParseUser.getCurrentUser().getObjectId();
                                System.out.println("Current User Id: " + sUserId);
                                // If user exist and authenticated, send user to Welcome.class
                                login();
                                Toast.makeText(getApplicationContext(),
                                        "Successfully Logged in",
                                        Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        e.toString(),
                                        Toast.LENGTH_LONG).show();
                                        createNewUser();
                            }
                        }
                    });
        }
    }

    private void createNewUser() {
        System.out.println("Creating new user");
        ParseUser user = new ParseUser();
        user.setUsername(personName);
        user.setEmail(email);
        user.setPassword("password");
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Show a simple Toast message upon successful registration
                    Toast.makeText(getApplicationContext(),
                            "Successfully Signed up.",
                            Toast.LENGTH_LONG).show();
                    login();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Sign up Error", Toast.LENGTH_LONG)
                            .show();
                    signOutFromGplus();
                }
            }
        });
    }

<<<<<<< HEAD
//    private void startWithCurrentUser() {
//        //KLLmVL886h
//        sUserId = ParseUser.getCurrentUser().getObjectId();
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString("sUserId", sUserId);
//        editor.commit();
//    }

=======
>>>>>>> f5545e5185bb1805ac98a8807db409fc2fb0004c
    private void login() {
        //KLLmVL886h
        Intent intent = new Intent(
                MainActivity.this,
                HomeScreen.class);
        startActivity(intent);
    }
}
