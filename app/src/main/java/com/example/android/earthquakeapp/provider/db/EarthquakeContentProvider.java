package com.example.android.earthquakeapp.provider.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.earthquakeapp.provider.db.EarthquakeContract.EarthquakeEntry;
import com.example.android.earthquakeapp.provider.exception.IllegalUriException;

public class EarthquakeContentProvider extends ContentProvider {
    public EarthquakeContentProvider() {
        Log.d(TAG,"EarthquakeContentProvider constructor");
    }

    @Override
    public boolean onCreate() {
        if (getContext() != null) {
            helper = new EarthquakeDbHelper(getContext());
            database = helper.getWritableDatabase();
        }
        return true;
    }

    @Override
    public void shutdown() {
        if (database != null) {
            database.close();
        }
        helper = null;

        super.shutdown();
    }

    @Override
    public Cursor query(@NonNull final Uri uri, @Nullable final String[] projection, @Nullable final String selection,
                        @Nullable final String[] selectionArgs, @Nullable final String sortOrder) {
        Cursor cursor;
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case QUAKES:
                cursor = database.query(EarthquakeEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case QUAKE_ID:
                String selectionLocal = EarthquakeEntry._ID + "=?";
                String[] selectionArgsLocal = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(EarthquakeEntry.TABLE_NAME, projection, selectionLocal, selectionArgsLocal, null, null, sortOrder);
                break;
            default:
                // URI unknown
                throw new IllegalUriException("Cannot query unknown URI: %1$s", uri);
        }

        if (getContext() != null) {
            // Set notification URI on the Cursor,
            // so we know what content URI the Cursor was created for.
            // If the data at this URI changes, then we know we need to update the Cursor.
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Override
    public Uri insert(@NonNull final Uri uri, @Nullable final ContentValues values) {
        int match = URI_MATCHER.match(uri);
        if (match == QUAKES) {

            long id = database.insert(EarthquakeEntry.TABLE_NAME, null, values);
            if (getContext() != null) {
                // notify all listeners that the data at the given URI has changed
                getContext().getContentResolver().notifyChange(uri, null);
            }
            // return Uri with id appended
            return ContentUris.withAppendedId(uri, id);
        } else {
            // URI unknown
            throw new IllegalUriException("Cannot insert unknown URI: %1$s", uri);
        }
    }


    @Override
    public int update(@NonNull final Uri uri, @Nullable final ContentValues values, @Nullable final String selection, @Nullable final String[] selectionArgs) {
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case QUAKES:
                return updateQuake(uri, values, selection, selectionArgs);
            case QUAKE_ID:
                String selectionLocal = EarthquakeEntry._ID + "=?";
                String[] selectionArgsLocal = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateQuake(uri, values, selectionLocal, selectionArgsLocal);
            default:
                // URI unknown
                throw new IllegalUriException("Cannot update unknown URI: %1$s", uri);
        }
    }


    @Override
    public int delete(@NonNull final Uri uri, @Nullable final String whereClause, @Nullable final String[] whereArgs) {
        int match = URI_MATCHER.match(uri);
        String selectionLocal;
        String[] selectionArgsLocal;
        switch (match) {
            case QUAKES:
                selectionLocal = null;
                selectionArgsLocal = null;
                break;
            case QUAKE_ID:
                selectionLocal = EarthquakeEntry._ID + "=?";
                selectionArgsLocal = new String[]{String.valueOf(ContentUris.parseId(uri))};
                break;
            default:
                // URI unknown
                throw new IllegalUriException("Cannot delete unknown URI: %1$s", uri);
        }
        final int row = database.delete(EarthquakeEntry.TABLE_NAME, selectionLocal, selectionArgsLocal);
        if (row > 0 && getContext() != null) {
            // notify all listeners that the data at the given URI has changed
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return row;
    }

    /**
     * The returned MIME type should start with "vnd.android.cursor.item/" for a single record,
     * or "vnd.android.cursor.dir/" for multiple items
     *
     * @param uri {@code Uri} of resource
     * @return {@code String} that describes the type of the data stored at the input Uri (MIME type)
     */
    @Override
    public String getType(@NonNull final Uri uri) {
        String res;
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case QUAKES:
                res = EarthquakeEntry.CONTENT_LIST_TYPE;
                break;
            case QUAKE_ID:
                res = EarthquakeEntry.CONTENT_ITEM_TYPE;
                break;
            default:
                throw new IllegalUriException("Cannot getType unknown URI: %1$s", uri);
        }
        return res;
    }

    private int updateQuake(Uri uri, ContentValues values, String whereClause, String[] whereArgs) {
        final int rows = database.update(EarthquakeEntry.TABLE_NAME, values, whereClause, whereArgs);
        if (rows > 0 && getContext() != null) {
            // notify all listeners that the data at the given URI has changed
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows;
    }

    private EarthquakeDbHelper helper = null;
    private SQLiteDatabase database = null;

    /**
     * URI matcher code for the content URI for the pets table
     */
    private static final int QUAKES = 100;
    /**
     * URI matcher code for the content URI for a single pet in the pets table
     */
    private static final int QUAKE_ID = 101;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // int code MUST be unique into same URI_MATCHER
        URI_MATCHER.addURI(EarthquakeContract.CONTENT_AUTHORITY, EarthquakeContract.PATH_QUAKE, QUAKES);
        URI_MATCHER.addURI(EarthquakeContract.CONTENT_AUTHORITY, EarthquakeContract.PATH_QUAKE + "/#", QUAKE_ID);
    }

    private static final String TAG = EarthquakeContentProvider.class.getSimpleName();
}
