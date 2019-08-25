package com.example.android.earthquakeapp.activity;

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
import android.widget.Toast;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.earthquakeapp.Configurations;
import com.example.android.earthquakeapp.R;
import com.example.android.earthquakeapp.bean.Earthquake;
import com.example.android.earthquakeapp.bean.EarthquakeList;
import com.example.android.earthquakeapp.bean.ErrorList;
import com.example.android.earthquakeapp.loader.EarthquakeAdapter;
import com.example.android.earthquakeapp.loader.EarthquakeLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

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

        this.mAdapter = new EarthquakeAdapter(this, new EarthquakeList());
        earthquakeListView.setAdapter(mAdapter);
        earthquakeListView.setEmptyView(this.mEmptyList);

        earthquakeListView.setOnItemClickListener((parent, view, position, id) -> {
            // listener on row, not into a single piece of it
            final Earthquake earthquake = mAdapter.getItem(position);

            if (earthquake.getUrl() != null) {
                Log.w(TAG, String.format("View id %1$s, id %2$s, id target %3$s", view.getId(), id, R.id.web));
            }
        });

        final Bundle bundle = new Bundle();
        bundle.putString(BASE_URL, BASE_USGS_REQUEST_URL);

        if (checkConnection()) {
            // TODO deprecated
            Log.d(TAG, "LOADER initLoader()");
            getLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, bundle, this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConnection();
    }

    private boolean checkConnection() {
        boolean isConnected = isConnected(this);
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
                final ArrayList<Earthquake> list = new ArrayList<>(mAdapter.getEarthquakes());
                final int maxCapacity = 2000;
                final ArrayList<Earthquake> tmp = new ArrayList<>(list.subList(0, Math.min(list.size(), maxCapacity)));
                if (list.size() > maxCapacity) {
                    Toast.makeText(this, getString(R.string.map_max_capacity, list.size(), maxCapacity), Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG,"insert earthquakes into intent : " + tmp.size());
                intent.putParcelableArrayListExtra(MapsActivity.EARTHQUAKES, tmp);
                // https://developer.android.com/reference/android/os/TransactionTooLargeException.html
                Log.d(TAG,"start activity MapsActivity");
                startActivity(intent);
                return true;
            case R.id.action_reload:
                final Bundle bundle = new Bundle();
                bundle.putString(BASE_URL, BASE_USGS_REQUEST_URL);

                if (checkConnection()) {
                    // TODO deprecated
                    Log.d(TAG, "LOADER initLoader()");
                    getLoaderManager().restartLoader(EARTHQUAKE_LOADER_ID, bundle, this);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            if (earthquakes instanceof ErrorList) {
                this.mEmptyList.setText(getString(R.string.error_earthquakes));
            } else {
                this.mEmptyList.setText(getString(R.string.no_earthquakes));
            }
        } else {
            if (Configurations.IS_LOADED) {
                Toast.makeText(this, getString(R.string.number_of_earthquakes, earthquakes.size()), Toast.LENGTH_SHORT).show();
                Configurations.IS_LOADED = false;
            }
            mAdapter.addAll(earthquakes);
        }
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
        final String limitRow = sharedPrefs.getString(getString(R.string.settings_limit_row_key), getString(R.string.settings_limit_row_default));
        final int startTimeLimit = Integer.valueOf(sharedPrefs.getString(getString(R.string.settings_start_time_limit_key), getString(R.string.settings_start_time_limit_default)));
        final Uri baseUri = Uri.parse(baseUriString);
        final Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("format", "geojson");

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1 * startTimeLimit);

        uriBuilder.appendQueryParameter("limit", limitRow);
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);
        //uriBuilder.appendQueryParameter("offset", "1");
        //uriBuilder.appendQueryParameter("endtime", "2019-08-16"); // default present
        uriBuilder.appendQueryParameter("starttime", DATE_FORMAT.format(calendar.getTime())); // default now - 30 days
        return uriBuilder.toString();
    }

    private EarthquakeAdapter mAdapter;
    private TextView mEmptyList;
    private ProgressBar mProgressBar;

    private static final String BASE_USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private static final String BASE_USGS_COUNT_URL = "https://earthquake.usgs.gov/fdsnws/event/1/count";


    private static final String BASE_URL = "BASE_URL";
    private static final int EARTHQUAKE_LOADER_ID = 1;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private static final String TAG = EarthquakeActivity.class.getSimpleName();


}
