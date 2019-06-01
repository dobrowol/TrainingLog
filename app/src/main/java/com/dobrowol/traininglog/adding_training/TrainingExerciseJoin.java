package com.dobrowol.traininglog.adding_training;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = {@Index("exerciseId"), @Index("trainingId")},
        tableName = "training_exercise_join",
        primaryKeys = { "exerciseId", "trainingId" },
        foreignKeys = {
                @ForeignKey(onDelete = CASCADE,
                        entity = Exercise.class,
                        parentColumns = "id",
                        childColumns = "exerciseId"),
                @ForeignKey(onDelete = CASCADE,
                        entity = Training.class,
                        parentColumns = "id",
                        childColumns = "trainingId")
        })

public class TrainingExerciseJoin {
    @NonNull
    public String exerciseId;
    @NonNull
    public String trainingId;
    public int order;

    public TrainingExerciseJoin(String exerciseId, String trainingId, int order) {
        this.exerciseId = exerciseId;
        this.trainingId = trainingId;
        this.order = order;
    }
}
