package com.example.android.earthquakeapp.provider.exception;

import androidx.annotation.NonNull;

public class IllegalUriException extends IllegalArgumentException {
    public IllegalUriException(@NonNull final String msg, Object ... args){
        super(String.format(msg, args));
    }
}
