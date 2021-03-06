package com.example.android.earthquakeapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>>, EarthquakeLoader.StartLoading {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        final ListView earthquakeListView = findViewById(R.id.list);
        this.mEmptyList = findViewById(R.id.empty_view);
        this.mProgressBar = findViewById(R.id.loading_indicator);

        this.mAdapter = new EarthquakeAdapter(this, new ArrayList<>());
        earthquakeListView.setAdapter(mAdapter);
        earthquakeListView.setEmptyView(this.mEmptyList);

        earthquakeListView.setOnItemClickListener((parent, view, position, id) -> {
            final Earthquake earthquake = mAdapter.getItem(position);
            if (earthquake.getUrl() != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(earthquake.getUrl()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        if (!isConnected(this)) {
            this.mEmptyList.setText(getString(R.string.no_internet));
            this.mEmptyList.setVisibility(View.VISIBLE);
        } else {
            this.mEmptyList.setText(null);
            this.mEmptyList.setVisibility(View.GONE);

            final Bundle bundle = new Bundle();
            bundle.putString(BASE_URL, BASE_USGS_REQUEST_URL);

            // TODO deprecated
            Log.d(TAG, "LOADER initLoader()");
            getLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, bundle, this);
        }
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
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(final int i, @NonNull final Bundle bundle) {
        Log.d(TAG, "LOADER onCreateLoader()");
        return new EarthquakeLoader(this, getQueryUrl(bundle.getString(BASE_URL)), this);
    }

    @Override
    public void onLoadFinished(@NonNull final Loader<List<Earthquake>> loader, @NonNull final List<Earthquake> earthquakes) {
        Log.d(TAG, "LOADER onLoadFinished()");
        mAdapter.clear();
        this.mEmptyList.setText(null);
        this.mProgressBar.setVisibility(View.GONE);
        if (earthquakes.size() == 0) {
            this.mEmptyList.setText(getString(R.string.no_earthquakes));
        }
        mAdapter.addAll(earthquakes);
    }

    @Override
    public void onLoaderReset(@NonNull final Loader<List<Earthquake>> loader) {
        Log.d(TAG, "LOADER onLoaderReset()");
        mAdapter.clear();
    }

    @Override
    public void startLoading() {
        this.mEmptyList.setText(getString(R.string.loading_data));
        this.mEmptyList.setVisibility(View.VISIBLE);
        this.mProgressBar.setVisibility(View.VISIBLE);
    }

    public static boolean isConnected(@NonNull final Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    @Nullable
    @CheckResult
    private String getQueryUrl(@Nullable final String baseUriString) {
        if (baseUriString == null) return null;
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String minMagnitude = sharedPrefs.getString(getString(R.string.settings_min_magnitude_key), getString(R.string.settings_min_magnitude_default));
        final String orderBy = sharedPrefs.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));
        final Uri baseUri = Uri.parse(baseUriString);
        final Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);
        return uriBuilder.toString();
    }

    private EarthquakeAdapter mAdapter;
    private TextView mEmptyList;
    private ProgressBar mProgressBar;

    private static final String BASE_USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    private static final String BASE_URL = "BASE_URL";
    private static final int EARTHQUAKE_LOADER_ID = 1;

    private static final String TAG = EarthquakeActivity.class.getSimpleName();


}
