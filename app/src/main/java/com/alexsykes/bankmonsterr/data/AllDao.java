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

    @Query("SELECT * FROM waters " +
            " WHERE type LIKE :category " +
            "ORDER by name ASC ")
    LiveData<List<Water>> getAllWatersList(String category);

    @Query("DELETE FROM markers")
    void deleteAllMarkers();

    @Query("DELETE FROM waters")
    void deleteAllWaters();
    @Query("DELETE FROM parents")
    void deleteAllParents();
//
//    @Query("SELECT waters.water_id as water_id, waters.name as waterName, parents.name AS parentName " +
//            "FROM waters, parents " +
//            "WHERE waters.parent_id = parents.parent_id")
//    LiveData<List<WaterParent>> loadWaterAndParents();

    @Transaction
    @Query("SELECT * FROM parents")
    List<ParentWithWaters> getParentsWithWaterLists();

    @Transaction
    @Query("SELECT * FROM waters")
    List<WaterAndParent> getWatersAndParent();


    @Query("SELECT waters.water_id, waters.name AS water, parents.name AS parent " +
            "FROM waters, parents " +
            "WHERE waters.parent_id = parents.parent_id " +
            "ORDER BY parent, water ASC")
    public LiveData<List<WaterAndParents>> WaterAndParentList();

    // You can also define this class in a separate file, as long as you add the
    // "public" access modifier.

}





