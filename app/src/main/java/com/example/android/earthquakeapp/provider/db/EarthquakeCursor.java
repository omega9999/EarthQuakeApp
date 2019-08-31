package com.example.android.earthquakeapp.provider.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import androidx.annotation.NonNull;

public class EarthquakeCursor extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public EarthquakeCursor(@NonNull final Cursor cursor) {
        super(cursor);
    }

    public Double getDouble(@NonNull final String column) {
        final int index = getWrappedCursor().getColumnIndex(column);
        return getWrappedCursor().getDouble(index);
    }

    public String getString(@NonNull final String column) {
        final int index = getWrappedCursor().getColumnIndex(column);
        return getWrappedCursor().getString(index);
    }

    public long getLong(@NonNull final String column) {
        final int index = getWrappedCursor().getColumnIndex(column);
        return getWrappedCursor().getLong(index);
    }
}
