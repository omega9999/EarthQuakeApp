package com.example.android.earthquakeapp;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.android.earthquakeapp.geojson.Utils;

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
        final ArrayList<Earthquake> earthquakes = Utils.convertFromJSONExample(this);

        // Find a reference to the {@link ListView} in the layout
        final ListView earthquakeListView = findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        final EarthquakeAdapter adapter = new EarthquakeAdapter(this, earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

        EarthquakeAdapter(Context context, ArrayList<Earthquake> earthquakes) {
            super(context, 0, earthquakes);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
            View root = convertView;
            if (root == null) {
                root = getLayoutInflater().inflate(R.layout.earthquake_item, parent, false);
            }
            final Earthquake earthquake = getItem(position);
            if (earthquake != null) {

                final TextView magnitude = root.findViewById(R.id.magnitude);
                final TextView location = root.findViewById(R.id.primary_location);
                final TextView locationOffset = root.findViewById(R.id.location_offset);
                final TextView date = root.findViewById(R.id.date);

                GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();
                int magnitudeColor = getMagnitudeColor(earthquake.getMagnitude());
                magnitudeCircle.setColor(magnitudeColor);

                magnitude.setText(DECIMAL_FORMAT.format(earthquake.getMagnitude()));
                location.setText(earthquake.getPrimaryLocation());
                locationOffset.setText(earthquake.getLocationOffset());
                date.setText(dateFormat.format(earthquake.getDate()));
            }
            return root;
        }

        private int getMagnitudeColor(final double magnitude) {
            int magnitudeColorResourceId = R.color.magnitude1;
            int magnitudeFloor = (int) Math.floor(magnitude);
            if (magnitudeFloor < 0) {
                magnitudeFloor = 0;
            } else if (magnitudeFloor > 10) {
                magnitudeFloor = 10;
            }
            switch (magnitudeFloor) {
                case 0:
                case 1:
                    magnitudeColorResourceId = R.color.magnitude1;
                    break;
                case 2:
                    magnitudeColorResourceId = R.color.magnitude2;
                    break;
                case 3:
                    magnitudeColorResourceId = R.color.magnitude3;
                    break;
                case 4:
                    magnitudeColorResourceId = R.color.magnitude4;
                    break;
                case 5:
                    magnitudeColorResourceId = R.color.magnitude5;
                    break;
                case 6:
                    magnitudeColorResourceId = R.color.magnitude6;
                    break;
                case 7:
                    magnitudeColorResourceId = R.color.magnitude7;
                    break;
                case 8:
                    magnitudeColorResourceId = R.color.magnitude8;
                    break;
                case 9:
                    magnitudeColorResourceId = R.color.magnitude9;
                    break;
                case 10:
                    magnitudeColorResourceId = R.color.magnitude10plus;
                    break;
            }
            return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
        }

        //TODO see https://developer.android.com/reference/java/text/SimpleDateFormat.html
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
    }


    private static final String TAG = EarthquakeActivity.class.getSimpleName();
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0");


}
