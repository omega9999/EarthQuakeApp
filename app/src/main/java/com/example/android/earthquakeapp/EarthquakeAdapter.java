package com.example.android.earthquakeapp;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    EarthquakeAdapter(@NonNull final Activity activity, @NonNull final ArrayList<Earthquake> earthquakes) {
        super(activity, 0, earthquakes);
        this.mLayoutInflater = activity.getLayoutInflater();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
        View root = convertView;
        if (root == null) {
            root = this.mLayoutInflater.inflate(R.layout.earthquake_item, parent, false);
        }
        final Earthquake earthquake = getItem(position);
        if (earthquake != null) {

            final TextView magnitude = root.findViewById(R.id.magnitude);
            final TextView location = root.findViewById(R.id.primary_location);
            final TextView locationOffset = root.findViewById(R.id.location_offset);
            final TextView date = root.findViewById(R.id.date);
            final TextView time = root.findViewById(R.id.time);
            final View web = root.findViewById(R.id.web);
            final View mapSearch = root.findViewById(R.id.map_search);

            if (position % 2 == 0){
                root.setBackgroundResource(R.color.backgroundColorEven);
            }
            else{
                root.setBackgroundResource(R.color.backgroundColorOdd);
            }


            if (earthquake.getUrl() != null) {
                web.setVisibility(View.VISIBLE);

                web.setOnClickListener(v -> {
                    Toast.makeText(getContext(), earthquake.getPrimaryLocation(), Toast.LENGTH_SHORT).show();
                    /*
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(earthquake.getUrl()));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                    */
                });
            }
            if (earthquake.isCoordinates()){
                mapSearch.setVisibility(View.VISIBLE);
            }


            GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();
            int magnitudeColor = getMagnitudeColor(earthquake.getMagnitude());
            magnitudeCircle.setColor(magnitudeColor);

            magnitude.setText(DECIMAL_FORMAT.format(earthquake.getMagnitude()));
            location.setText(earthquake.getPrimaryLocation());
            locationOffset.setText(earthquake.getLocationOffset());
            date.setText(mDateFormat.format(earthquake.getDate()));
            time.setText(mTimeFormat.format(earthquake.getDate()));
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
    private final SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat mTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    private final LayoutInflater mLayoutInflater;

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0");
}