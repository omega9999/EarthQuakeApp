package com.example.android.earthquakeapp.provider.db;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.earthquakeapp.EarthquakeCallback;
import com.example.android.earthquakeapp.bean.Earthquake;
import com.example.android.earthquakeapp.provider.connection.HttpConnection;
import com.example.android.earthquakeapp.provider.geojson.JsonUtils;

import java.util.List;

public class EarthquakeDataDbLoader {
    public static EarthquakeDataDbLoader getInstance() {
        return ourInstance;
    }

    public String status() {
        return "Is alive? " + threadMain.isAlive();
    }

    public void loadData(@NonNull Context context, @Nullable EarthquakeCallback callback) {
        final Handler handler = new Handler(threadMain.getLooper());
        handler.post(() -> loadDataInner(context, callback));
    }

    private void loadDataInner(@NonNull Context context, @Nullable EarthquakeCallback callback) {
        final String[] urls = DbUtils.getQueryUrl(context);
        final int rows = DbUtils.deleteAllQuake(context);
        Log.d(TAG,"Deleted rows " + rows);
        for (String url:urls             ) {
            Log.d(TAG,"Url: " + url);
        }

        final Handler[] handlers = new Handler[THREADS.length];
        final boolean[] connectionEnded = new boolean[urls.length];

        for (int index = 0; index < THREADS.length; index++) {
            THREADS[index] = new HandlerThread(TAG + ".Thread_" + index);
            THREADS[index].start();
            handlers[index] = new Handler(THREADS[index].getLooper());
        }

        for (int index = 0; index < urls.length; index++) {
            final int indexFinal = index;
            final String url = urls[index];
            if (TextUtils.isEmpty(url)) {
                continue;
            }

            final int indexHandler = index % handlers.length;
            handlers[indexHandler].post(() -> {
                final HttpConnection connection;
                try {
                    Log.d(TAG,"Handler["+indexHandler+"] manage connection " + url);
                    connection = new HttpConnection(url);
                    final List<Earthquake> list = JsonUtils.convertFromJSON(context, connection.makeHttpGetRequest());
                    if (list != null && !list.isEmpty()) {
                        for (Earthquake quake : list) {
                            quake.setUrlRequest(url);
                        }
                        insert(context, list, callback);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Problem", e);
                }
                if (callback != null) {
                    connectionEnded[indexFinal] = true;
                    synchronized (connectionEnded) {
                        boolean res = true;
                        for (boolean b : connectionEnded) {
                            res &= b;
                        }
                        if (res) {
                            // ALL works finished
                            final int count = DbUtils.getCountEarthquake(context);
                            callback.notifyEarthquakeFinalCount(count);
                        }
                    }
                }
            });
        }
    }

    private void insert(@NonNull final Context context, @NonNull final List<Earthquake> list,  @Nullable final EarthquakeCallback callback){
        final Handler handler = new Handler(threadMain.getLooper());
        handler.post(() -> {
            Log.d(TAG, "start save list on db " + list.size());
            DbUtils.addEarthquake(context, list);
            Log.d(TAG, "end save list on db " + list.size());
            if (callback != null) {
                Log.d(TAG, "notify New Data");
                callback.notifyNewData();
            }
            else{
                Log.e(TAG, "callback NULL!!!");
            }
        });
    }

    @Override
    protected void finalize() throws Throwable {
        threadMain.quit();
        threadInsert.quit();
        for (HandlerThread handlerThread : THREADS) {
            if (handlerThread != null && handlerThread.isAlive()) {
                handlerThread.quit();
            }
        }
        super.finalize();
    }


    private EarthquakeDataDbLoader() {
        threadMain = new HandlerThread(TAG + ".Thread_Main");
        threadMain.start();
        threadInsert = new HandlerThread(TAG + ".Thread_Insert");
        threadInsert.start();
        for (int index = 0; index < THREADS.length; index++) {
            THREADS[index] = new HandlerThread(TAG + ".Thread_" + index);
            THREADS[index].start();

        }
    }

    private final HandlerThread threadMain;
    private final HandlerThread threadInsert;
    private static final int MAX_THREAD = 4;
    private static final HandlerThread[] THREADS = new HandlerThread[MAX_THREAD];
    private static final EarthquakeDataDbLoader ourInstance = new EarthquakeDataDbLoader();

    private static final String TAG = EarthquakeDataDbLoader.class.getSimpleName();
}

