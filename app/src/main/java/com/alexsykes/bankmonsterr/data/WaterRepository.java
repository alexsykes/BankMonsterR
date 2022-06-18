package com.alexsykes.bankmonsterr.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class WaterRepository {
    private final AllDao dao;
    private final LiveData<List<Water>> allWaters;
    private final LiveData<List<Water>> riverList;
    private final LiveData<List<WaterAndParents>> waterAndParentList;

    WaterRepository(Application application) {
        WaterRoomDatabase db = WaterRoomDatabase.getDatabase(application);
        dao = db.dao();
        allWaters = dao.getAllWaters();
        riverList = dao.getAllWatersList("Canal");
        waterAndParentList = dao.WaterAndParentList();
    }

    LiveData<List<Water>> getAllWaters() {
        return allWaters;
    }

    LiveData<List<Water>> getRiverList() {
        return riverList;
    }

    LiveData<List<WaterAndParents>> getWaterAndParentList() { return waterAndParentList; }

    void insert(Water water) {
        WaterRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.insertWater(water);
        });
    }
}
