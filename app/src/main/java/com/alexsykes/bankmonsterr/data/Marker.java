package com.alexsykes.bankmonsterr.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "markers")
public class Marker {

    public void setMarker_id(int marker_id) {
        this.marker_id = marker_id;
    }

    @PrimaryKey(autoGenerate = true)
    private int marker_id;

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    private String  name, code, type;
    private int water_id;
    private double lat, lng;

    public int getMarker_id() {
        return marker_id;
    }

    public Marker(@NonNull String name, String code, @NonNull String type, int water_id, double lng, double lat) {
        this.code = code;
        this.type = type;
        this.water_id = water_id;
        this.lat = lat;
        this.lng = lng;
        this.name = name;
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
}
