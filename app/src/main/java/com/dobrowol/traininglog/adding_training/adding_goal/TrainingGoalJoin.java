package com.dobrowol.traininglog.adding_training.adding_goal;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = {@Index("trainingId"), @Index("goalId")},
        tableName = "training_goal_join",
        foreignKeys = {
                @ForeignKey(onDelete = CASCADE,
                        entity = Training.class,
                        parentColumns = "id",
                        childColumns = "trainingId"),
                @ForeignKey(onDelete = CASCADE,
                        entity = Goal.class,
                        parentColumns = "id",
                        childColumns = "goalId")
        })

public class TrainingGoalJoin {
    @PrimaryKey
    @NonNull
    public String id;
    @NonNull
    public String trainingId;
    @NonNull
    public String goalId;
    public int load;

    public TrainingGoalJoin(String id, String trainingId, String goalId) {
        this.id = id;
        this.trainingId = trainingId;
        this.goalId = goalId;
        load = 0;

    }
}
