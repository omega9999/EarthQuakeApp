package com.example.android.earthquakeapp.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.android.earthquakeapp.R;
import com.example.android.earthquakeapp.bean.Earthquake;
import com.example.android.earthquakeapp.bean.EarthquakeList;
import com.example.android.earthquakeapp.bean.MagnitudeDrawable;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String EARTHQUAKES = "EARTHQUAKES";



    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (getIntent() != null) {
            ArrayList<Earthquake> list = getIntent().getParcelableArrayListExtra(EARTHQUAKES);
            mEarthquakes.addAll(list);
        }

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
        mMap = googleMap;

        LatLng centerCoords = new LatLng(0, 0);
        double latMed = centerCoords.latitude;
        double lonMed = centerCoords.longitude;
        int zoom = 4;
        if (mEarthquakes.size() > 0){
            for (Earthquake earthquake : mEarthquakes){
                final MarkerOptions options = new MarkerOptions();
                options.position(new LatLng(earthquake.getLatitude(), earthquake.getLongitude()));


                GradientDrawable magnitudeCircle = (GradientDrawable) getDrawable(R.drawable.magnitude_circle);
                int magnitudeColor = ContextCompat.getColor(this, earthquake.getMagnitudeColorIdRef());
                magnitudeCircle.setColor(magnitudeColor);


                Bitmap bitmap = UiUtils.drawableToBitmap(magnitudeCircle);
                //Bitmap bitmap = UiUtils.drawableToBitmap(new MagnitudeDrawable(this, earthquake));
                final BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
                options.icon(icon);
                options.title(UiUtils.DECIMAL_FORMAT.format(earthquake.getMagnitude()));
                mMap.addMarker(options);
                latMed += earthquake.getLatitude();
                lonMed += earthquake.getLongitude();
            }
            latMed /= mEarthquakes.size();
            lonMed /= mEarthquakes.size();
            centerCoords = new LatLng(latMed, lonMed);

        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerCoords,zoom));
    }



    private GoogleMap mMap;

    private final EarthquakeList mEarthquakes = new EarthquakeList();

    private static final String TAG = MapsActivity.class.getSimpleName();
}
