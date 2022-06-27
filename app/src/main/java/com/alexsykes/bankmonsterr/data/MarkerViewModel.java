package com.alexsykes.bankmonsterr.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

public class MarkerViewModel extends AndroidViewModel {
    private final MarkerRepository markerRepository;

    private final List<BMarker> allBMarkers;
    private List<BMarker> BMarkerList;

    public MarkerViewModel(@NonNull Application application) {
        super(application);
        markerRepository = new MarkerRepository(application);
        allBMarkers = markerRepository.getAllMarkers();
    }

    public void insert(BMarker BMarker) {
        markerRepository.insert(BMarker);
    }

    public List<BMarker> getMarkerListForWater(int water_id) {
        BMarkerList = markerRepository.getMarkerListForWater(water_id);
        return BMarkerList;
    }

    public List<BMarker> getAllMarkers() {
        BMarkerList = markerRepository.getAllMarkers();
        return allBMarkers;
    }
}
