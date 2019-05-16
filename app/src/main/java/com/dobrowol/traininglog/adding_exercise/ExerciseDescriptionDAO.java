package com.dobrowol.traininglog.adding_exercise;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ExerciseDescriptionDAO {
    @Query("SELECT * FROM exercise_description_table")
    LiveData<List<ExerciseDescription>> getAll();

    @Query("SELECT * FROM exercise_description_table WHERE eid IN (:eIds)")
    List<ExerciseDescription> loadAllByIds(int[] eIds);

    @Query("SELECT * FROM exercise_description_table WHERE description LIKE :description AND " +
            "specificity LIKE :specificity LIMIT 1")
    ExerciseDescription findByName(String description, Integer specificity);

    @Query("SELECT * FROM exercise_description_table WHERE description LIKE :exerciseDescription LIMIT 1")
    LiveData<ExerciseDescription> findByDescription( String exerciseDescription);

    @Insert
    void insert(ExerciseDescription exerciseDescription);

    @Delete
    void delete(ExerciseDescription exerciseDescription);
}
