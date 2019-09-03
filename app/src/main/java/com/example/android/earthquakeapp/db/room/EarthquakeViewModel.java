package com.example.android.earthquakeapp.db.room;

import android.app.Application;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.earthquakeapp.bean.Earthquake;

import java.util.List;

public class EarthquakeViewModel extends AndroidViewModel {

    public EarthquakeViewModel(@NonNull final Application application) {
        super(application);
        mRepository = new EarthquakeRepository(application);
        mAllEarthquakes = mRepository.getAllEarthquakes();
    }

    @CheckResult
    public LiveData<List<Earthquake>> getAllEarthquakes() {
        return mAllEarthquakes;
    }

    void insert(@NonNull final Earthquake earthquake) {
        mRepository.insert(earthquake);
    }

    void deleteAll() {
        mRepository.deleteAll();
    }

    private final EarthquakeRepository mRepository;
    // Using LiveData and caching what getEarthquakess returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private final LiveData<List<Earthquake>> mAllEarthquakes;
}
