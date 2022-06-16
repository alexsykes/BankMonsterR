package com.alexsykes.bankmonsterr.data;


import androidx.room.Embedded;
import androidx.room.Query;
import androidx.room.Relation;
import androidx.room.Transaction;

import java.util.List;

public class ParentWithWaters {
    @Embedded
    public Parent parent;

    @Relation(
            parentColumn = "parent_id",
            entityColumn = "parent_id"
    )

    public List<Water> waters;

    public ParentWithWaters(Parent parent, List<Water> waters) {
        this.parent = parent;
        this.waters = waters;
    }
}