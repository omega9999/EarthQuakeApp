package com.example.android.earthquakeapp;

import java.util.Date;

public class Earthquake {

    public Earthquake() {
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public Earthquake setMagnitude(double magnitude) {
        this.mMagnitude = magnitude;
        return this;
    }

    public String getPrimaryLocation() {
        return mPrimaryLocation;
    }

    public Earthquake setPrimaryLocation(String location) {
        this.mPrimaryLocation = location;
        return this;
    }

    public String getLocationOffset() {
        return mLocationOffset;
    }

    public Earthquake setLocationOffset(String location) {
        this.mLocationOffset = location;
        return this;
    }
    public Date getDate() {
        return mDate;
    }

    public Earthquake setDate(Date date) {
        this.mDate = date;
        return this;
    }



    private double mMagnitude;
    private String mPrimaryLocation;
    private String mLocationOffset;
    private Date mDate;
}
