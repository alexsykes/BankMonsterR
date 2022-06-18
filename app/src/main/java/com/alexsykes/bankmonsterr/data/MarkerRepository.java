package com.alexsykes.bankmonsterr.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MarkerRepository {
    private final MarkerDao dao;
    private final LiveData<List<Marker>> allMarkers;
    private final LiveData<List<Marker>> markersForWater;
    // private final List<Marker> markerList;
    int water_id;

    MarkerRepository(Application application) {
        WaterRoomDatabase db = WaterRoomDatabase.getDatabase(application);

        dao = db.dao();
        allMarkers = dao.getAllMarkers();
        markersForWater = dao.getAllMarkersForWater(water_id);
    }

    LiveData<List<Marker>> getAllMarkers() {
        return allMarkers;
    }

    public void setWater_id(int water_id) {
        this.water_id = water_id;
    }

    void insert(Marker marker) {
        WaterRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.insertMarker(marker);
        });
    }

    public LiveData<List<Marker>> getAllMarkersForWater(int water_id) { return markersForWater;
    }

    public List<Marker> getMarkerList(int water_id) {
        List<Marker>  markerList = dao.getAllMarkerList(water_id);
        return markerList;
    }
}