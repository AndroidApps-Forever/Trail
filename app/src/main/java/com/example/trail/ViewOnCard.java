package com.example.trail;

/**
 * Created by shambhavi on 11/22/2015.
 */
public class ViewOnCard {
    String heading;
    String value;

    ViewOnCard(String h,String v)
    {
        this.heading = h;
        this.value = v;
    }

    public String getHeading() {
        return heading;
    }

    public String getValue() {
        return value;
    }
}
