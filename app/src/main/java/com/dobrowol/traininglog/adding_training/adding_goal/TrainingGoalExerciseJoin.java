package com.dobrowol.traininglog.adding_training.adding_goal;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = {@Index("trainingId"), @Index("goalId"), @Index("exerciseId")},
        tableName = "training_goal_exercise_join",
        foreignKeys = {
                @ForeignKey(onDelete = CASCADE,
                        entity = Training.class,
                        parentColumns = "id",
                        childColumns = "trainingId"),
                @ForeignKey(onDelete = CASCADE,
                        entity = Goal.class,
                        parentColumns = "id",
                        childColumns = "goalId"),
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
    public String trainingId;
    @NonNull
    public String goalId;
    @NonNull
    public String exerciseId;
    public int order;

    public TrainingGoalExerciseJoin(String id, String trainingId, String goalId, String exerciseId, int order) {
        this.id = id;
        this.trainingId = trainingId;
        this.goalId = goalId;
        this.exerciseId = exerciseId;
        this.order = order;
    }
}
