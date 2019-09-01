package com.example.android.earthquakeapp.provider.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.earthquakeapp.R;
import com.example.android.earthquakeapp.bean.Earthquake;
import com.example.android.earthquakeapp.provider.db.EarthquakeContract.EarthquakeEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;



public class DbUtils {

    @Nullable
    @CheckResult
    public static String[] getQueryUrl(@NonNull final Context context) {

        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        final String minMagnitude = sharedPrefs.getString(context.getString(R.string.settings_min_magnitude_key), context.getString(R.string.settings_min_magnitude_default));
        final String orderBy = sharedPrefs.getString(context.getString(R.string.settings_order_by_key), context.getString(R.string.settings_order_by_default));
        final String limitRow = sharedPrefs.getString(context.getString(R.string.settings_limit_row_key), context.getString(R.string.settings_limit_row_default));
        final int limit = Integer.valueOf(limitRow);
        final int startTimeLimit = Integer.valueOf(sharedPrefs.getString(context.getString(R.string.settings_start_time_limit_key), context.getString(R.string.settings_start_time_limit_default)));
        final Uri baseUri = Uri.parse(BASE_USGS_REQUEST_URL);

        final Calendar calendar = GregorianCalendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1 * startTimeLimit);
        final String startTime = DATE_FORMAT.format(calendar.getTime());

        int quotient = limit / MAX_SET;
        int rest = (quotient * MAX_SET) < limit ? 1 : 0;
        String[] urls = new String[quotient + rest];

        for (int index = 0; index < urls.length; index++){
            final Uri.Builder uriBuilder = baseUri.buildUpon();
            uriBuilder.appendQueryParameter("format", "geojson");
            uriBuilder.appendQueryParameter("limit", String.valueOf(MAX_SET));
            uriBuilder.appendQueryParameter("minmag", minMagnitude);
            uriBuilder.appendQueryParameter("orderby", orderBy);
            uriBuilder.appendQueryParameter("offset", String.valueOf(index+1));
            //uriBuilder.appendQueryParameter("endtime", "2019-08-16"); // default present
            uriBuilder.appendQueryParameter("starttime", startTime); // default now - 30 days
            urls[index] = uriBuilder.toString();
        }
        return urls;
    }



    public static void addEarthquake(@NonNull final Context context, @NonNull final List<Earthquake> earthquakes) {
        for (final Earthquake earthquake : earthquakes) {
            final ContentValues values = earthquake2ContentValues(earthquake);
            context.getContentResolver().insert(EarthquakeEntry.CONTENT_URI, values);
        }
    }

    public static int getCountEarthquake(@NonNull final Context context){
        final String selection = null;
        final String[] selectionArgs = null;
        final String orderBy = null;

        Cursor cursor = context.getContentResolver().query(EarthquakeEntry.CONTENT_URI, PROJECTION, selection, selectionArgs, orderBy );
        int res = cursor.getCount();
        cursor.close();
        return res;
    }

    public static List<Earthquake> getEarthquakeSync(@NonNull final Context context){
        final String selection = null;
        final String[] selectionArgs = null;
        final String orderBy = null;

        List<Earthquake> earthquakes = new ArrayList<>();
        EarthquakeCursor cursor = (EarthquakeCursor) context.getContentResolver().query(EarthquakeEntry.CONTENT_URI, PROJECTION, selection, selectionArgs, orderBy );
        while(cursor.moveToNext()){
            earthquakes.add(cursor2Earthquake(cursor));
        }
        cursor.close();
        return earthquakes;
    }

    public static CursorLoader getEarthquake(@NonNull final Context context) {
        final String selection = null;
        final String[] selectionArgs = null;
        final String orderBy = null;

        //FIXME togliere la riga Looper.prepare()
        Looper.prepare();

        // db to query may be in other apps with this method: context.getContentResolver()
        final CursorLoader cursorLoader = new CursorLoader(context, EarthquakeEntry.CONTENT_URI,
                PROJECTION, selection, selectionArgs, orderBy);
        Log.d(TAG, "Create cursor loader");
        return cursorLoader;
    }

    @CheckResult
    public static int deleteAllQuake(@NonNull final Context context) {
        return context.getContentResolver().delete(EarthquakeEntry.CONTENT_URI, null, null);
    }


    public static Earthquake cursor2Earthquake(@NonNull final EarthquakeCursor values) {
        return new Earthquake()
                .setMagnitude(values.getDouble(EarthquakeEntry.MAGNITUDE))
                .setPrimaryLocation(values.getString(EarthquakeEntry.PRIMARY_LOCATION))
                .setLocationOffset(values.getString(EarthquakeEntry.LOCATION_OFFSET))
                .setDate(new Date(values.getLong(EarthquakeEntry.DATE)))
                .setUrl(values.getString(EarthquakeEntry.URL))
                .setId(values.getString(EarthquakeEntry.ID_GEO))
                .setLongitude(values.getDouble(EarthquakeEntry.LONGITUDE))
                .setLatitude(values.getDouble(EarthquakeEntry.LATITUDE))
                .setDept(values.getDouble(EarthquakeEntry.DEPT));
    }

    static ContentValues earthquake2ContentValues(Earthquake quake) {
        final ContentValues values = new ContentValues();
        values.put(EarthquakeEntry.MAGNITUDE, quake.getMagnitude());
        values.put(EarthquakeEntry.PRIMARY_LOCATION, quake.getPrimaryLocation());
        values.put(EarthquakeEntry.LOCATION_OFFSET, quake.getLocationOffset());
        values.put(EarthquakeEntry.DATE, quake.getDate().getTime());
        values.put(EarthquakeEntry.URL, quake.getUrl());
        values.put(EarthquakeEntry.ID_GEO, quake.getId());
        values.put(EarthquakeEntry.LONGITUDE, quake.getLongitude());
        values.put(EarthquakeEntry.LATITUDE, quake.getLatitude());
        values.put(EarthquakeEntry.DEPT, quake.getDept());
        return values;
    }

    private static final String[] PROJECTION = {
            EarthquakeEntry._ID,
            EarthquakeEntry.MAGNITUDE,
            EarthquakeEntry.PRIMARY_LOCATION,
            EarthquakeEntry.LOCATION_OFFSET,
            EarthquakeEntry.DATE,
            EarthquakeEntry.URL,
            EarthquakeEntry.ID_GEO,
            EarthquakeEntry.LONGITUDE,
            EarthquakeEntry.LATITUDE,
            EarthquakeEntry.DEPT
    };

    private static final int MAX_SET = 500;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final String BASE_USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private static final String BASE_USGS_COUNT_URL = "https://earthquake.usgs.gov/fdsnws/event/1/count";

    private static final String TAG = DbUtils.class.getSimpleName();
}
