package com.example.android.earthquakeapp.bean;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;

public class EarthquakeList extends ArrayList<Earthquake> {

    @Override
    public boolean addAll(@NonNull final Collection<? extends Earthquake> collection) {
        for (Earthquake earthquake : collection) {
            this.add(earthquake);
        }
        return true;
    }

    @Override
    public boolean add(@Nullable final Earthquake earthquake) {
        mMinMagnitude = compareQuake(mMinMagnitude, earthquake, Earthquake::getMagnitude, true);
        mMaxMagnitude = compareQuake(mMaxMagnitude, earthquake, Earthquake::getMagnitude, false);
        mMinTime = compareQuake(mMinTime, earthquake, Earthquake::getDate, true);
        mMaxTime = compareQuake(mMaxTime, earthquake, Earthquake::getDate, false);

        return super.add(earthquake);
    }

    @Nullable
    @CheckResult
    private static Earthquake compareQuake(@Nullable final Earthquake oldValue, @Nullable final Earthquake newValue, Function<Earthquake, Comparable> function, boolean isNatural) {
        Earthquake result = oldValue;
        if (oldValue == null) {
            result = newValue;
        } else if (newValue != null) {
            final Comparator<Comparable> comparator = isNatural ? Comparator.naturalOrder() : Comparator.reverseOrder();
            if (comparator.compare(function.apply(oldValue), function.apply(newValue)) > 0) {
                result = newValue;
            }
        }
        return result;
    }

    public Earthquake getMinMagnitude() {
        return mMinMagnitude;
    }

    public Earthquake getMaxMagnitude() {
        return mMaxMagnitude;
    }

    public Earthquake getMinTime() {
        return mMinTime;
    }

    public Earthquake getMaxTime() {
        return mMaxTime;
    }


    private Earthquake mMinMagnitude = null;
    private Earthquake mMaxMagnitude = null;
    private Earthquake mMinTime = null;
    private Earthquake mMaxTime = null;
}
