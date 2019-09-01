package com.example.android.earthquakeapp;

import android.app.Application;
import android.util.Log;

import com.example.android.earthquakeapp.activity.UiUtils;
import com.example.android.earthquakeapp.provider.db.EarthquakeDataDbLoader;

public class EarthquakeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        mEarthquakeDataDbLoader = EarthquakeDataDbLoader.getInstance();
        if (UiUtils.isConnected(this)){
            Configurations.SETTINGS_CHANGED = false;
            mEarthquakeDataDbLoader.loadData(this, null);
        }
    }

    @Override
    public void onTerminate() {
        Log.d(TAG,mEarthquakeDataDbLoader.status());
        super.onTerminate();
    }

    private EarthquakeDataDbLoader mEarthquakeDataDbLoader;

    private static final String TAG = EarthquakeApplication.class.getSimpleName();
}
