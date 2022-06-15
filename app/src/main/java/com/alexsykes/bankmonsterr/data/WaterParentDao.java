package com.alexsykes.bankmonsterr.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WaterParentDao {
    @Query("SELECT waters.name as waterName, parents.name AS parentName " +
    "FROM waters, parents " +
    "WHERE waters.parentid = parents.id")
    public LiveData<List<WaterParent>> loadWaterAndParents();

    public class WaterParent {
        public String waterName, parentName;
    }
}
