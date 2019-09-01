package com.example.android.earthquakeapp.activity;

import androidx.annotation.NonNull;

import com.example.android.earthquakeapp.bean.Earthquake;

interface WriteMark {
    void writeEarthquake(@NonNull final Earthquake earthquake);
}
