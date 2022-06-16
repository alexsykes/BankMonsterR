package com.alexsykes.bankmonsterr.data;

import androidx.room.Embedded;
import androidx.room.Relation;

public class WaterAndParent {
    @Embedded
    public Water water;
    @Relation(
            parentColumn = "parent_id",
            entityColumn = "parent_id"
    )
    public Parent parent;
}
