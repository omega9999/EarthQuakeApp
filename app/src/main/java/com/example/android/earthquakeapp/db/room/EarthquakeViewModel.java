package com.example.android.earthquakeapp.db.room;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.earthquakeapp.bean.Earthquake;

import java.util.List;

public class EarthquakeViewModel extends AndroidViewModel {

    public EarthquakeViewModel(Application application) {
        super(application);
        mRepository = new EarthquakeRepository(application);
        mAllEarthquakes = mRepository.getAllEarthquakes();
    }

    LiveData<List<Earthquake>> getAllEarthquakes() {
        return mAllEarthquakes;
    }

    void insert(Earthquake word) {
        mRepository.insert(word);
    }

    private EarthquakeRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<Earthquake>> mAllEarthquakes;
}
