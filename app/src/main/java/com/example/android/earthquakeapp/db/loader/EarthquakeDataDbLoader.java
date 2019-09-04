package com.example.android.earthquakeapp.db.loader;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.earthquakeapp.EarthquakeCallback;
import com.example.android.earthquakeapp.bean.Earthquake;
import com.example.android.earthquakeapp.db.connection.HttpConnection;
import com.example.android.earthquakeapp.db.geojson.JsonUtils;
import com.example.android.earthquakeapp.db.room.DbUtils;

import java.io.Closeable;
import java.util.List;

public class EarthquakeDataDbLoader implements Closeable {
    public static EarthquakeDataDbLoader getInstance() {
        return ourInstance;
    }

    public String status() {
        return "Is alive? " + threadMain.isAlive();
    }

    public void loadData(@NonNull Application context, @Nullable EarthquakeCallback callback) {
        final Handler handler = new Handler(threadMain.getLooper());
        handler.post(() -> loadDataInner(context, callback));
    }

    private void loadDataInner(@NonNull Application context, @Nullable EarthquakeCallback callback) {
        final String[] urls = LoaderUtils.getQueryUrl(context);
        final int rows = DbUtils.deleteAllQuake(context);
        Log.d(TAG, "Deleted rows " + rows);
        for (String url : urls) {
            Log.d(TAG, "Url: " + url);
        }

        final Handler[] handlers = new Handler[THREADS.length];
        final boolean[] connectionEnded = new boolean[urls.length];

        for (int index = 0; index < THREADS.length; index++) {
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
                    Log.d(TAG, "Handler[" + indexHandler + "] manage connection " + url);
                    connection = new HttpConnection(url);
                    final List<Earthquake> list = JsonUtils.convertFromJSON(context, connection.makeHttpGetRequest());
                    if (list != null && !list.isEmpty()) {
                        for (Earthquake quake : list) {
                            quake.setUrlRequest(url);
                        }
                        final ManageInsert manage = new ManageInsert(list, callback, connectionEnded, indexFinal);
                        insert(context, manage);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Problem", e);
                }

            });
        }
    }

    private void insert(@NonNull final Application context, @NonNull final ManageInsert manage) {
        final Handler handler = new Handler(threadMain.getLooper());
        handler.post(() -> {
            Log.d(TAG, "start save list on db " + manage.list.size());
            DbUtils.addEarthquake(context, manage.list);
            Log.d(TAG, "end save list on db " + manage.list.size());
            if (manage.callback != null) {
            }
            if (manage.callback != null) {
                Log.d(TAG, "notify New Data");
                synchronized (manage.connectionEnded) {
                    manage.connectionEnded[manage.index] = true;
                    boolean res = true;
                    int count = 0;
                    for (boolean b : manage.connectionEnded) {
                        res &= b;
                        if (b) {
                            count++;
                        }
                    }
                    manage.callback.notifyNewData(manage.list.size(), count, manage.connectionEnded.length);
                    if (res) {
                        // ALL works finished
                        final int finalCount = DbUtils.getCountEarthquake(context);
                        manage.callback.notifyEarthquakeFinalCount(finalCount);
                    }
                }
            }
        });
    }

    @Override
    public void close() {
        threadMain.quit();
        for (HandlerThread handlerThread : THREADS) {
            if (handlerThread != null && handlerThread.isAlive()) {
                handlerThread.quit();
            }
        }
    }

    private void start() {
        threadMain.start();
        for (int index = 0; index < THREADS.length; index++) {
            THREADS[index].start();
        }

    }


    private EarthquakeDataDbLoader() {
        threadMain = new HandlerThread(TAG + ".Thread_Main");
        for (int index = 0; index < THREADS.length; index++) {
            THREADS[index] = new HandlerThread(TAG + ".Thread_" + index);
        }
        start();
    }

    private class ManageInsert {
        ManageInsert(List<Earthquake> list, EarthquakeCallback callback, boolean[] connectionEnded, int index) {
            this.list = list;
            this.callback = callback;
            this.connectionEnded = connectionEnded;
            this.index = index;
        }

        final List<Earthquake> list;
        final EarthquakeCallback callback;
        final boolean[] connectionEnded;
        final int index;
    }

    private final HandlerThread threadMain;
    private static final int MAX_THREAD = 5;
    private static final HandlerThread[] THREADS = new HandlerThread[MAX_THREAD];
    private static final EarthquakeDataDbLoader ourInstance = new EarthquakeDataDbLoader();

    private static final String TAG = EarthquakeDataDbLoader.class.getSimpleName();
}

