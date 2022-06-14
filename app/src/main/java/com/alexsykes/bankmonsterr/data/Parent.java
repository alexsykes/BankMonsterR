package com.alexsykes.bankmonsterr.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "parents")
public class Parent {

    @PrimaryKey(autoGenerate = true)
    private int parentId;

    @NonNull
    private String name;
    private String category;

    public Parent(@NonNull String name, @NonNull String category){
        this.category = category;
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
