package com.example.android.earthquakeapp;

import androidx.lifecycle.LifecycleOwner;

public interface EarthquakeCallback extends LifecycleOwner {
    void notifyEarthquakeFinalCount(final int numEarthquake);
    void notifyNewData(final int numEarthquakeAdded, int numJobTotal);
    void startLoading();
}
