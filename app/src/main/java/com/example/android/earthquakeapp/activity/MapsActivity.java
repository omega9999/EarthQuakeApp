package com.example.android.earthquakeapp.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentActivity;

import com.example.android.earthquakeapp.Configurations;
import com.example.android.earthquakeapp.R;
import com.example.android.earthquakeapp.bean.Earthquake;
import com.example.android.earthquakeapp.bean.EarthquakeList;
import com.example.android.earthquakeapp.bean.MagnitudeDrawable;
import com.example.android.earthquakeapp.provider.db.DbUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String EARTHQUAKES = MapsActivity.class.getPackage() + ".EARTHQUAKES";

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Log.d(TAG, "onCreate");

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> NavUtils.navigateUpFromSameTask(MapsActivity.this));

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    if (key.startsWith(EARTHQUAKES)) {
                        final ArrayList<Earthquake> list = bundle.getParcelableArrayList(key);
                        if (list != null) {
                            mEarthquakes.addAll(list);
                        }
                    }
                }
            }
        }

        new Handler(new HandlerThread(TAG + ".Thread").getLooper()).post(() -> mEarthquakes.addAll(DbUtils.getEarthquakeSync(this)));


        Log.d(TAG, "list received");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull final GoogleMap googleMap) {

        LatLng centerCoords = new LatLng(0, 0);
        double latMin;
        double lonMin;
        double latMax;
        double lonMax;
        int zoom = 4;
        if (mEarthquakes.size() > 0) {
            latMin = mEarthquakes.get(0).getLatitude();
            lonMin = mEarthquakes.get(0).getLongitude();
            latMax = mEarthquakes.get(0).getLatitude();
            lonMax = mEarthquakes.get(0).getLongitude();

            Log.d(TAG, "start list decoding");
            for (Earthquake earthquake : mEarthquakes) {
                final MarkerOptions options = new MarkerOptions();
                options.position(new LatLng(earthquake.getLatitude(), earthquake.getLongitude()));

                final String key = getKeyMarker(earthquake);
                if (!mMapMarker.containsKey(key)) {
                    Bitmap bitmap = UiUtils.drawableToBitmap(new MagnitudeDrawable(this, earthquake));
                    final BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
                    mMapMarker.put(key, icon);
                }

                options.icon(mMapMarker.get(key));
                options.title(getTitle(earthquake));
                googleMap.addMarker(options);

                latMin = Math.min(latMin, earthquake.getLatitude());
                lonMin = Math.min(lonMin, earthquake.getLongitude());
                latMax = Math.max(latMax, earthquake.getLatitude());
                lonMax = Math.max(lonMax, earthquake.getLongitude());
            }

            Log.d(TAG, String.format("Number of earthquake %1$s, number of different marks %2$s", mEarthquakes.size(), mMapMarker.keySet().size()));

            Log.d(TAG, "end list decoding");

            double delta = Math.max(Math.abs(latMax - latMin), Math.abs(lonMax - lonMin));
            Log.d(TAG, String.format("Delta between min/max %1$s", delta));
            //TODO decidere meglio lo zoom (lo zoom è un float)
            if (delta >= 0 && delta < 3) {
                zoom = 4;
            } else {
                zoom = 1;
            }

            centerCoords = new LatLng((latMax + latMin) / 2, (lonMax + lonMin) / 2);

        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerCoords, zoom));
    }

    private String getTitle(Earthquake earthquake) {
        final StringBuilder builder = new StringBuilder();
        builder.append("M ").append(UiUtils.DECIMAL_FORMAT.format(earthquake.getMagnitude()));
        builder.append(" - ").append(UiUtils.DATE_FORMAT.format(earthquake.getDate()));
        builder.append(" - ").append(earthquake.getLocationOffset()).append(" ").append(earthquake.getPrimaryLocation());
        return builder.toString();
    }

    private String getKeyMarker(@NonNull final Earthquake earthquake) {
        return String.format("%1$s_%2$s", earthquake.getMagnitudeColorIdRef(), UiUtils.DECIMAL_FORMAT.format(earthquake.getMagnitude()));
    }

    private Map<String, BitmapDescriptor> mMapMarker = new HashMap<>();

    private final EarthquakeList mEarthquakes = new EarthquakeList();

    private static final String TAG = MapsActivity.class.getSimpleName();
}
