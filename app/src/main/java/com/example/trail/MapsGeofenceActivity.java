package com.example.trail;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsGeofenceActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
        ResultCallback<Status> , ConnectionCallbacks,
        OnConnectionFailedListener {
    ////////////////////////////////////////////////////////////////////////////////////////////////

    Toolbar toolbar;
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private GoogleMap mMap;
    private double latitude;
    private double longitude;
    private String userAddress;
    private static String TAG = "MapsGeofence";
    int geoFenceRadius = 500;

    // For Geofences ///////////////////////////////////////////////////////////////////////////////
    SharedPreferences mSharedPreferences;
    private static final int geofenceRadius=500;
    protected ArrayList<Geofence> mGeofenceList;
    private GoogleApiClient mLocationClient ;
    List<LatLng> geofenceLocations = new ArrayList<LatLng>(10);

    ///Buttons called when clicked on the marker///////////////////////////////////////////////////////////
    private Button mAddGeofencesButton;
    private Button mRemoveGeofencesButton;
    private boolean mGeofencesAdded;
    private PendingIntent mGeofencePendingIntent;


    //////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_geofence);

        setupToolbar();
        if (mMap != null) {
            System.out.println("Enabling my the location");
            mMap.setMyLocationEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // -------For Geofences --------------------

        //Empty list for storing geofences
        mGeofenceList = new ArrayList<>();
        mGeofencePendingIntent = null;

        SharedPreferences mSharedPreferences = getSharedPreferences(HOLDER.SHARED_PREFERENCES_NAME,
                MODE_PRIVATE);

        // Get the value of mGeofencesAdded from SharedPreferences. Set to false as a default.
        mGeofencesAdded = mSharedPreferences.getBoolean(HOLDER.GEOFENCES_ADDED_KEY, false);

        populateGeofenceList();
        buildLocationApiClient();

    }

    private void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null) {
            setSupportActionBar(toolbar);
        }
        // Show menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
    }

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

    protected synchronized void buildLocationApiClient() {
        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //New delhi
        LatLng point = new LatLng(28.607335730210824,77.21330847591162);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
        mMap.setOnMapLongClickListener(this);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onMapLongClick(LatLng point) {
        int count=0;
        count = HOLDER.GEOFENCE_COUNT;
        if(count < 10) {
            HOLDER.GEOFENCE_COUNT++ ;
            count = HOLDER.GEOFENCE_COUNT;
            System.out.println(count);
            System.out.println(point);
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            /*---- Adding marker -----*/
            mMap.addMarker(new MarkerOptions()
                    .position(point)
                    .title("Geofence_" + count + " Added").snippet(point.toString())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(point));

            //setting circle
            CircleOptions circleOptions = new CircleOptions()
                    .center(point).radius(HOLDER.GEOFENCE_RADIUS_IN_METERS)
                    .fillColor(0x40ff0000)
                    .strokeColor(Color.TRANSPARENT)
                    .strokeWidth(2);

            //ADDING THE CIRCLE OF THE RESPECTIVE RADIUS ON THE MAP
            mMap.addCircle(circleOptions);
            //---- Calling add Geofence function ---------------------------------------------------------
            HOLDER.GEOFENCES.put("Geofence_" + count , point);
            addGeofencesHandler();
        }
        else{
            Toast.makeText(this, "Only 10 Geofences allowed!", Toast.LENGTH_SHORT);
        }

        /*Adding cancellation on marker click*/
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                System.out.println("Point already had geofence");
                marker.remove();
                removeGeofencesHandler();
                return false;
            }
        });

    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    //  Clicked onlong click on map. Adds geofences, which sets alerts to be notified
    public void addGeofencesHandler() {
        if (!mLocationClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            LocationServices.GeofencingApi.addGeofences(
                    mLocationClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            logSecurityException(securityException);
        }
    }

    //remove geofences
    public void removeGeofencesHandler(){

        if (!mLocationClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            LocationServices.GeofencingApi.removeGeofences(
                    mLocationClient,
                    getGeofencePendingIntent()
            ).setResultCallback(this);
        } catch (SecurityException securityException) {
            logSecurityException(securityException);
        }
    }

    @Override
    public void onResult(Status status) {
        System.out.println(status);
        if (status.isSuccess()) {
            // Update state and save in shared preferences.
            //mGeofencesAdded = !mGeofencesAdded;
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(HOLDER.GEOFENCES_ADDED_KEY, mGeofencesAdded);
            editor.apply();

            // Update the UI. Adding geofences enables the Remove Geofences button, and removing
            // geofences enables the Add Geofences button.
            //setButtonsEnabledState();

            Toast.makeText(
                    this,
                    getString(mGeofencesAdded ? R.string.geofences_added :
                            R.string.geofences_removed),
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    status.getStatusCode());
            Log.e(TAG, errorMessage);
        }
    }

    private void logSecurityException(SecurityException securityException) {
        Log.e(TAG, "Invalid location permission. " +
                "You need to use ACCESS_FINE_LOCATION with geofences", securityException);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    private PendingIntent getGeofencePendingIntent(){
        //to check if already a pendingIntent exists.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        //with the new intent start the service which will note down the transtions.
        Intent intent = new Intent(this, GeofenceTransitionIntentService.class);
        return PendingIntent.getService(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(mGeofenceList);

        // Return a GeofencingRequest.
        return builder.build();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    //Creating the geofence around the academic block.
    public void populateGeofenceList() {
        for (Map.Entry<String, LatLng> entry : HOLDER.GEOFENCES.entrySet()) {
            //adding new geofence to object
            mGeofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(entry.getKey())

                            // Set the circular region of this geofence.
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            HOLDER.GEOFENCE_RADIUS_IN_METERS
                    )

                            // Set the expiration duration of the geofence. This geofence gets automatically
                            // removed after this period of time.
                    .setExpirationDuration(HOLDER.GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                            // Set the transition types of interest. Alerts are only generated for these
                            // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)

                            // Create the geofence.
                    .build());
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onStart(){
        super.onStart();
        mLocationClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.disconnect();
    }

    @Override
    protected void onDestroy(){
        Log.e(TAG, "Destroy");
        super.onDestroy();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    //FUNCTION TO EXTRACT LATITUDE AND LONGITUDE OF AN ADDRESS
    public Barcode.GeoPoint getLocationFromAddress(String strAddress) throws IOException {

        Geocoder coder = new Geocoder(this, Locale.getDefault());
        List<Address> address;
        Barcode.GeoPoint p1 = null;

        address = coder.getFromLocationName(strAddress,5);
        if (address == null) {
            return null;
        }
        Address location = address.get(0);
        location.getLatitude();
        location.getLongitude();

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        p1 = new Barcode.GeoPoint();
        p1.lat = (location.getLatitude() * 1E6);
        p1.lng = (location.getLongitude() * 1E6);

        return p1;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Connected to GoogleApiClient");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    //@Override
    public void onMapLongClickNotUsed(LatLng latLng) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText strAddress = new EditText(this);
        strAddress.setHint("Type your address");
        strAddress.setTextColor(Color.BLACK);
        layout.addView(strAddress);

        final EditText radius = new EditText(this);
        radius.setHint("Type the geofence radius");
        radius.setTextColor(Color.BLACK);
        layout.addView(radius);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Add Geofence");
        dialog.setView(layout);

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
            }
        });

        dialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                userAddress = strAddress.getText().toString();
                geoFenceRadius = Integer.valueOf(radius.getText().toString());
                Barcode.GeoPoint p = new Barcode.GeoPoint();
                try {
                    p = getLocationFromAddress(userAddress);
                    Log.i("Lat&Long",latitude + " " + longitude);
                    LatLng newLoc = new LatLng(latitude,longitude);
                    mMap.addMarker(new MarkerOptions().position(newLoc).title("New Delhi").snippet(userAddress));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(newLoc));

                    CircleOptions circleOptions = new CircleOptions()
                            .center(newLoc).radius(geoFenceRadius)
                            .fillColor(0x40ff0000)
                            .strokeColor(Color.TRANSPARENT)
                            .strokeWidth(2);

                    //ADDING THE CIRCLE OF THE RESPECTIVE RADIUS ON THE MAP
                    mMap.addCircle(circleOptions);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(MapsGeofenceActivity.this, "Latitude = " + p.lat + "\nLongitude = " + p.lng, Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog d = dialog.create();
        d.show();
    }
}
