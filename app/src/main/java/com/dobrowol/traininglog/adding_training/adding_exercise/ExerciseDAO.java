package com.dobrowol.traininglog.adding_training.adding_exercise;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExerciseDAO {
    @Query("SELECT * FROM exercise_table")
    LiveData<List<Exercise>> getAll();

    @Query("SELECT * FROM exercise_table WHERE id IN (:eIds)")
    List<Exercise> loadAllByIds(int[] eIds);


    @Query("SELECT * FROM exercise_description_table WHERE description LIKE :exerciseDescription LIMIT 1")
    LiveData<ExerciseDescription> findExerciseDescriptionByDescription(String exerciseDescription);

    @Insert
    long insert(Exercise exercise);

    @Delete
    void delete(Exercise exercise);

    @Update
    void update(Exercise exercise);

    @Query("SELECT * FROM exercise_table WHERE id IN (:id)")
    LiveData<Exercise> getExerciseById(String id);

    @Query("SELECT MAX(loadValue) FROM exercise_table")
    LiveData<Integer> getMaximumLoad();
}

