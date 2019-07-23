package com.dobrowol.traininglog.adding_training.adding_goal;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = {@Index("goalId"), @Index("exerciseId")},
        tableName = "goal_exercise_join",
        foreignKeys = {
                @ForeignKey(onDelete = CASCADE,
                        entity = Goal.class,
                        parentColumns = "goalId",
                        childColumns = "goalId"),
                @ForeignKey(onDelete = CASCADE,
                        entity = Exercise.class,
                        parentColumns = "id",
                        childColumns = "exerciseId")
        })

public class GoalExercise {
    @NonNull
    @PrimaryKey
    String id;
    public String goalId;
    public String exerciseId;
    int specificity;

    public GoalExercise(@NonNull String id, String goalId, String exerciseId, int specificity){
        this.id = id;
        this.goalId = goalId;
        this.exerciseId = exerciseId;
        this.specificity = specificity;
    }
}
