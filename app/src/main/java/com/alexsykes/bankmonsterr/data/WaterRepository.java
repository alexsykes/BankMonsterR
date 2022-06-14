package com.alexsykes.bankmonsterr.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class WaterRepository {
    private WaterDao waterDao;
    private LiveData<List<Water>> allWaters;

    WaterRepository(Application application) {
        WaterRoomDatabase db = WaterRoomDatabase.getDatabase(application);
        waterDao = db.waterDao();
        allWaters = waterDao.getAllWaters();
    }

    LiveData<List<Water>> getAllWaters() {
        return allWaters;
    }

    void insert(Water water) {
        WaterRoomDatabase.databaseWriteExecutor.execute(() -> {
            waterDao.insert(water);
        });
    }
}
