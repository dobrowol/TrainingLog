package com.dobrowol.traininglog.adding_training.adding_goal;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = {@Index("goalId"), @Index("exerciseId")},
        tableName = "goal_exercise_join",
        foreignKeys = {
                @ForeignKey(onDelete = CASCADE,
                        entity = Goal.class,
                        parentColumns = "id",
                        childColumns = "goalId"),
                @ForeignKey(onDelete = CASCADE,
                        entity = Exercise.class,
                        parentColumns = "id",
                        childColumns = "exerciseId")
        })

public class GoalExercise {
    String id;
    String goalId;
    String exerciseId;
    int specificity;
}
