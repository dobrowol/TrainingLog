package com.dobrowol.traininglog.adding_training.adding_goal;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GoalDAO {
    @Query("SELECT * FROM goal_table")
    LiveData<List<Goal>> getAll();

    @Query("SELECT * FROM goal_table WHERE goalId IN (:eIds)")
    List<Goal> loadAllByIds(int[] eIds);

    @Query("SELECT * FROM goal_table WHERE description=:eDescription LIMIT 1")
    LiveData<Goal> getByDescription(String eDescription);

    @Insert
    long insert(Goal goal);

    @Delete
    void delete(Goal goal);

    @Update
    void update(Goal goal);

    @Query("SELECT * FROM goal_table WHERE goalId=:goalId")
    LiveData<Goal> getById(String goalId);
}

