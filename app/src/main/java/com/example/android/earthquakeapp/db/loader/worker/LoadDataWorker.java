package com.example.android.earthquakeapp.db.loader.worker;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.android.earthquakeapp.bean.Earthquake;
import com.example.android.earthquakeapp.db.connection.HttpConnection;
import com.example.android.earthquakeapp.db.geojson.JsonUtils;
import com.example.android.earthquakeapp.db.room.DbUtils;

import java.util.List;

public class LoadDataWorker extends Worker {

    public LoadDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        final String url = getInputData().getString(EarthquakeDataDbLoader.URL);
        final int index = getInputData().getInt(EarthquakeDataDbLoader.INDEX, -1);
        Log.w(TAG,"doWork of LoadDataWorker # " + index);

        int numberOfQuake = 0;
        try {
            Log.d(TAG, "LoadDataWorker manage connection " + url);
            connection = new HttpConnection(url);
            final List<Earthquake> list = JsonUtils.convertFromJSON(getApplicationContext(), connection.makeHttpGetRequest());
            if (list != null && !list.isEmpty()) {
                for (Earthquake quake : list) {
                    quake.setUrlRequest(url);
                }
                numberOfQuake = list.size();
                final ManageInsert manage = new ManageInsert(list);
                insert(getApplicationContext(), manage);
            }
        } catch (Exception e) {
            Log.e(TAG, "Problem", e);
            final Data outputData = new Data.Builder()//TODO
                    .putString(EarthquakeDataDbLoader.EXCEPTION_CLASS,e.getClass().toString())
                    .putString(EarthquakeDataDbLoader.EXCEPTION_MESSAGE,e.getMessage())
                    .build();
            return Result.failure(outputData);
        }
        Log.d(TAG, "LoadDataWorker " + index + " finish");

        final Data outputData = new Data.Builder()
                .putInt(EarthquakeDataDbLoader.NUMBER_QUAKE_INSERTED,numberOfQuake)
                .build();

        return Result.success(outputData);
    }

    @Override
    public void onStopped() {
        connection.close();
        super.onStopped();
    }


    private void insert(@NonNull final Context context, @NonNull final ManageInsert manage) {
        Log.d(TAG, "start save list on db " + manage.list.size());
        DbUtils.addEarthquake((Application) context, manage.list);
        Log.d(TAG, "end save list on db " + manage.list.size());
    }

    private class ManageInsert {
        ManageInsert(List<Earthquake> list) {
            this.list = list;
        }

        final List<Earthquake> list;
    }

    private HttpConnection connection;

    private static final String TAG = LoadDataWorker.class.getSimpleName();
}

