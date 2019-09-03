package com.example.android.earthquakeapp.db.loader;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.earthquakeapp.R;
import com.example.android.earthquakeapp.bean.Earthquake;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class DbUtils {

    @Nullable
    @CheckResult
    static String[] getQueryUrl(@NonNull final Context context) {
        final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault());
        DATE_FORMAT.setTimeZone(TimeZone.getDefault()); // getTimeZone("GMT"));
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        final String minMagnitude = sharedPrefs.getString(context.getString(R.string.settings_min_magnitude_key), context.getString(R.string.settings_min_magnitude_default));
        final String orderBy = sharedPrefs.getString(context.getString(R.string.settings_order_by_key), context.getString(R.string.settings_order_by_default));
        final String limitRow = sharedPrefs.getString(context.getString(R.string.settings_limit_row_key), context.getString(R.string.settings_limit_row_default));
        final int startTimeLimit = Integer.valueOf(sharedPrefs.getString(context.getString(R.string.settings_start_time_limit_key), context.getString(R.string.settings_start_time_limit_default)));
        final Uri baseUri = Uri.parse(BASE_USGS_REQUEST_URL);

        CALENDAR.setTime(new Date());
        CALENDAR.add(Calendar.DAY_OF_YEAR, -1 * startTimeLimit);
        final long startTimeTarget = CALENDAR.getTimeInMillis();
        //DATE_FORMAT.format(calendar.getTime());

        final Date endTimeDate = new Date();
        long endTime = endTimeDate.getTime();
        long startTime = getStartTime(endTime, startTimeTarget);

        ArrayList<String> urls = new ArrayList<>();

        while(startTimeTarget <= startTime) {
            if (startTime >= endTime){
                break;
            }
            final Uri.Builder uriBuilder = baseUri.buildUpon();
            uriBuilder.appendQueryParameter("format", "geojson");
            //uriBuilder.appendQueryParameter("limit", limitRow);
            uriBuilder.appendQueryParameter("minmag", minMagnitude);
            uriBuilder.appendQueryParameter("orderby", orderBy);
            //uriBuilder.appendQueryParameter("offset", String.valueOf(index + 1));
            uriBuilder.appendQueryParameter("endtime", long2DateString(DATE_FORMAT, endTime)); // default present
            uriBuilder.appendQueryParameter("starttime", long2DateString(DATE_FORMAT, startTime)); // default now - 30 days
            urls.add(uriBuilder.toString());

            /*
            calendar.setTime(new Date(startTime));
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            endTime = calendar.getTimeInMillis();
            */
            endTime = startTime;
            startTime = getStartTime(endTime, startTimeTarget);

        }
        return urls.toArray(new String[0]);
    }

    private static long getStartTime(final long endTime, final long lowerbound){
        CALENDAR.setTimeInMillis(endTime);
        CALENDAR.add(Calendar.DAY_OF_YEAR, -15);
        return Math.max(CALENDAR.getTimeInMillis(),lowerbound);
    }

    private static String long2DateString(SimpleDateFormat DATE_FORMAT, final long time){
        return DATE_FORMAT.format(new Date(time).getTime());
    }


    static void addEarthquake(@NonNull final Context context, @NonNull final List<Earthquake> earthquakes) {
        for (final Earthquake earthquake : earthquakes) {
            final ContentValues values = earthquake2ContentValues(earthquake);
            context.getContentResolver().insert(EarthquakeEntry.CONTENT_URI, values);
        }
    }

    static int getCountEarthquake(@NonNull final Context context) {
        final String selection = null;
        final String[] selectionArgs = null;
        final String orderBy = null;

        Cursor cursor = context.getContentResolver().query(EarthquakeEntry.CONTENT_URI, PROJECTION, selection, selectionArgs, orderBy);
        int res = cursor.getCount();
        cursor.close();
        return res;
    }

    public static EarthquakeCursor getEarthquakeSync(@NonNull final Context context) {
        final String selection = null;
        final String[] selectionArgs = null;
        final String orderBy = null;

        Cursor cursor = context.getContentResolver().query(EarthquakeEntry.CONTENT_URI, PROJECTION, selection, selectionArgs, orderBy);
        return new EarthquakeCursor(cursor);
    }

    public static CursorLoader getEarthquake(@NonNull final Context context) {
        final String selection = null;
        final String[] selectionArgs = null;
        String orderBy = null;

        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        final String orderByString = sharedPrefs.getString(context.getString(R.string.settings_order_by_key), context.getString(R.string.settings_order_by_default));

        if (orderByString != null) {
            switch (orderByString) {
                case "magnitude":
                    orderBy = EarthquakeEntry.MAGNITUDE + " DESC";
                    break;
                case "time":
                    orderBy = EarthquakeEntry.DATE + " DESC";
                    break;
                case "magnitude-asc":
                    orderBy = EarthquakeEntry.MAGNITUDE + " ASC";
                    break;
                case "time-asc":
                    orderBy = EarthquakeEntry.DATE + " ASC";
                    break;
            }
        }

        // db to query may be in other apps with this method: context.getContentResolver()
        final CursorLoader cursorLoader = new CursorLoader(context, EarthquakeEntry.CONTENT_URI,
                PROJECTION, selection, selectionArgs, orderBy);
        Log.d(TAG, "Create cursor loader");
        return cursorLoader;
    }

    @CheckResult
    static int deleteAllQuake(@NonNull final Context context) {
        return context.getContentResolver().delete(EarthquakeEntry.CONTENT_URI, null, null);
    }


    public static Earthquake cursor2Earthquake(@NonNull final Cursor val) {
        final EarthquakeCursor values = new EarthquakeCursor(val);
        return new Earthquake()
                .setMagnitude(values.getDouble(EarthquakeEntry.MAGNITUDE))
                .setPrimaryLocation(values.getString(EarthquakeEntry.PRIMARY_LOCATION))
                .setLocationOffset(values.getString(EarthquakeEntry.LOCATION_OFFSET))
                .setDate(new Date(values.getLong(EarthquakeEntry.DATE)))
                .setUrl(values.getString(EarthquakeEntry.URL))
                .setId(values.getString(EarthquakeEntry.ID_GEO))
                .setLongitude(values.getDouble(EarthquakeEntry.LONGITUDE))
                .setLatitude(values.getDouble(EarthquakeEntry.LATITUDE))
                .setDept(values.getDouble(EarthquakeEntry.DEPT))
                .setUrlRequest(values.getString(EarthquakeEntry.URL_REQUEST));
    }

    static ContentValues earthquake2ContentValues(Earthquake quake) {
        final ContentValues values = new ContentValues();
        values.put(EarthquakeEntry.MAGNITUDE, quake.getMagnitude());
        values.put(EarthquakeEntry.PRIMARY_LOCATION, quake.getPrimaryLocation());
        values.put(EarthquakeEntry.LOCATION_OFFSET, quake.getLocationOffset());
        values.put(EarthquakeEntry.DATE, quake.getDate().getTime());
        values.put(EarthquakeEntry.URL, quake.getUrl());
        values.put(EarthquakeEntry.URL_REQUEST, quake.getUrlRequest());
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
            EarthquakeEntry.DEPT,
            EarthquakeEntry.URL_REQUEST
    };

    private static final Calendar CALENDAR = GregorianCalendar.getInstance();
    private static final int MAX_SET = 50;
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String BASE_USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private static final String BASE_USGS_COUNT_URL = "https://earthquake.usgs.gov/fdsnws/event/1/count";

    private static final String TAG = DbUtils.class.getSimpleName();
}
