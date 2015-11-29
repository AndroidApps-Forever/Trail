package com.example.trail;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.plus.model.people.Person;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Anjali on 02-11-2015.
 */
public final class HOLDER {

    private HOLDER(){

    }


    public static int GEOFENCE_COUNT =0;

    public static final String PACKAGE_NAME = "com.example.trail";

    public static final String SHARED_PREFERENCES_NAME = PACKAGE_NAME + ".SHARED_PREFERENCES_NAME";

    public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";

    public static Person user;
    public static String sUserId;

    public static ArrayList<LatLng> GEOFENCES = new ArrayList<LatLng>(5);


   /* *//* Expiration in Days -----  7 days *//*
    public static final long GEOFENCE_EXPIRATION_IN_DAYS = 7;
    *//*Expiration in Hours *//*
    public static final long GEOFENCE_EXPIRATION_IN_HOURS = GEOFENCE_EXPIRATION_IN_DAYS * 24;

    *//* Expiration in Millisecond *//*
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;

    *//*Radius of Geofence in Meters*//*
    public static final float GEOFENCE_RADIUS_IN_METERS = 500; // 0.5 Km

    public static int FLAG = 0;*/
    /**
     * Map for storing information about metro Station in the Okhla area.
     */
    /*public static HashMap<String, LatLng> GEOFENCES = new HashMap<String, LatLng>();
    static {
        GEOFENCES.put("Academic block", new LatLng(28.5444498,77.2726199));
    }*/

    /**
     * Google API project id registered to use GCM.
     */
    public static String SENDER_ID = "";//TODO provide project id e.g. 4815162342
}
