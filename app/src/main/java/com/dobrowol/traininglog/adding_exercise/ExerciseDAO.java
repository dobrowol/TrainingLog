package com.dobrowol.traininglog.adding_exercise;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExerciseDAO {
    @Query("SELECT * FROM exercise_table")
    LiveData<List<Exercise>> getAll();

    @Query("SELECT * FROM exercise_table WHERE id IN (:eIds)")
    List<Exercise> loadAllByIds(int[] eIds);


    @Query("SELECT * FROM exercise_description_table WHERE eid LIKE :exerciseDescriptionId LIMIT 1")
    ExerciseDescription findExerciseDescriptonById( Integer exerciseDescriptionId);

    @Query("SELECT * FROM exercise_description_table WHERE description LIKE :exerciseDescription LIMIT 1")
    LiveData<ExerciseDescription> findExerciseDescriptonByDescription( String exerciseDescription);

    @Insert
    void insert(Exercise exercise);

    @Delete
    void delete(Exercise exercise);
}

