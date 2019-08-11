package com.example.android.earthquakeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

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

        // Create a fake list of earthquake locations.
        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        earthquakes.add(new Earthquake(7.2,"San Francisco","2016-02-02"));
        earthquakes.add(new Earthquake(6.1,"London","2015-07-20"));
        earthquakes.add(new Earthquake(3.9,"Tokyo","2014-11-10"));
        earthquakes.add(new Earthquake(5.4,"Mexico City","2014-05-03"));
        earthquakes.add(new Earthquake(2.8,"Moscow","2013-01-31"));
        earthquakes.add(new Earthquake(4.9,"Rio de Janeiro","2012-08-19"));
        earthquakes.add(new Earthquake(1.6,"Paris","2011-10-30"));

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        EarthquakeAdapter adapter = new EarthquakeAdapter(this , earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);
    }

    private class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

        public EarthquakeAdapter(Context context, ArrayList<Earthquake> earthquakes) {
            super(context,0,earthquakes);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
            View root = convertView;
            if (root == null){
                root = getLayoutInflater().inflate(R.layout.earthquake_item, parent, false);
            }
            Earthquake earthquake = getItem(position);
            if (earthquake != null){
                TextView magnitude = root.findViewById(R.id.magnitude);
                TextView city = root.findViewById(R.id.location);
                TextView date = root.findViewById(R.id.date);

                magnitude.setText(DECIMAL_FORMAT.format(earthquake.getMagnitude()));
                city.setText(earthquake.getLocation());
                date.setText(dateFormat.format(earthquake.getDate()));
            }
            return root;
        }
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy", Locale.getDefault());
    }

    private static final String TAG = EarthquakeActivity.class.getSimpleName();
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0");


}
