package com.example.android.earthquakeapp;

import android.content.Context;

import androidx.room.Room;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.core.app.ApplicationProvider;

import com.example.android.earthquakeapp.bean.Earthquake;
import com.example.android.earthquakeapp.db.room.EarthquakeDao;
import com.example.android.earthquakeapp.db.room.EarthquakeRoomDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class SimpleEntityReadWriteTest {
    private EarthquakeDao userDao;
    private EarthquakeRoomDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, EarthquakeRoomDatabase.class).build();
        userDao = db.earthquakeDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        Earthquake user = new Earthquake("123");
        user.setMagnitude(6.2);
        userDao.insert(user);
        //FIXME
        //List<Earthquake> byName = userDao.getEarthquakeSortByMagnitudeAsc();
        //assertThat(byName.get(0), equalTo(user));
    }
}