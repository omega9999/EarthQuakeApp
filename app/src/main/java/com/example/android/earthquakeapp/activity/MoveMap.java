package com.example.android.earthquakeapp.activity;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

interface MoveMap {

    void moveMap(@NonNull final LatLng centre, final float zoom);

}
