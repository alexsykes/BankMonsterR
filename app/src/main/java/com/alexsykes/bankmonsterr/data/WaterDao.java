package com.alexsykes.bankmonsterr.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WaterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Water water);

    @Query("DELETE FROM waters")
    void deleteAll();

    @Query("SELECT * FROM waters ORDER by parentid, name ASC")
    LiveData<List<Water>> getAllWaters();
}
