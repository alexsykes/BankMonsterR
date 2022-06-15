package com.alexsykes.bankmonsterr.data;


import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ParentAndWaters {
    @Embedded
    public Parent parent;

    @Relation(
            parentColumn = "parent.id",
            entityColumn = "waters.parentid"
    )

    public List<Water> waters;

    public ParentAndWaters(Parent parent, List<Water> waters) {
        this.parent = parent;
        this.waters = waters;
    }
}
