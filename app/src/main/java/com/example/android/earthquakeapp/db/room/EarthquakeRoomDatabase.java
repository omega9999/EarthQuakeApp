package com.example.android.earthquakeapp.db.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.android.earthquakeapp.bean.Earthquake;
import com.example.android.earthquakeapp.db.Converters;

@Database(entities = {Earthquake.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class EarthquakeRoomDatabase extends RoomDatabase {

    public abstract EarthquakeDao earthquakeDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile EarthquakeRoomDatabase INSTANCE;

    static EarthquakeRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EarthquakeRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), EarthquakeRoomDatabase.class)
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this codelab.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     * <p>
     * If you want to populate the database only when the database is created for the 1st time,
     * override RoomDatabase.Callback()#onCreate
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };
}
