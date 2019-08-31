package com.example.android.earthquakeapp.provider.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

import com.example.android.earthquakeapp.bean.Earthquake;
import com.example.android.earthquakeapp.provider.db.EarthquakeContract.EarthquakeEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbUtils {

    public static void addEarthquake(@NonNull final Context context, @NonNull final List<Earthquake> earthquakes) {
        for (final Earthquake earthquake : earthquakes) {
            final ContentValues values = earthquake2ContentValues(earthquake);
            context.getContentResolver().insert(EarthquakeEntry.CONTENT_URI, values);
        }
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

    private static final String TAG = DbUtils.class.getSimpleName();
}
