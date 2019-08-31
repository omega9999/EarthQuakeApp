package com.example.android.earthquakeapp.provider.db;

import android.content.ContentResolver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;

final class EarthquakeContract {


    // see AndroidManifest.xml for CONTENT_AUTHORITY
    static final String CONTENT_AUTHORITY = "com.example.android.earthquake";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    static final String PATH_QUAKE = "earthquake";

    private EarthquakeContract() {
    }


    static abstract class EarthquakeEntry implements BaseColumns {
        // Uri for ContentProvider to access table pets
        static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_QUAKE);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_QUAKE;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_QUAKE;


        static final String TABLE_NAME = "earth_quake";
        static final String _ID = BaseColumns._ID;


        static final String MAGNITUDE = "magnitude";
        static final String PRIMARY_LOCATION = "primary_location";
        static final String LOCATION_OFFSET = "location_offset";
        static final String DATE = "date";
        static final String URL = "url";
        static final String ID_GEO = "id_geo";
        static final String LONGITUDE = "longitude";
        static final String LATITUDE = "latitude";
        static final String DEPT = "dept";


        static void onCreate(@NonNull final SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        static void onDrop(@NonNull final SQLiteDatabase db) {
            db.execSQL(SQL_DELETE_ENTRIES);
        }

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + EarthquakeEntry.TABLE_NAME + " (" +
                        EarthquakeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        MAGNITUDE + " DOUBLE, " +
                        PRIMARY_LOCATION + " TEXT, " +
                        LOCATION_OFFSET + " TEXT, " +
                        DATE + " LONG, " +
                        URL + " TEXT, " +
                        ID_GEO + " TEXT, " +
                        LONGITUDE + " DOUBLE, " +
                        LATITUDE + " DOUBLE, " +
                        DEPT + " DOUBLE " +
                        ")";

        private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
