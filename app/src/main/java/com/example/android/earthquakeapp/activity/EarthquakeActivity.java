package com.example.android.earthquakeapp.activity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.earthquakeapp.Configurations;
import com.example.android.earthquakeapp.EarthquakeCallback;
import com.example.android.earthquakeapp.R;
import com.example.android.earthquakeapp.bean.Earthquake;
import com.example.android.earthquakeapp.provider.loader.EarthquakeAdapter;
import com.example.android.earthquakeapp.provider.db.DbUtils;
import com.example.android.earthquakeapp.provider.db.EarthquakeDataDbLoader;

/*
TODO Programmable Web API Directory: http://www.programmableweb.com/apis/directory
TODO Google APIs Explorer: https://developers.google.com/apis-explorer/#p/
TODO Data.gov: http://data.gov
TODO Tips for building a great UI https://developer.android.com/guide/topics/ui
TODO USGS Earthquake real time feeds and notifications: http://earthquake.usgs.gov/earthquakes/feed/v1.0/index.php
TODO USGS Real-Time Earthquake Data in Spreadsheet Format: http://earthquake.usgs.gov/earthquakes/feed/v1.0/csv.php

TODO https://earthquake.usgs.gov/data/
TODO APIs spec https://earthquake.usgs.gov/fdsnws/event/1/#format-geojson
*/
// TODO make link to https://earthquake.usgs.gov/earthquakes/map/
public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, EarthquakeCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        final ListView earthquakeListView = findViewById(R.id.list);
        this.mEmptyList = findViewById(R.id.empty_view);
        this.mProgressBar = findViewById(R.id.loading_indicator);


        if (checkConnection()) {
            if (Configurations.SETTINGS_CHANGED) {
                Configurations.SETTINGS_CHANGED = false;
                EarthquakeDataDbLoader.getInstance().loadData(this, this);
            }
        }

        // Find a reference to the {@link ListView} in the layout

        this.mAdapter = new EarthquakeAdapter(this, null);
        earthquakeListView.setAdapter(mAdapter);
        earthquakeListView.setEmptyView(this.mEmptyList);

        earthquakeListView.setOnItemClickListener((parent, view, position, id) -> {
            // listener on row, not into a single piece of it
            final Earthquake earthquake = DbUtils.cursor2Earthquake((Cursor) mAdapter.getItem(position));
            if (earthquake != null) {
                Toast.makeText(this, getString(R.string.info_quake, earthquake.getId(), earthquake.getUrlRequest()), Toast.LENGTH_LONG).show();
            }

        });

        getLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConnection();
    }

    private boolean checkConnection() {
        boolean isConnected = UiUtils.isConnected(this);
        if (!isConnected) {
            this.mEmptyList.setText(getString(R.string.no_internet));
            this.mEmptyList.setVisibility(View.VISIBLE);
        } else {
            this.mEmptyList.setText(null);
            this.mEmptyList.setVisibility(View.GONE);
        }
        return isConnected;
    }

    /**
     * method for create option menu for settings
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * method to listen click on option menu
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_all_map:
                final Intent intent = new Intent(this, MapsActivity.class);
                intent.setData(MapsActivity.FROM_DB);
                startActivity(intent);
                return true;
            case R.id.action_reload:
                if (checkConnection()) {
                    EarthquakeDataDbLoader.getInstance().loadData(this, this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return DbUtils.getEarthquake(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        invalidateOptionsMenu();
    }


    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    @Override
    public void notifyNewData() {
        runOnUiThread(() -> {
            final Loader loader = getLoaderManager().getLoader(EARTHQUAKE_LOADER_ID);
            if (loader != null && loader.isStarted()) {
                Log.w(TAG,"forceLoad");
                loader.forceLoad();
            }
            else{
                Log.e(TAG,"loader not initiated?");
                getLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this);
            }
        });
    }

    @Override
    public void notifyEarthquakeFinalCount(int numEarthquake) {
        Log.w(TAG, getString(R.string.number_of_earthquakes, numEarthquake));
        runOnUiThread(() -> Toast.makeText(this, getString(R.string.number_of_earthquakes, numEarthquake), Toast.LENGTH_SHORT).show());
    }


    private EarthquakeAdapter mAdapter;
    private TextView mEmptyList;
    private ProgressBar mProgressBar;//TODO gestire progressbar incrementale


    private static final String BASE_URL = "BASE_URL";
    private static final int EARTHQUAKE_LOADER_ID = 1;


    private static final String TAG = EarthquakeActivity.class.getSimpleName();


}
