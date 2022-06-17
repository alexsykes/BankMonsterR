package com.alexsykes.bankmonsterr.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class WaterRepository {
    private AllDao dao;
    private LiveData<List<Water>> allWaters;
    private List<Water> allWatersList;

    WaterRepository(Application application) {
        WaterRoomDatabase db = WaterRoomDatabase.getDatabase(application);
        dao = db.dao();
        allWaters = dao.getAllWaters();
        allWatersList = dao.getAllWatersList();
    }

    LiveData<List<Water>> getAllWaters() {
        return allWaters;
    }

    List<Water> getAllWatersList() {
        return allWatersList;
    }

    void insert(Water water) {
        WaterRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.insertWater(water);
        });
    }
}
