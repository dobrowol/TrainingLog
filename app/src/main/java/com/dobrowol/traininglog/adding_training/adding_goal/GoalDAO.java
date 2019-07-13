package com.dobrowol.traininglog.adding_training.adding_goal;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseDescription;

import java.util.List;

@Dao
public interface GoalDAO {
    @Query("SELECT * FROM goal_table")
    LiveData<List<Goal>> getAll();

    @Query("SELECT * FROM goal_table WHERE id IN (:eIds)")
    List<Goal> loadAllByIds(int[] eIds);

    @Insert
    void insert(Goal exercise);

    @Delete
    void delete(Goal exercise);

    @Update
    void update(Goal exercise);
}

