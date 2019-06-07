package com.dobrowol.traininglog.adding_training;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TrainingDAO {
    @Query("SELECT * FROM training_table")
    LiveData<List<Training>> getAll();

    @Query("SELECT * FROM training_table WHERE id IN (:ids)")
    List<Training> loadAllByIds(int[] ids);

    @Query("SELECT general_load FROM training_table")
    LiveData<List<Integer>> getAllGeneralLoads();

    @Query("SELECT specific_load FROM training_table")
    LiveData<List<Integer>> getAllSpecificLoads();

    @Query("SELECT competitive_load FROM training_table")
    LiveData<List<Integer>> getAllCompetitiveLoads();
    @Insert
    void insert(Training exercise);

    @Delete
    void delete(Training exercise);

    @Query("SELECT * FROM training_table WHERE date=Date(:date)")
    LiveData<List<Training>> findTrainingByDate(String date);

    @Update
    void updateTask(Training training);

    @Query("SELECT * FROM training_table WHERE id=:trainingId")
    LiveData<Training> findTrainingById(String trainingId);
}

