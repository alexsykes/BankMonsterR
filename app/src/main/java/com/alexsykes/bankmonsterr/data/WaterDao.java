package com.alexsykes.bankmonsterr.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WaterDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertWater(Water water);

    @Query("DELETE FROM waters")
    void deleteAllWaters();

    @Query("SELECT * FROM waters ORDER by parent_id, name ASC")
    LiveData<List<Water>> getAllWaters();

    @Query("SELECT * FROM waters " +
            " WHERE type LIKE :category " +
            "ORDER by name ASC ")
    LiveData<List<Water>> getAllWatersList(String category);

    @Query("SELECT waters.water_id, waters.name AS water, parents.name AS parent " +
            "FROM waters, parents " +
            "WHERE waters.parent_id = parents.parent_id " +
            "ORDER BY water ASC")
    LiveData<List<WaterAndParents>> WaterAndParentList();
}
