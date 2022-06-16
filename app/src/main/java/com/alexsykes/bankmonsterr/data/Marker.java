package com.alexsykes.bankmonsterr.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "markers")
public class Marker {

    @PrimaryKey(autoGenerate = true)
    private int marker_id;

    @NonNull
    public String getCode() {
        return code;
    }

    public void setCode(@NonNull String code) {
        this.code = code;
    }

    @NonNull
    private String name, code, type;

    @NonNull
    private double lat, lng;

    public Marker(@NonNull String name, @NonNull String code, @NonNull String type, double lat, double lng) {
        this.name = name;
        this.code = code;
        this.type = type;
        this.lat = lat;
        this.lng = lng;
    }

    public int getMarker_id() {
        return marker_id;
    }

    public void setMarker_id(int marker_id) {
        this.marker_id = marker_id;
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

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }
}
