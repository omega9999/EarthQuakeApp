package com.example.android.earthquakeapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;



public class AppLiveService extends Service {
    public AppLiveService() {
        Log.d(TAG, "AppLiveService.constructor");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "AppLiveService.onCreate()");
    }

    /**
     * call when service is killed
     * @param rootIntent
     */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        System.out.println("onTaskRemoved called");
        Log.d(TAG, "AppLiveService.onTaskRemoved()");
        super.onTaskRemoved(rootIntent);
        //do something you want
        //stop service
        this.stopSelf();
    }

    private static final String TAG = EarthquakeApplication.class.getSimpleName();
}
