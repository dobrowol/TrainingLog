package com.dobrowol.traininglog.adding_training.adding_goal;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "sub-goal_table",
        foreignKeys = {
        @ForeignKey(onDelete = CASCADE,
                entity = Goal.class,
                parentColumns = "id",
                childColumns = "goalId")})
public class Subgoal {
    @PrimaryKey
    @NonNull
    public String id;

    public String goalId;

    private ArrayList<Integer> vector;
}
