package com.example.android.earthquakeapp.loader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.android.earthquakeapp.R;
import com.example.android.earthquakeapp.activity.MapsActivity;
import com.example.android.earthquakeapp.activity.UiUtils;
import com.example.android.earthquakeapp.activity.WebActivity;
import com.example.android.earthquakeapp.bean.Earthquake;
import com.example.android.earthquakeapp.bean.EarthquakeList;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeAdapter(@NonNull final Context context, @NonNull final EarthquakeList earthquakes) {
        super(context, 0, earthquakes);
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void addAll(@NonNull Collection<? extends Earthquake> collection) {
        if (collection instanceof EarthquakeList) {
            final EarthquakeList list = (EarthquakeList) collection;
            this.mMinMagnitude = list.getMinMagnitude();
            this.mMaxMagnitude = list.getMaxMagnitude();
            this.mMinTime = list.getMinTime();
            this.mMaxTime = list.getMaxTime();
        }
        super.addAll(collection);
        this.mEarthquakes.addAll(collection);
    }

    public ArrayList<Earthquake> getEarthquakes() {
        return mEarthquakes;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
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

            if (position % 2 == 0) {
                root.setBackgroundResource(R.color.backgroundColorEven);
            } else {
                root.setBackgroundResource(R.color.backgroundColorOdd);
            }


            if (earthquake.getUrl() != null) {
                web.setVisibility(View.VISIBLE);

                web.setOnClickListener(v -> {
                    Toast.makeText(getContext(), earthquake.getPrimaryLocation(), Toast.LENGTH_SHORT).show();
                    final String webOpen = sharedPrefs.getString(getContext().getString(R.string.settings_web_open_key), getContext().getString(R.string.settings_web_open_default));

                    Intent intent = null;
                    if (webOpen != null) {
                        if (webOpen.equals(getContext().getString(R.string.settings_web_open_external_value))) {
                            intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(earthquake.getUrl()));
                        } else if (webOpen.equals(getContext().getString(R.string.settings_web_open_internal_value))) {
                            //TODO implement activity WebActivity
                            intent = new Intent(getContext(), WebActivity.class);
                            intent.setData(Uri.parse(earthquake.getUrl()));
                            intent.putExtra(WebActivity.TITLE,earthquake.getLocationOffset() + " " + earthquake.getPrimaryLocation());
                        }
                    }

                    if (intent != null) {
                        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                            getContext().startActivity(intent);
                        }
                    }
                });
            }
            if (earthquake.isCoordinates()) {
                mapSearch.setVisibility(View.VISIBLE);
                mapSearch.setOnClickListener(v -> {
                    Toast.makeText(getContext(), earthquake.getPrimaryLocation(), Toast.LENGTH_SHORT).show();
                    final String mapOpen = sharedPrefs.getString(getContext().getString(R.string.settings_map_open_key), getContext().getString(R.string.settings_map_open_default));
                    Intent intent = null;
                    if (mapOpen != null) {
                        final String label = Uri.encode(String.format("Earthquake of %1$s", UiUtils.DECIMAL_FORMAT.format(earthquake.getMagnitude())));
                        if (mapOpen.equals(getContext().getString(R.string.settings_map_open_external_value))) {
                            final int zoom = 3;
                            intent = new Intent(Intent.ACTION_VIEW);
                            //TODO bugfix: https://developers.google.com/maps/documentation/urls/android-intents
                            final Uri uri = Uri.parse(String.format("geo:%1$s, %2$s?z=%4$s&q=(%3$s)@%1$s,%2$s", earthquake.getLatitude(), earthquake.getLongitude(), label, zoom));
                            intent.setData(uri);
                        } else if (mapOpen.equals(getContext().getString(R.string.settings_map_open_internal_value))) {
                            //TODO implement activity MapsActivity
                            intent = new Intent(getContext(), MapsActivity.class);
                            final ArrayList<Earthquake> list = new ArrayList<>();
                            list.add(earthquake);
                            intent.putParcelableArrayListExtra(MapsActivity.EARTHQUAKES, list);
                        }
                    }

                    if (intent != null) {
                        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                            getContext().startActivity(intent);
                        }
                    }
                });
            }


            GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();
            int magnitudeColor = ContextCompat.getColor(getContext(), earthquake.getMagnitudeColorIdRef());
            magnitudeCircle.setColor(magnitudeColor);

            magnitude.setText(UiUtils.DECIMAL_FORMAT.format(earthquake.getMagnitude()));
            location.setText(earthquake.getPrimaryLocation());
            locationOffset.setText(earthquake.getLocationOffset());
            date.setText(UiUtils.DATE_FORMAT.format(earthquake.getDate()));
            time.setText(UiUtils.TIME_FORMAT.format(earthquake.getDate()));
        }
        return root;
    }

    private double getTimeAlpha(@NonNull final Date date) {
        // TODO scale from 0.1 to 1.0 relative scale (min/max) from preferences
        return 1;
    }

    private Earthquake mMinMagnitude = null;
    private Earthquake mMaxMagnitude = null;
    private Earthquake mMinTime = null;
    private Earthquake mMaxTime = null;

    //TODO see https://developer.android.com/reference/java/text/SimpleDateFormat.html
    private final LayoutInflater mLayoutInflater;

    private final ArrayList<Earthquake> mEarthquakes = new ArrayList<>();

}