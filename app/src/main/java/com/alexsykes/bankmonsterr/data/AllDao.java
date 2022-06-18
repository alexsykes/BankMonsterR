package com.alexsykes.bankmonsterr.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface AllDao {
    // Marker Dao
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMarker(Marker marker);

    @Query("DELETE FROM markers")
    void deleteAllMarkers();

    @Query("SELECT * FROM markers ORDER by name ASC")
    LiveData<List<Marker>> getAllMarkers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertParent(Parent parent);

    @Query("SELECT * FROM parents ORDER by name ASC")
    LiveData<List<Parent>> getAllParents();

    @Query("DELETE FROM parents")
    void deleteAllParents();

    @Transaction
    @Query("SELECT * FROM parents")
    List<ParentWithWaters> getParentsWithWaterLists();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWater(Water water);

    @Query("DELETE FROM waters")
    void deleteAllWaters();

    @Query("SELECT * FROM waters ORDER by parent_id, name ASC")
    LiveData<List<Water>> getAllWaters();

    @Query("SELECT * FROM waters " +
            " WHERE type LIKE :category " +
            "ORDER by name ASC ")
    LiveData<List<Water>> getAllWatersList(String category);
    @Transaction
    @Query("SELECT * FROM waters")
    List<WaterAndParent> getWatersAndParent();

    @Query("SELECT waters.water_id, waters.name AS water, parents.name AS parent " +
            "FROM waters, parents " +
            "WHERE waters.parent_id = parents.parent_id " +
            "ORDER BY parent, water ASC")
    LiveData<List<WaterAndParents>> WaterAndParentList();

}





