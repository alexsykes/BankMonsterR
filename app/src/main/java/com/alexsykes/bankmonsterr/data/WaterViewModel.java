package com.alexsykes.bankmonsterr.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WaterViewModel extends AndroidViewModel {
    private final WaterRepository waterRepositry;

    private final LiveData<List<Water>> allWaters;
    private final LiveData<List<Water>> riverList;
    private final LiveData<List<WaterAndParents>> waterAndParentList;

    public WaterViewModel(@NonNull Application application) {
        super(application);
        waterRepositry = new WaterRepository(application);
        allWaters = waterRepositry.getAllWaters();
        riverList = waterRepositry.getRiverList();
        waterAndParentList = waterRepositry.getWaterAndParentList();
    }

    public LiveData<List<Water>> getAllWaters() {
        return allWaters;
    }
    public LiveData<List<Water>> getRiverList() {
        return riverList;
    }

    public LiveData<List<WaterAndParents>> getWaterAndParentList() { return waterAndParentList; }

    public void insert(Water water) {
        waterRepositry.insert(water);
    }
}
