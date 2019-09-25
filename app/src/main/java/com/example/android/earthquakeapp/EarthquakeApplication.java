package com.example.android.earthquakeapp;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Configuration;

import com.example.android.earthquakeapp.activity.UiUtils;

import java.util.concurrent.Executors;

public class EarthquakeApplication extends Application implements  Configuration.Provider {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate()");

        //ActivityManager app died, no saved state

        //TODO lifecycle app
        //TODO https://developer.android.com/topic/libraries/architecture/lifecycle#java
        //TODO https://developer.android.com/reference/androidx/lifecycle/ProcessLifecycleOwner.html
    }

    @Override
    protected void finalize() throws Throwable {
        Log.d(TAG,"finalize()");
        super.finalize();
    }

    @Override
    public void onTerminate() {
        Log.d(TAG,"onTerminate()");
        super.onTerminate();
    }

    private static final String TAG = EarthquakeApplication.class.getSimpleName();

    @NonNull
    @Override
    public Configuration getWorkManagerConfiguration() {
        return new Configuration.Builder()
                //.setExecutor(Executors.newFixedThreadPool(10))
                .setMinimumLoggingLevel(Log.VERBOSE)
                .build();
    }
}
