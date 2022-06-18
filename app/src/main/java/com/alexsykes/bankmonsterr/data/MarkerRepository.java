package com.alexsykes.bankmonsterr.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MarkerRepository {
    private final MarkerDao dao;
    private final LiveData<List<Marker>> allMarkers;
    private final LiveData<List<Marker>> markersForWater;
    int water_id = 9;

    MarkerRepository(Application application) {
        WaterRoomDatabase db = WaterRoomDatabase.getDatabase(application);
        dao = db.dao();
        allMarkers = dao.getAllMarkers();
        markersForWater = dao.getAllMarkersForWater(water_id);
    }

    LiveData<List<Marker>> getAllMarkers() {
        return allMarkers;
    }

    void insert(Marker marker) {
        WaterRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.insertMarker(marker);
        });
    }

    public LiveData<List<Marker>> getAllMarkersForWater(int water_id) { return markersForWater;
    }
}