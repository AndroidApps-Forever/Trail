package com.example.trail;


import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.InputStream;

public class MainActivity extends Activity implements View.OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {

    private static final int RC_SIGN_IN =0;
    private static final String TAG = "MainActivity";

    private GoogleApiClient mGoogleApiClient;
    Bundle b = new Bundle();

    private boolean mSignInClicked;

    private ConnectionResult mConnectionResult;

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
        System.out.println("I am done with creation");

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
    protected void onDestroy(){
        Log.e(TAG, "Application Destroyed");
        super.onDestroy();
    }
    @Override
    public void onConnected(Bundle bundle) {
        String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
        if(email.endsWith("@iiitd.ac.in")) {
            Toast.makeText(this, "User is connected, Yay!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, HomeScreen.class);

            // Get user's information
            getProfileInformation();
            intent.putExtras(b);
            startActivity(intent);
            mSignInClicked = false;
        }
        else {
            Toast.makeText(this, "Not a IIITD ID", Toast.LENGTH_LONG).show();
            mGoogleApiClient.disconnect();
            //mSignInClicked = false;
            resolveSignInError();
        }
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
        }
        else {
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
    protected void onActivityResult(int requestCode, int responseCode,Intent intent) {
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
    }

    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }

    }

    private void resolveSignInError() {
        System.out.println("Mconnection result " + mConnectionResult);
        if (mConnectionResult.hasResolution()) {

            try {

                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {

                mGoogleApiClient.connect();
            }
        }
    }

    public void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);
                /*Holder.USER_NAME = personName;
                Holder.EMAIL_ID = email;*/
                b.putString("txtName", personName);
                b.putString("txtEmail", email);
                b.putString("txtURL", personPhotoUrl );

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
