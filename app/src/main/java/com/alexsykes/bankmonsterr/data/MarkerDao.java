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
    void insertMarker(BMarker BMarker);

    @Query("DELETE FROM markers")
    void deleteAllMarkers();

    @Query("SELECT * FROM markers ORDER by water_id ASC")
    List<BMarker> getAllMarkers();

    @Query("SELECT * FROM markers WHERE water_id = :water_id")
    LiveData<List<BMarker>> getAllMarkersForWater(int water_id);

    @Query("SELECT * FROM markers WHERE water_id = :water_id ORDER BY name ASC")
    List<BMarker> getAllMarkerList(int water_id);

    @Query("UPDATE markers SET lat = :lat, lng = :lng, isUpdated = :isUpdated WHERE marker_id = :marker_id")
    void updateMarker(int marker_id, double lat, double lng, boolean isUpdated);

    @Query("SELECT * FROM markers WHERE isUpdated = 1 OR isNew = 1")
    List<BMarker> uploadChangesToServer();
}