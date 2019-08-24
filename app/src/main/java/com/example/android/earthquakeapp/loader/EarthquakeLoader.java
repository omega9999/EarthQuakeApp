package com.example.android.earthquakeapp.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.earthquakeapp.bean.Earthquake;
import com.example.android.earthquakeapp.bean.ErrorList;
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

    public EarthquakeLoader(@NonNull final Context context, @Nullable final String url, @Nullable final StartLoading startLoadingCallBack) {
        super(context);
        Log.i(TAG, String.format("URL GET: %1$s", url));
        this.mUrl = url;
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
    public List<Earthquake> loadInBackground() {
        Log.d(TAG, "LOADER loadInBackground()");

        try {
            if (TextUtils.isEmpty(this.mUrl)) {
                return new ArrayList<>();
            }
            this.checkLoad = false;
            final HttpConnection connection = new HttpConnection(this.mUrl);
            final List<Earthquake> list = JsonUtils.convertFromJSON(getContext(), connection.makeHttpGetRequest());
            this.checkLoad = true;
            return list;
        } catch (Exception e) {
            Log.e(TAG, "Problem", e);
            return (new ErrorList<Earthquake>()).setException(e);
        }
    }

    public boolean isCheckLoad(){
        boolean res = this.checkLoad;
        if (res){
            this.checkLoad = false;
        }
        return res;
    }

    public interface StartLoading {
        void startLoading();
    }



    private boolean checkLoad = false;


    private final String mUrl;
    private final StartLoading mStartLoadingCallBack;

    private static final String TAG = EarthquakeLoader.class.getSimpleName();
}