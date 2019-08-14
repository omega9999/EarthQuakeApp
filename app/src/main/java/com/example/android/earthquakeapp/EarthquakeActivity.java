package com.example.android.earthquakeapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/*
TODO Programmable Web API Directory: http://www.programmableweb.com/apis/directory
TODO Google APIs Explorer: https://developers.google.com/apis-explorer/#p/
TODO Data.gov: http://data.gov
TODO Tips for building a great UI https://developer.android.com/guide/topics/ui
TODO USGS Earthquake real time feeds and notifications: http://earthquake.usgs.gov/earthquakes/feed/v1.0/index.php
TODO USGS Real-Time Earthquake Data in Spreadsheet Format: http://earthquake.usgs.gov/earthquakes/feed/v1.0/csv.php
*/
public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = findViewById(R.id.list);
        this.mAdapter = new EarthquakeAdapter(this, new ArrayList<>());
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener((parent, view, position, id) -> {
            final Earthquake earthquake = mAdapter.getItem(position);
            if (earthquake.getUrl() != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(earthquake.getUrl()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        final Bundle bundle = new Bundle();
        bundle.putString(URL, USGS_REQUEST_URL);

        // TODO deprecated
        Log.d(TAG,"LOADER initLoader()");
        getLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, bundle, this);
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(final int i, @NonNull final Bundle bundle) {
        Log.d(TAG,"LOADER onCreateLoader()");
        return new EarthquakeLoader(this, bundle.getString(URL));
    }

    @Override
    public void onLoadFinished(@NonNull final Loader<List<Earthquake>> loader, @NonNull final List<Earthquake> earthquakes) {
        Log.d(TAG,"LOADER onLoadFinished()");
        mAdapter.clear();
        mAdapter.addAll(earthquakes);
    }

    @Override
    public void onLoaderReset(@NonNull final Loader<List<Earthquake>> loader) {
        Log.d(TAG,"LOADER onLoaderReset()");
        mAdapter.clear();
    }


    private EarthquakeAdapter mAdapter;

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";

    private static final String URL = "URL";
    private static final int EARTHQUAKE_LOADER_ID = 1;

    private static final String TAG = EarthquakeActivity.class.getSimpleName();


}
