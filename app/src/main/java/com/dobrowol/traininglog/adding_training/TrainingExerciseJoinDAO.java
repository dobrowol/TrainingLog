package com.dobrowol.traininglog.adding_training;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

import java.util.List;

@Dao
public interface TrainingExerciseJoinDAO {

        @Insert
        void insert(TrainingExerciseJoin userRepoJoin);

        @Query("SELECT exercise_table.* FROM exercise_table INNER JOIN training_exercise_join ON exercise_table.id=training_exercise_join.exerciseId WHERE training_exercise_join.trainingId=:trainingId")
        LiveData<List<Exercise>> getExercisesForTraining(final String trainingId);

        @Query("SELECT training_table.* FROM training_table INNER JOIN training_exercise_join ON training_table.id=training_exercise_join.trainingId WHERE training_exercise_join.exerciseId=:exerciseId")
                List<Training> getTrainingsForExercise(final String exerciseId);

}
