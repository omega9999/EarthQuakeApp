package com.example.android.earthquakeapp.db.loader;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.earthquakeapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class LoaderUtils {

    @Nullable
    @CheckResult
    public static String[] getQueryUrl(@NonNull final Context context) {
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

    private static final Calendar CALENDAR = GregorianCalendar.getInstance();
    private static final int MAX_SET = 50;
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String BASE_USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private static final String BASE_USGS_COUNT_URL = "https://earthquake.usgs.gov/fdsnws/event/1/count";

    private static final String TAG = LoaderUtils.class.getSimpleName();
}
