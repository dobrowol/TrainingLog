package com.dobrowol.traininglog.adding_training.adding_exercise;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExerciseDescriptionDAO {
    @Query("SELECT * FROM exercise_description_table")
    LiveData<List<ExerciseDescription>> getAll();

    @Query("SELECT * FROM exercise_description_table WHERE eid IN (:eIds)")
    List<ExerciseDescription> loadAllByIds(int[] eIds);

    @Query("SELECT * FROM exercise_description_table WHERE description LIKE :exerciseDescription LIMIT 1")
    LiveData<ExerciseDescription> findByDescription( String exerciseDescription);

    @Insert
    void insert(ExerciseDescription exerciseDescription);

    @Delete
    void delete(ExerciseDescription exerciseDescription);

    @Query("SELECT * FROM exercise_description_table WHERE eid=:id")
    LiveData<ExerciseDescription> findById(String id);
}
