package com.example.android.earthquakeapp;

public interface EarthquakeCallback {
    void notifyEarthquakeFinalCount(final int numEarthquake);
    void notifyNewData();
}
