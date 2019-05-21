package com.dobrowol.traininglog.adding_training.adding_exercise;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "exercise_description_table")
public class ExerciseDescription implements Serializable {
    @PrimaryKey
    @NonNull
    public String eid;

    @NonNull
    @ColumnInfo(name = "description")
    public String description;

    @NonNull
    @ColumnInfo(name = "specificity")
    public Specificity specificity;
}
