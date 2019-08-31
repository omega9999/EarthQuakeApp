package com.example.android.earthquakeapp.provider.geojson;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.android.earthquakeapp.bean.Earthquake;
import com.example.android.earthquakeapp.R;
import com.example.android.earthquakeapp.bean.EarthquakeList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            Earthquake earthquake = new Earthquake();
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
                earthquake.setId(feature.getId());
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



    /**
     * convert string json into ArrayList<Earthquake> using {@code JSONObject}
     *
     * @param context
     * @param earthquakeJSON string to convert
     * @return {@code ArrayList<Earthquake>} from JSON string
     */
    public static ArrayList<Earthquake> extractFeatureFromJson(@NonNull final Context context, @NonNull final String earthquakeJSON) {
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return new ArrayList<>();
        }

        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        try {
            final JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);
            final JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");
            for (int i = 0; i < earthquakeArray.length(); i++) {
                final JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
                final JSONObject properties = currentEarthquake.getJSONObject("properties");
                final double magnitude = properties.getDouble("mag");
                final String place = properties.getString("place");
                final long time = properties.getLong("time");
                final String url = properties.getString("url");
                Earthquake earthquake = new Earthquake();

                earthquake.setMagnitude(magnitude);
                String[] places = place.split(LOCATION_SEPARATOR);
                String locationOffset = context.getString(R.string.near_the);
                String location = places[0];
                if (places.length > 1) {
                    locationOffset = places[0] + " " + context.getString(R.string.separator);
                    location = places[1];
                }
                earthquake.setPrimaryLocation(location);
                earthquake.setLocationOffset(locationOffset);
                earthquake.setUrl(url);
                earthquake.setDate(new Date(time));

                earthquakes.add(earthquake);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    private static final String LOCATION_SEPARATOR = " of ";

    private static final String TAG = JsonUtils.class.getSimpleName();
}
