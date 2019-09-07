package com.example.android.earthquakeapp.db.geojson;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.android.earthquakeapp.bean.Earthquake;
import com.example.android.earthquakeapp.R;
import com.example.android.earthquakeapp.bean.EarthquakeList;

import java.util.ArrayList;
import java.util.Date;

public class JsonUtils {
    /**
     * convert string json into ArrayList<Earthquake> using google {@code Gson}
     *
     * @param context
     * @param json string to convert
     * @return {@code ArrayList<Earthquake>} from JSON string
     */
    public static ArrayList<Earthquake> convertFromJSON(@NonNull final Context context, @NonNull final String json) {
        Log.d(TAG, "start of convertFromJSON method");
        final GeoJSON geoJSON = GeoJSON.createFromJSON(json);
        Log.d(TAG, "object GeoJSON created");
        final EarthquakeList earthquakes = new EarthquakeList();
        for (Feature feature : geoJSON.getFeatures()) {
            Earthquake earthquake = new Earthquake(feature.getId());
            Properties properties = feature.getProperties();

            if (properties != null) {
                earthquake.setMagnitude(properties.getMag());
                String[] places = properties.getPlace().split(LOCATION_SEPARATOR);
                String locationOffset = context.getString(R.string.near_the);
                String location = places[0];
                if (places.length > 1) {
                    locationOffset = places[0] + " " + context.getString(R.string.separator);
                    location = places[1];
                }
                earthquake.setPrimaryLocation(location);
                earthquake.setLocationOffset(locationOffset);
                earthquake.setUrl(properties.getUrl());
                earthquake.setDate(new Date(properties.getTime()));
            }
            Geometry geometry = feature.getGeometry();
            if (geometry != null) {
                if ("Point".equals(geometry.getType())) {
                    if (geometry.getCoordinates() != null && geometry.getCoordinates().size() >= 3) {
                        earthquake.setLongitude(geometry.getCoordinates().get(0));
                        earthquake.setLatitude(geometry.getCoordinates().get(1));
                        earthquake.setDept(geometry.getCoordinates().get(2));
                    }
                }
            }
            earthquakes.add(earthquake);
        }
        Log.d(TAG, "end of convertFromJSON method");
        return earthquakes;
    }




    private static final String LOCATION_SEPARATOR = " of ";

    private static final String TAG = JsonUtils.class.getSimpleName();
}
