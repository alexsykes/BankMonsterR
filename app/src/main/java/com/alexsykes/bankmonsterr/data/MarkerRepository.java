package com.alexsykes.bankmonsterr.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MarkerRepository {
    private MarkerDao markerDao;
    private LiveData<List<Marker>> allMarkers;

    MarkerRepository(Application application) {
        WaterRoomDatabase db = WaterRoomDatabase.getDatabase(application);
        markerDao = db.markerDao();
        allMarkers = markerDao.getAllMarkers();
    }

    LiveData<List<Marker>> getAllMarkers() {
        return allMarkers;
    }

    void insert(Marker marker) {
        WaterRoomDatabase.databaseWriteExecutor.execute(() -> {
            markerDao.insert(marker);
        });
    }
}