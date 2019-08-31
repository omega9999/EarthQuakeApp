package com.example.android.earthquakeapp.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.CursorLoader;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.earthquakeapp.R;
import com.example.android.earthquakeapp.bean.Earthquake;
import com.example.android.earthquakeapp.provider.db.DbUtils;
import com.example.android.earthquakeapp.provider.connection.HttpConnection;
import com.example.android.earthquakeapp.provider.geojson.JsonUtils;

import java.util.List;

/**
 * must be external class
 * Object returned from onCreateLoader must not be a non-static inner member class
 * <p>
 * Loads a list of earthquakes by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class EarthquakeLoader extends AsyncTaskLoader<CursorLoader> {

    public EarthquakeLoader(@NonNull final Context context, @Nullable final String[] urls, @Nullable final StartLoading startLoadingCallBack) {
        super(context);
        //Log.i(TAG, String.format("URL GET: %1$s", url));
        this.mUrls = urls;
        this.mStartLoadingCallBack = startLoadingCallBack;
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "LOADER onStartLoading()");

        if (this.mStartLoadingCallBack != null) {
            this.mStartLoadingCallBack.startLoading();
        }

        // is a required step to actually trigger the loadInBackground() method to execute
        forceLoad();
    }


    @Override
    public CursorLoader loadInBackground() {
        Log.d(TAG, "LOADER loadInBackground()");

        try {
            final int rows = DbUtils.deleteAllQuake(getContext());
            final CursorLoader cursorLoader = DbUtils.getEarthquake(getContext());

            final int MAX_THREAD = 10;
            final HandlerThread[] handlerThreads = new HandlerThread[Math.min(MAX_THREAD,this.mUrls.length)];
            final MyHandler[] handlers = new MyHandler[handlerThreads.length];
            final boolean[] connectionEnded = new boolean[this.mUrls.length];

            for (int index = 0; index < handlerThreads.length; index++){
                handlerThreads[index] = new HandlerThread(TAG + ".Thread_" + index);
                handlerThreads[index].start();
                handlers[index] = new MyHandler(handlerThreads[index].getLooper());
            }


            for (int index = 0; index < this.mUrls.length; index++){
                final int indexFinal = index;
                String url = this.mUrls[index];
                if (TextUtils.isEmpty(url)) {
                    continue;
                }

                final int indexHandler = index % handlers.length;
                handlers[indexHandler].post(() -> {
                    final HttpConnection connection;
                    try {
                        connection = new HttpConnection(url);
                        final List<Earthquake> list = JsonUtils.convertFromJSON(getContext(), connection.makeHttpGetRequest());
                        if (list != null && !list.isEmpty()){
                            Log.wtf(TAG,"save list on db");
                            DbUtils.addEarthquake(getContext(), list);
                            cursorLoader.forceLoad();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Problem", e);
                    }
                    connectionEnded[indexFinal] = true;
                    synchronized (connectionEnded){
                        boolean res = true;
                        for (boolean b : connectionEnded) {
                            res &= b;
                        }
                        if (res){
                            // ALL works finished
                            for (HandlerThread handlerThread : handlerThreads) {
                                handlerThread.quit();
                            }
                            new Handler(Looper.getMainLooper()).post(()->Toast.makeText(getContext(), getContext().getString(R.string.number_of_earthquakes, "??"), Toast.LENGTH_SHORT).show());
                        }
                    }
                });
            }
            return cursorLoader;
        } catch (Exception e) {
            Log.e(TAG, "Problem", e);
            return null;//(new ErrorList<Earthquake>()).setException(e);
        }
    }

    private class MyHandler extends Handler{

        MyHandler(Looper looper) {
            super(looper);
        }
    }

    public interface StartLoading {
        void startLoading();
    }

    private final String[] mUrls;
    private final StartLoading mStartLoadingCallBack;

    private static final String TAG = EarthquakeLoader.class.getSimpleName();
}