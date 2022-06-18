package com.alexsykes.bankmonsterr.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "waters")
public class Water {

    public void setWater_id(int water_id) {
        this.water_id = water_id;
    }

    @PrimaryKey(autoGenerate = true)
    private int water_id;

    @NonNull
    private String name, type;

    private int parent_id;

    public int getWater_id() {
        return water_id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    @Ignore
    public Water(@NonNull String name, @NonNull String type, int parent_id) {
        this.name = name;
        this.type = type;
        this.parent_id = parent_id;
    }

    public Water(int water_id, @NonNull String name, @NonNull String type, int parent_id) {
        this.water_id = water_id;
        this.name = name;
        this.type = type;
        this.parent_id = parent_id;
    }
}

