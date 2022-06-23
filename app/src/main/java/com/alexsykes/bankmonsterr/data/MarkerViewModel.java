package com.alexsykes.bankmonsterr.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

public class MarkerViewModel extends AndroidViewModel {
    private final MarkerRepository markerRepository;

    private final List<Marker> allMarkers;
    private List<Marker> markerList;

    public MarkerViewModel(@NonNull Application application) {
        super(application);
        markerRepository = new MarkerRepository(application);
        allMarkers = markerRepository.getAllMarkers();
    }

    public void insert(Marker marker) {
        markerRepository.insert(marker);
    }

    public List<Marker> getMarkerList(int water_id) {
        markerList = markerRepository.getMarkerList(water_id);
        return markerList;
    }

    public List<Marker> getAllMarkers() {
        markerList = markerRepository.getAllMarkers();
        return allMarkers;
    }
}
