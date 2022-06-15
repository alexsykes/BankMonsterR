package com.alexsykes.bankmonsterr.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MarkerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Marker marker);

    @Query("DELETE FROM markers")
    void deleteAll();

    @Query("SELECT * FROM markers ORDER by name ASC")
    LiveData<List<Marker>> getAllMarkers();
}
