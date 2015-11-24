package com.example.trail;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    public static final String YOUR_APPLICATION_ID = "rwfzzBKFDB3wsBVvqKcEI0JadlkmJnYEKq8jgUa4";
    public static final String YOUR_CLIENT_KEY = "8sI46dTVztI5Wb9QHVAI0QficGRu8G5ZeThbPn8w";

    @Override
    public void onCreate() {
        super.onCreate();

        // Add your initialization code here
        ParseObject.registerSubclass(Message.class);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        // Test creation of object
        /*ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();*/
    }
}
