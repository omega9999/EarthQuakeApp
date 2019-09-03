package com.example.android.earthquakeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.android.earthquakeapp.Configurations;
import com.example.android.earthquakeapp.EarthquakeCallback;
import com.example.android.earthquakeapp.R;
import com.example.android.earthquakeapp.bean.Earthquake;
import com.example.android.earthquakeapp.db.loader.EarthquakeAdapter;
import com.example.android.earthquakeapp.db.room.EarthquakeViewModel;
import com.example.android.earthquakeapp.db.loader.EarthquakeDataDbLoader;

import java.util.List;

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
public class EarthquakeActivity extends AppCompatActivity implements EarthquakeCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        final EmptyRecyclerView earthquakeListView = findViewById(R.id.list);
        this.mEmptyList = findViewById(R.id.empty_view);
        this.mProgressBar = findViewById(R.id.loading_indicator);

        if (checkConnection()) {
            if (Configurations.SETTINGS_CHANGED) {
                Configurations.SETTINGS_CHANGED = false;
                EarthquakeDataDbLoader.getInstance().loadData(this, this);
            }
        }

        // Find a reference to the {@link ListView} in the layout

        this.mAdapter = new EarthquakeAdapter(this);
        earthquakeListView.setAdapter(mAdapter);
        earthquakeListView.setLayoutManager(new LinearLayoutManager(this));
        earthquakeListView.setEmptyView(this.mEmptyList);


        this.mAdapter.setOnItemClickListener((parent, view, position, id) -> {
            // listener on row, not into a single piece of it
            final Earthquake earthquake = mAdapter.getItem(position);
            if (earthquake != null) {
                Toast.makeText(this, getString(R.string.info_quake, earthquake.getId(), earthquake.getUrlRequest()), Toast.LENGTH_LONG).show();
            }
        });
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


    @Override
    public void notifyNewData(final int earthquakeAdded, int numJobCompleted, int numJobTotal) {
        runOnUiThread(() -> {
            invalidateOptionsMenu();
            final String str = getString(R.string.number_of_earthquakes_added, earthquakeAdded, numJobCompleted, numJobTotal);
            Log.w(TAG, str);
            runOnUiThread(() -> Toast.makeText(this, str, Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    public void notifyEarthquakeFinalCount(int numEarthquake) {
        invalidateOptionsMenu();
        final String str = getString(R.string.number_of_earthquakes, numEarthquake);
        Log.w(TAG, str);
        runOnUiThread(() -> Toast.makeText(this, str, Toast.LENGTH_SHORT).show());
    }


    private EarthquakeAdapter mAdapter;
    private TextView mEmptyList;
    private ProgressBar mProgressBar;//TODO gestire progressbar incrementale



    private static final String TAG = EarthquakeActivity.class.getSimpleName();


}
