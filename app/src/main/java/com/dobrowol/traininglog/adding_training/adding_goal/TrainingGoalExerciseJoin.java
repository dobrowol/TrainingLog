package com.dobrowol.traininglog.adding_training.adding_goal;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = {@Index("trainingGoalId"), @Index("exerciseId")},
        tableName = "training_goal_exercise_join",
        foreignKeys = {
                @ForeignKey(onDelete = CASCADE,
                        entity = TrainingGoalJoin.class,
                        parentColumns = "id",
                        childColumns = "trainingGoalId"),
                @ForeignKey(onDelete = CASCADE,
                        entity = Exercise.class,
                        parentColumns = "id",
                        childColumns = "exerciseId")
        })

public class TrainingGoalExerciseJoin {
    @PrimaryKey
    @NonNull
    public String id;
    @NonNull
    public String trainingGoalId;
    @NonNull
    public String exerciseId;
    public int order;

    public TrainingGoalExerciseJoin(@NonNull String id, @NonNull String trainingGoalId, @NonNull String exerciseId, int order) {
        this.id = id;
        this.trainingGoalId = trainingGoalId;
        this.exerciseId = exerciseId;
        this.order = order;
    }
}
