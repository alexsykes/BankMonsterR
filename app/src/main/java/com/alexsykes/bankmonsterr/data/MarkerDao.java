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
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMarker(BMarker BMarker);

    @Query("DELETE FROM markers")
    void deleteAllMarkers();

    @Query("SELECT * FROM markers ORDER by water_id ASC")
    List<BMarker> getAllMarkers();

    @Query("SELECT * FROM markers WHERE water_id = :water_id")
    LiveData<List<BMarker>> getAllMarkersForWater(int water_id);

    @Query("SELECT * FROM markers WHERE water_id = :water_id ORDER BY name ASC")
    List<BMarker> getAllMarkerList(int water_id);
}