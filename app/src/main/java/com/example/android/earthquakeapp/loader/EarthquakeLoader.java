package com.example.android.earthquakeapp.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.CursorLoader;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.earthquakeapp.provider.db.DbUtils;

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
            final CursorLoader cursorLoader = DbUtils.getEarthquake(getContext());



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