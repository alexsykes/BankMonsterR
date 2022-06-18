package com.alexsykes.bankmonsterr.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ParentRepository {
    private final AllDao dao;
    private final LiveData<List<Parent>> allParents;

    ParentRepository(Application application) {
        WaterRoomDatabase db = WaterRoomDatabase.getDatabase(application);
        dao = db.dao();
        allParents = dao.getAllParents();
    }

    LiveData<List<Parent>> getAllParents() {
        return allParents;
    }

    void insert(Parent parent) {
        WaterRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.insertParent(parent);
        });
    }
}
