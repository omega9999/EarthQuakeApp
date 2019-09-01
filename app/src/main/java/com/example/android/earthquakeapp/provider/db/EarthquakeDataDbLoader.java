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

    public String status(){
        return "Is alive? " + thread.isAlive();
    }

    public void loadData(@NonNull Context context, @Nullable EarthquakeCallback callback) {
        final Handler handler = new Handler(thread.getLooper());
        handler.post(() -> loadDataInner(context, callback));
    }

    private void loadDataInner(@NonNull Context context, @Nullable EarthquakeCallback callback) {
        final String[] urls = DbUtils.getQueryUrl(context);
        final int rows = DbUtils.deleteAllQuake(context);

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
                    connection = new HttpConnection(url);
                    final List<Earthquake> list = JsonUtils.convertFromJSON(context, connection.makeHttpGetRequest());
                    if (list != null && !list.isEmpty()) {
                        Log.d(TAG, "save list on db " + list.size());
                        DbUtils.addEarthquake(context, list);
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
                            callback.notifyEarthquake(count);
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void finalize() throws Throwable {
        thread.quit();
        for (HandlerThread handlerThread : THREADS) {
            if (handlerThread != null && handlerThread.isAlive()) {
                handlerThread.quit();
            }
        }
        super.finalize();
    }


    private EarthquakeDataDbLoader() {
        thread = new HandlerThread(TAG + ".Thread_Main");
        thread.start();
        for (int index = 0; index < THREADS.length; index++) {
            THREADS[index] = new HandlerThread(TAG + ".Thread_" + index);
            THREADS[index].start();

        }
    }

    private final HandlerThread thread;
    private static final int MAX_THREAD = 10;
    private static final HandlerThread[] THREADS = new HandlerThread[MAX_THREAD];
    private static final EarthquakeDataDbLoader ourInstance = new EarthquakeDataDbLoader();

    private static final String TAG = EarthquakeDataDbLoader.class.getSimpleName();
}

