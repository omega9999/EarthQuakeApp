package com.example.android.earthquakeapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.earthquakeapp.connection.HttpConnection;
import com.example.android.earthquakeapp.geojson.JsonUtils;

import java.util.ArrayList;

/*
TODO Programmable Web API Directory: http://www.programmableweb.com/apis/directory
TODO Google APIs Explorer: https://developers.google.com/apis-explorer/#p/
TODO Data.gov: http://data.gov
TODO Tips for building a great UI https://developer.android.com/guide/topics/ui
TODO USGS Earthquake real time feeds and notifications: http://earthquake.usgs.gov/earthquakes/feed/v1.0/index.php
TODO USGS Real-Time Earthquake Data in Spreadsheet Format: http://earthquake.usgs.gov/earthquakes/feed/v1.0/csv.php
*/
public class EarthquakeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        earthquakeListView = findViewById(R.id.list);
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
        final EarthquakeAsyncTask task = new EarthquakeAsyncTask(this);
        task.execute(USGS_REQUEST_URL);
        // Create a new {@link ArrayAdapter} of earthquakes

    }

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, ArrayList<Earthquake>> {

        EarthquakeAsyncTask(@NonNull final Activity activity) {
            this.mActivity = activity;
        }

        @Override
        protected ArrayList<Earthquake> doInBackground(String... urls) {
            try {
                if (urls.length < 1 || urls[0] == null) {
                    return new ArrayList<>();
                }
                final HttpConnection connection = new HttpConnection(urls[0]);
                return JsonUtils.convertFromJSON(this.mActivity, connection.makeHttpGetRequest());
            } catch (Exception e) {
                Log.e(TAG, "Problem", e);
            }
            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(ArrayList<Earthquake> earthquakes) {
            mAdapter.clear();
            mAdapter.addAll(earthquakes);
        }

        private final Activity mActivity;
    }


    private ListView earthquakeListView;
    private EarthquakeAdapter mAdapter;

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";

    private static final String TAG = EarthquakeActivity.class.getSimpleName();


}
