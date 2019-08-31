package com.example.android.earthquakeapp.loader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
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
import com.example.android.earthquakeapp.provider.db.DbUtils;
import com.example.android.earthquakeapp.provider.db.EarthquakeCursor;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

public class EarthquakeAdapter extends CursorAdapter {


    // N.B. method getView() implemented by CursorAdapter that call newView() and bindView()

    public EarthquakeAdapter(@NonNull final Context context, @Nullable final Cursor cursor) {
        super(context, cursor, 0);
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    /**
     * ONLY INFLATE
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(@NonNull final Context context, @Nullable final Cursor cursor, @NonNull final ViewGroup parent) {
        return this.mLayoutInflater.inflate(R.layout.earthquake_item, parent, false);
    }

    /**
     * POPULATE VIEW FROM OUTSIDE
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param root    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(@NonNull final View root, @NonNull final Context context, @NonNull final Cursor cursor) {
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        final int position = cursor.getPosition();

        final Earthquake earthquake = DbUtils.cursor2Earthquake((EarthquakeCursor) cursor);
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
                    Toast.makeText(context, earthquake.getPrimaryLocation(), Toast.LENGTH_SHORT).show();
                    final String webOpen = sharedPrefs.getString(context.getString(R.string.settings_web_open_key), context.getString(R.string.settings_web_open_default));

                    Intent intent = null;
                    if (webOpen != null) {
                        if (webOpen.equals(context.getString(R.string.settings_web_open_external_value))) {
                            intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(earthquake.getUrl()));
                        } else if (webOpen.equals(context.getString(R.string.settings_web_open_internal_value))) {
                            //TODO implement activity WebActivity
                            intent = new Intent(context, WebActivity.class);
                            intent.setData(Uri.parse(earthquake.getUrl()));
                            intent.putExtra(WebActivity.TITLE, earthquake.getLocationOffset() + " " + earthquake.getPrimaryLocation());
                        }
                    }

                    if (intent != null) {
                        if (intent.resolveActivity(context.getPackageManager()) != null) {
                            context.startActivity(intent);
                        }
                    }
                });
            }
            if (earthquake.isCoordinates()) {
                mapSearch.setVisibility(View.VISIBLE);
                mapSearch.setOnClickListener(v -> {
                    Toast.makeText(context, earthquake.getPrimaryLocation(), Toast.LENGTH_SHORT).show();
                    final String mapOpen = sharedPrefs.getString(context.getString(R.string.settings_map_open_key), context.getString(R.string.settings_map_open_default));
                    Intent intent = null;
                    if (mapOpen != null) {
                        final String label = Uri.encode(String.format("Earthquake of %1$s", UiUtils.DECIMAL_FORMAT.format(earthquake.getMagnitude())));
                        if (mapOpen.equals(context.getString(R.string.settings_map_open_external_value))) {
                            final int zoom = 3;
                            intent = new Intent(Intent.ACTION_VIEW);
                            //TODO bugfix: https://developers.google.com/maps/documentation/urls/android-intents
                            final Uri uri = Uri.parse(String.format("geo:%1$s, %2$s?z=%4$s&q=(%3$s)@%1$s,%2$s", earthquake.getLatitude(), earthquake.getLongitude(), label, zoom));
                            intent.setData(uri);
                        } else if (mapOpen.equals(context.getString(R.string.settings_map_open_internal_value))) {
                            //TODO implement activity MapsActivity
                            intent = new Intent(context, MapsActivity.class);
                            final ArrayList<Earthquake> list = new ArrayList<>();
                            list.add(earthquake);
                            intent.putParcelableArrayListExtra(MapsActivity.EARTHQUAKES, list);
                        }
                    }

                    if (intent != null) {
                        if (intent.resolveActivity(context.getPackageManager()) != null) {
                            context.startActivity(intent);
                        }
                    }
                });
            }


            GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();
            int magnitudeColor = ContextCompat.getColor(context, earthquake.getMagnitudeColorIdRef());
            magnitudeCircle.setColor(magnitudeColor);

            magnitude.setText(UiUtils.DECIMAL_FORMAT.format(earthquake.getMagnitude()));
            location.setText(earthquake.getPrimaryLocation());
            locationOffset.setText(earthquake.getLocationOffset());
            date.setText(UiUtils.DATE_FORMAT.format(earthquake.getDate()));
            time.setText(UiUtils.TIME_FORMAT.format(earthquake.getDate()));
        }
    }


    //TODO see https://developer.android.com/reference/java/text/SimpleDateFormat.html
    private final LayoutInflater mLayoutInflater;


}