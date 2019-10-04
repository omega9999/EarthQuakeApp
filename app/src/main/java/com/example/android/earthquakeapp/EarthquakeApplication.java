package com.example.android.earthquakeapp;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.work.Configuration;

import com.example.android.earthquakeapp.activity.UiUtils;

import java.util.concurrent.Executors;

public class EarthquakeApplication extends Application implements LifecycleEventObserver, Configuration.Provider {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");

        // when service is killed then app is killed
        startService(new Intent(this, AppLiveService.class));

        //ActivityManager app died, no saved state

        //TODO lifecycle app
        //TODO https://developer.android.com/topic/libraries/architecture/lifecycle#java
        //TODO https://developer.android.com/reference/androidx/lifecycle/ProcessLifecycleOwner.html
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }


    @Override
    public void onStateChanged(@NonNull final LifecycleOwner source, @NonNull final Lifecycle.Event event) {
        Log.v(TAG,"onStateChanged " + event);
        System.out.println(TAG +" onStateChanged " + event);
    }

    @Override
    protected void finalize() throws Throwable {
        Log.d(TAG, "finalize()");
        super.finalize();
    }

    @Override
    public void onTerminate() {
        Log.d(TAG, "onTerminate()");
        super.onTerminate();
    }



    @NonNull
    @Override
    public Configuration getWorkManagerConfiguration() {
        return new Configuration.Builder()
                //.setExecutor(Executors.newFixedThreadPool(10))
                .setMinimumLoggingLevel(Log.VERBOSE)
                .build();
    }


    private static final String TAG = EarthquakeApplication.class.getSimpleName();
}
