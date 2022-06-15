package com.alexsykes.bankmonsterr.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MarkerRepository {
    private AllDao dao;
    private LiveData<List<Marker>> allMarkers;

    MarkerRepository(Application application) {
        WaterRoomDatabase db = WaterRoomDatabase.getDatabase(application);
        dao = db.dao();
        allMarkers = dao.getAllMarkers();
    }

    LiveData<List<Marker>> getAllMarkers() {
        return allMarkers;
    }

    void insert(Marker marker) {
        WaterRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.insertMarker(marker);
        });
    }
}