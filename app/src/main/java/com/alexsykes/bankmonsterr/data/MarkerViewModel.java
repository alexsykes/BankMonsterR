package com.alexsykes.bankmonsterr.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MarkerViewModel extends AndroidViewModel {
    private final MarkerRepository markerRepository;

    private final LiveData<List<Marker>> allMarkers;
    private List<Marker> markerList;

    public MarkerViewModel(@NonNull Application application) {
        super(application);
        markerRepository = new MarkerRepository(application);
        allMarkers = markerRepository.getAllMarkers();
    }

    public LiveData<List<Marker>> getAllParents() {
        return allMarkers;
    }

    public void insert(Marker marker) {
        markerRepository.insert(marker);
    }

    public List<Marker> getMarkerList(int water_id) {
        markerList = markerRepository.getMarkerList(water_id);
        return markerList;
    }
}
