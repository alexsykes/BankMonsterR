package com.alexsykes.bankmonsterr.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WaterViewModel extends AndroidViewModel {
    private WaterRepository waterRepositry;

    private final LiveData<List<Water>> allWaters;

    public WaterViewModel(@NonNull Application application) {
        super(application);
        waterRepositry = new WaterRepository(application);
        allWaters = waterRepositry.getAllWaters();
    }

    public LiveData<List<Water>> getAllWaters() {
        return allWaters;
    }

    public void insert(Water water) {
        waterRepositry.insert(water);
    }
}
