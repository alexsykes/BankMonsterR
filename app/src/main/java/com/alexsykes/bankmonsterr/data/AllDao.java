package com.alexsykes.bankmonsterr.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;
import java.util.Map;

@Dao
public interface AllDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMarker(Marker marker);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWater(Water water);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertParent(Parent parent);



    @Query("SELECT * FROM markers ORDER by name ASC")
    LiveData<List<Marker>> getAllMarkers();

    @Query("SELECT * FROM parents ORDER by name ASC")
    LiveData<List<Parent>> getAllParents();

    @Query("SELECT * FROM waters ORDER by parent_id, name ASC")
    LiveData<List<Water>> getAllWaters();

    @Query("DELETE FROM markers")
    void deleteAllMarkers();

    @Query("DELETE FROM waters")
    void deleteAllWaters();
    @Query("DELETE FROM parents")
    void deleteAllParents();

    @Query("SELECT waters.name as waterName, parents.name AS parentName " +
            "FROM waters, parents " +
            "WHERE waters.parent_id = parents.parent_id")
    public LiveData<List<WaterParent>> loadWaterAndParents();

    @Transaction
    @Query("SELECT * FROM parents")
    public  List<ParentWithWaters> getParentsWithWaterLists();
}
