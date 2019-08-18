package com.example.android.earthquakeapp.bean;

import java.util.ArrayList;

/**
 * class to indicate there are errors
 *
 * @param <T>
 */
public class ErrorList<T> extends ArrayList<T> {

    public Exception getException() {
        return mException;
    }


    public ErrorList<T> setException(Exception exception) {
        this.mException = exception;
        return this;
    }

    private Exception mException;
}