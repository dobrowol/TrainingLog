package com.dobrowol.traininglog.adding_training.adding_goal;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = {
        @Index(value = {"trainingId","goal"}, unique = true)},
        tableName = "training_goal_join",
        foreignKeys = {
                @ForeignKey(onDelete = CASCADE,
                        entity = Training.class,
                        parentColumns = "id",
                        childColumns = "trainingId")
        })

public class TrainingGoalJoin {
    @PrimaryKey
    @NonNull
    public String id;
    @NonNull
    public String trainingId;
    @NonNull
    public String goal;
    public int load;


    public TrainingGoalJoin(String id, String trainingId, String goal, Integer load){
        this.id = id;
        this.trainingId = trainingId;
        this.goal = goal;
        this.load = load;
    }
}
