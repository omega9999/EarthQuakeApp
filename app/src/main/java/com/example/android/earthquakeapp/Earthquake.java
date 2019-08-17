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

    public String getUrl() {
        return mUrl;
    }

    public Earthquake setUrl(String url) {
        this.mUrl = url;
        return this;
    }

    public String getId() {
        return mId;
    }

    public Earthquake setId(String id) {
        this.mId = id;
        return this;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public Earthquake setLongitude(double longitude) {
        this.mLongitude = longitude;
        return this;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public Earthquake setLatitude(double latitude) {
        this.mLatitude = latitude;
        return this;
    }

    public double getDept() {
        return mDept;
    }

    public Earthquake setDept(double dept) {
        this.mDept = dept;
        return this;
    }

    public boolean isCoordinates() {
        return mCoordinates;
    }

    public Earthquake setCoordinates(boolean hasCoordinates) {
        this.mCoordinates = hasCoordinates;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (mId == null) return false;
        Earthquake that = (Earthquake) o;
        return mId.equals(that.mId);
    }

    @Override
    public int hashCode() {
        return mId.hashCode();
    }

    private boolean mCoordinates = false;
    private double mMagnitude;
    private String mPrimaryLocation;
    private String mLocationOffset;
    private Date mDate;
    private String mUrl;
    private String mId;
    private double mLongitude;
    private double mLatitude;
    private double mDept;
}
