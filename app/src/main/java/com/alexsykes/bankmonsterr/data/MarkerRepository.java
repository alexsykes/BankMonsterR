package com.alexsykes.bankmonsterr.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MarkerRepository {
    private final MarkerDao dao;
    private final List<BMarker> allBMarkers;
    private final LiveData<List<BMarker>> markersForWater;
    int water_id;

    MarkerRepository(Application application) {
        WaterRoomDatabase db = WaterRoomDatabase.getDatabase(application);
        dao = db.dao();
        allBMarkers = dao.getAllMarkers();
        markersForWater = dao.getAllMarkersForWater(water_id);
    }

    List<BMarker> getAllMarkers() {
        return allBMarkers;
    }

    public void setWater_id(int water_id) {
        this.water_id = water_id;
    }

    void insert(BMarker BMarker) {
        WaterRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.insertMarker(BMarker);
        });
    }

    public LiveData<List<BMarker>> getAllMarkersForWater(int water_id) {
        return markersForWater;
    }

    public List<BMarker> getMarkerList(int water_id) {
        return dao.getAllMarkerList(water_id);
    }
}