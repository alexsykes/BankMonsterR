package com.alexsykes.bankmonsterr.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MarkerDao {
    // Marker Dao
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMarker(Marker marker);

    @Query("DELETE FROM markers")
    void deleteAllMarkers();

    @Query("SELECT * FROM markers ORDER by water_id ASC")
    LiveData<List<Marker>> getAllMarkers();

    @Query("SELECT * FROM markers WHERE water_id = :water_id")
    LiveData<List<Marker>> getAllMarkersForWater(int water_id);
}





