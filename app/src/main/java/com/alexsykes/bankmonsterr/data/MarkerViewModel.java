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

    public MarkerViewModel(@NonNull Application application) {
        super(application);
        markerRepository = new MarkerRepository(application);
        allMarkers = markerRepository.getAllMarkers();
        markersForWater = markerRepository.getAllMarkersForWater(water_id);
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
}
