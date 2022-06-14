package com.alexsykes.bankmonsterr.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ParentRepository {
    private ParentDao parentDao;
    private LiveData<List<Parent>> allParents;

    ParentRepository(Application application) {
        WaterRoomDatabase db = WaterRoomDatabase.getDatabase(application);
        parentDao = db.parentDao();
        allParents = parentDao.getAllParents();
    }

    LiveData<List<Parent>> getAllParents() {
        return allParents;
    }

    void insert(Parent parent) {
        WaterRoomDatabase.databaseWriteExecutor.execute(() -> {
            parentDao.insert(parent);
        });
    }
}
