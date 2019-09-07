package com.example.android.earthquakeapp.db.loader.worker;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.android.earthquakeapp.db.room.DbUtils;

public class FinalWorker extends Worker {
    public FinalWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        final int finalCount = DbUtils.getCountEarthquake((Application) getApplicationContext());
        final Data outputData = new Data.Builder()
                .putInt(EarthquakeDataDbLoader.NUMBER_QUAKE_INSERTED_FINAL, finalCount)
                .build();
        return Result.success(outputData);
    }


    private static final String TAG = FinalWorker.class.getSimpleName();
}
