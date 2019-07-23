package com.dobrowol.traininglog.adding_training.adding_goal;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = {@Index("exerciseId"), @Index("goalId")},
        tableName = "training_goal_exercise_join",
        foreignKeys = {
                @ForeignKey(onDelete = CASCADE,
                        entity = TrainingGoalJoin.class,
                        parentColumns = "id",
                        childColumns = "trainingJoinId"),
                @ForeignKey(onDelete = CASCADE,
                        entity = Goal.class,
                        parentColumns = "goalId",
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
    public String trainingJoinId;
    @NonNull
    public String goalId;
    @NonNull
    public String exerciseId;
    public int order;

    public TrainingGoalExerciseJoin(@NonNull String id, @NonNull String trainingJoinId, @NonNull String goalId, @NonNull String exerciseId, int order) {
        this.id = id;
        this.trainingJoinId = trainingJoinId;
        this.goalId = goalId;
        this.exerciseId = exerciseId;
        this.order = order;
    }
}
