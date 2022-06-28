package com.alexsykes.bankmonsterr.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "markers")
public class BMarker {

    public void setMarker_id(int marker_id) {
        this.marker_id = marker_id;
    }

    @PrimaryKey(autoGenerate = true)
    private int marker_id;


    @NonNull
    private final String name;
    @NonNull
    private String code;
    @NonNull
    private String type;
    private int water_id;
    private double lat, lng;
    //@ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    //String created;
    //String updated;
    boolean isUpdated, isNew;

    public int getMarker_id() {
        return marker_id;
    }

    public BMarker(int marker_id, @NonNull String name, @NonNull String code, @NonNull String type, int water_id, double lat, double lng) {
        if (marker_id != -999) {
            this.marker_id = marker_id;
        }
        this.name = name;
        this.code = code;
        this.type = type;
        this.water_id = water_id;
        this.lat = lat;
        this.lng = lng;
    }


    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getCode() {
        return code;
    }

    public void setCode(@NonNull String code) {
        this.code = code;
    }
    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public int getWater_id() {
        return water_id;
    }

    public void setWater_id(int water_id) {
        this.water_id = water_id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public boolean isNew() {
        return isNew;
    }
}
