package com.dobrowol.traininglog.adding_training.adding_goal;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

import java.util.List;

@Dao
public interface TrainingGoalExerciseJoinDAO {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(TrainingGoalExerciseJoin trainingGoalExerciseJoin);

        @Query("SELECT exercise_table.* FROM exercise_table INNER JOIN training_goal_exercise_join ON exercise_table.id=training_goal_exercise_join.exerciseId WHERE training_goal_exercise_join.goalId=:goalId")
        LiveData<List<Exercise>> getExercisesForGoal(final String goalId);

        @Query("SELECT goal_table.* FROM goal_table  INNER JOIN training_goal_exercise_join ON goal_table.id=training_goal_exercise_join.goalId WHERE training_goal_exercise_join.exerciseId=:exerciseId")
        LiveData<List<Goal>> getGoalsForExercise(final String exerciseId);

        @Query("SELECT exercise_table.* FROM exercise_table  INNER JOIN training_goal_exercise_join ON exercise_table.id=training_goal_exercise_join.exerciseId WHERE training_goal_exercise_join.trainingId=:trainingId AND training_goal_exercise_join.goalId=:goalId")
        LiveData<List<Exercise>> getExercisesForTrainingAndGoal(final String goalId, final String trainingId);


        @Query("SELECT DISTINCT goal_table.* FROM goal_table  INNER JOIN training_goal_exercise_join ON goal_table.id=training_goal_exercise_join.goalId WHERE training_goal_exercise_join.trainingId=:trainingId ")
        LiveData<List<Goal>> getUniqueGoalsForTraining(final String trainingId);



}
