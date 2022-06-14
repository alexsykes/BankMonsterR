package com.alexsykes.bankmonsterr.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ParentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Parent parent);

    @Query("DELETE FROM parents")
    void deleteAll();

    @Query("SELECT * FROM parents ORDER by name ASC")
    LiveData<List<Parent>> getAllParents();
}
