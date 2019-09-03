package com.example.android.earthquakeapp.db.room;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.android.earthquakeapp.R;
import com.example.android.earthquakeapp.bean.Earthquake;

import java.util.List;

public class EarthquakeRepository {


    // Note that in order to unit test the EarthquakeRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    EarthquakeRepository(@NonNull final Context context) {
        final EarthquakeRoomDatabase db = EarthquakeRoomDatabase.getDatabase(context);
        mEarthquakeDao = db.earthquakeDao();

        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        final String orderByString = sharedPrefs.getString(context.getString(R.string.settings_order_by_key), context.getString(R.string.settings_order_by_default));

        if (orderByString != null) {
            switch (orderByString) {
                case "magnitude":
                    mAllEarthquakes = mEarthquakeDao.getEarthquakeSortByMagnitudeDesc();
                    break;
                case "time":
                    mAllEarthquakes = mEarthquakeDao.getEarthquakeSortByDateDesc();
                    break;
                case "magnitude-asc":
                    mAllEarthquakes = mEarthquakeDao.getEarthquakeSortByMagnitudeAsc();
                    break;
                case "time-asc":
                    mAllEarthquakes = mEarthquakeDao.getEarthquakeSortByDateAsc();
                    break;
                default:
                    throw new RuntimeException("Sorting unknown");
            }
        }

        this.mHandlerThread = new HandlerThread(EarthquakeRepository.class + ".Thread");
        this.mHandlerThread.start(); // close it with mHandlerThread.quit()
        this.mHandler = new Handler(this.mHandlerThread.getLooper());

    }

    @Override
    protected void finalize() throws Throwable {
        this.mHandlerThread.quit();
        super.finalize();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Earthquake>> getAllEarthquakes() {
        return mAllEarthquakes;
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    void insert(Earthquake earthquake) {
        if (Looper.getMainLooper().equals(Looper.myLooper())) {
            mHandler.post(() -> mEarthquakeDao.insert(earthquake));
        } else {
            mEarthquakeDao.insert(earthquake);
        }
    }

    void deleteAll() {
        if (Looper.getMainLooper().equals(Looper.myLooper())) {
            mHandler.post(() -> mEarthquakeDao.deleteAll());
        } else {
            mEarthquakeDao.deleteAll();
        }
    }


    private EarthquakeDao mEarthquakeDao;
    private LiveData<List<Earthquake>> mAllEarthquakes;

    private final HandlerThread mHandlerThread;
    private final Handler mHandler;

}
