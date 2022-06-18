package com.alexsykes.bankmonsterr.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MarkerViewModel extends AndroidViewModel {
    private final MarkerRepository markerRepository;

    private final LiveData<List<Marker>> allMarkers;
    private final LiveData<List<Marker>> markersForWater;
    private int water_id;
    private List<Marker> markerList;

    public MarkerViewModel(@NonNull Application application) {
        super(application);
        markerRepository = new MarkerRepository(application);
        markerRepository.setWater_id(water_id);
        allMarkers = markerRepository.getAllMarkers();
        markersForWater = markerRepository.getAllMarkersForWater(water_id);
        markerList = markerRepository.getMarkerList(water_id);
    }

    public LiveData<List<Marker>> getAllParents() {
        return allMarkers;
    }

    public void insert(Marker marker) {
        markerRepository.insert(marker);
    }

    public LiveData<List<Marker>> getMarkerListForWater(int water_id) {
        return markersForWater;
    }

    public void setWater_id(int water_id) {
        this.water_id = water_id;
    }

    public List<Marker> getMarkerList(int water_id) { return markerList;
    }
}
