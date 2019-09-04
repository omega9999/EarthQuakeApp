package com.example.android.earthquakeapp.db.room;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

import com.example.android.earthquakeapp.R;
import com.example.android.earthquakeapp.bean.Earthquake;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class DbUtils {

    public static void addEarthquake(@NonNull final Application application, @NonNull final List<Earthquake> earthquakes) {
        final EarthquakeViewModel model = new EarthquakeViewModel(application);
        for (final Earthquake earthquake : earthquakes) {
            model.insert(earthquake);
        }
    }

    public static int getCountEarthquake(@NonNull final Application application) {
        return getEarthquakeSync(application).size();
    }

    public static List<Earthquake> getEarthquakeSync(@NonNull final Application application) {
        final EarthquakeViewModel model = new EarthquakeViewModel(application);
        return model.getAllEarthquakesSync();
    }



    @CheckResult
    public static int deleteAllQuake(@NonNull final Application application) {
        final EarthquakeViewModel model = new EarthquakeViewModel(application);
        model.deleteAll();
        return 0; // FIXME
    }


    private static final String TAG = DbUtils.class.getSimpleName();
}
