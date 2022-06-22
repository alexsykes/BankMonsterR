package com.alexsykes.bankmonsterr.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "parents")
public class Parent {

    @PrimaryKey(autoGenerate = true)
    private int parent_id;

    @NonNull
    private final String name;
    private final String category;

    @Ignore
    public Parent(@NonNull String name, @NonNull String category){
        this.category = category;
        this.name = name;
    }

    public Parent(int parent_id, @NonNull String name, String category) {
        this.parent_id = parent_id;
        this.name = name;
        this.category = category;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getParent_id() {
        return parent_id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setParentId(int parentId) {
        this.parent_id = parentId;
    }
}
