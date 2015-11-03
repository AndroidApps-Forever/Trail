package com.example.trail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsGeofenceActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private double latitude;
    private double longitude;
    private String userAddress;
    private int geoFenceRadius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_geofence);

        if (mMap != null) {
            System.out.println("Enabling my the location");
            mMap.setMyLocationEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        // Add a marker in Sydney and move the camera
//        LatLng delhi = new LatLng(28.6454415,77.0907573);
//        mMap.addMarker(new MarkerOptions().position(delhi).title("New Delhi").snippet("New Delhi"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(delhi));
    }


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
    public void onMapLongClick(LatLng latLng) {
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
