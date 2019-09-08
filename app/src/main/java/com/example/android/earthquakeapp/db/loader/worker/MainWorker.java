package com.example.android.earthquakeapp.db.loader.worker;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.android.earthquakeapp.Configurations;
import com.example.android.earthquakeapp.db.room.DbUtils;

public class MainWorker extends Worker {

    public MainWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        final int rows = DbUtils.deleteAllQuake((Application) getApplicationContext());
        Log.d(TAG, "Deleted rows " + rows);
        return Result.success();
    }


    @Override
    public void onStopped() {
        super.onStopped();
    }



    private static final String TAG = MainWorker.class.getSimpleName();

}