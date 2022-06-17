package com.alexsykes.bankmonsterr.data;

public class WaterAndParents {
    public int water_id;
    public String water;
    public String parent;


    public WaterAndParents(int water_id, String water, String parent) {
        this.water_id = water_id;
        this.water = water;
        this.parent = parent;
    }

    public int getWater_id() {
        return water_id;
    }

    public void setWater_id(int water_id) {
        this.water_id = water_id;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}

