package com.example.android.earthquakeapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.earthquakeapp.connection.HttpConnection;
import com.example.android.earthquakeapp.geojson.JsonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * must be external class
 * Object returned from onCreateLoader must not be a non-static inner member class
 * <p>
 * Loads a list of earthquakes by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    EarthquakeLoader(@NonNull final Context context, @Nullable final String url, TextView loadingView) {
        super(context);
        this.mUrl = url;
        this.mLoading = loadingView;
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG,"LOADER onStartLoading()");
        this.mLoading.setText(getContext().getString(R.string.loading_data));
        // is a required step to actually trigger the loadInBackground() method to execute
        forceLoad();
    }


    @Override
    public List<Earthquake> loadInBackground() {
        Log.d(TAG,"LOADER loadInBackground()");
        try {
            if (TextUtils.isEmpty(this.mUrl)) {
                return new ArrayList<>();
            }
            final HttpConnection connection = new HttpConnection(this.mUrl);
            return JsonUtils.convertFromJSON(getContext(), connection.makeHttpGetRequest());
        } catch (Exception e) {
            Log.e(TAG, "Problem", e);
        }
        return new ArrayList<>();
    }

    private final String mUrl;
    private final TextView mLoading;

    private static final String TAG = EarthquakeLoader.class.getSimpleName();
}