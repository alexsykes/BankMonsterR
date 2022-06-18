package com.alexsykes.bankmonsterr.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class WaterRepository {
    private final WaterDao waterDao;
    private final LiveData<List<Water>> allWaters;
    private final LiveData<List<Water>> riverList;
    private final LiveData<List<WaterAndParents>> waterAndParentList;

    WaterRepository(Application application) {
        WaterRoomDatabase db = WaterRoomDatabase.getDatabase(application);
        waterDao = db.wdao();
        allWaters = waterDao.getAllWaters();
        riverList = waterDao.getAllWatersList("Canal");
        waterAndParentList = waterDao.WaterAndParentList();
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
            waterDao.insertWater(water);
        });
    }
}
