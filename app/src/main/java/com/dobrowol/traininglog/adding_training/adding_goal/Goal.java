package com.dobrowol.traininglog.adding_training.adding_goal;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "goal_table")
public class Goal {
    @PrimaryKey
    @NonNull
    public String id;

    public String description;

    public int priority;

    private ArrayList<Integer> vector;
}
