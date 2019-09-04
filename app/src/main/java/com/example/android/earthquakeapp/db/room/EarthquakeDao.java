package com.example.android.earthquakeapp.db.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.android.earthquakeapp.bean.Earthquake;

import java.util.List;

/**
 * The Room Magic is in this file, where you map a Java method call to an SQL query.
 *
 * When you are using complex data types, such as Date, you have to also supply type converters.
 * To keep this example basic, no types that require type converters are used.
 * See the documentation at
 * https://developer.android.com/topic/libraries/architecture/room.html#type-converters
 */

@Dao
public interface EarthquakeDao {

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * FROM earth_quake ORDER BY date ASC")
    LiveData<List<Earthquake>> getEarthquakeSortByDateAsc();

    @Query("SELECT * FROM earth_quake ORDER BY date DESC")
    LiveData<List<Earthquake>> getEarthquakeSortByDateDesc();

    @Query("SELECT * FROM earth_quake ORDER BY magnitude ASC")
    LiveData<List<Earthquake>> getEarthquakeSortByMagnitudeAsc();

    @Query("SELECT * FROM earth_quake ORDER BY magnitude DESC")
    LiveData<List<Earthquake>> getEarthquakeSortByMagnitudeDesc();

    @Query("SELECT * FROM earth_quake")
    List<Earthquake> getAllEarthquakeSync();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Earthquake earthquake);

    @Query("DELETE FROM earth_quake")
    void deleteAll();
}
