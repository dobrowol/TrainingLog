package com.dobrowol.traininglog.adding_training.adding_goal;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.training_load.calculating.TrainingGoalLoadData;

import java.util.List;

@Dao
public interface TrainingGoalExerciseJoinDAO {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        long insert(TrainingGoalExerciseJoin trainingGoalExerciseJoin);

        @Query("SELECT exercise_table.* FROM exercise_table  INNER JOIN training_goal_exercise_join ON exercise_table.id=training_goal_exercise_join.exerciseId " +
                "WHERE training_goal_exercise_join.trainingJoinId=:trainingId AND training_goal_exercise_join.goalId = :goalId")
        LiveData<List<Exercise>> getExercisesForTrainingAndGoal(final String trainingId, final String goalId);

        @Query("SELECT goal_table.*, exercise_table.* FROM goal_table INNER JOIN training_goal_exercise_join ON goal_table.goalId=training_goal_exercise_join.goalId INNER JOIN exercise_table ON exercise_table.id=training_goal_exercise_join.exerciseId WHERE training_goal_exercise_join.trainingJoinId=:trainingId")
        LiveData<List<GoalExercisePair>> getGoalsAndExercisesForTraining(final String trainingId);

        @Query("SELECT  training_goal_exercise_join.trainingJoinId, training_goal_exercise_join.goalId, loadValue, exercise_table.startDate, training_table.date, goal_exercise_join.specificity FROM TRAINING_GOAL_EXERCISE_JOIN "+
                "INNER JOIN exercise_table ON training_goal_exercise_join.exerciseId=exercise_table.id "+
                "INNER JOIN goal_exercise_join ON goal_exercise_join.goalId=training_goal_exercise_join.goalId AND goal_exercise_join.exerciseId=training_goal_exercise_join.exerciseId "+
                "INNER JOIN training_table ON training_table.id == training_goal_exercise_join.trainingJoinId " +
                "WHERE training_goal_exercise_join.trainingJoinId=:trainingId AND" +
                " training_goal_exercise_join.goalId=:goalId")
        LiveData<List<TrainingGoalLoadData>> getTrainingGoalLoadData(String trainingId, String goalId);

        @Query("SELECT training_goal_exercise_join.* FROM training_goal_exercise_join WHERE training_goal_exercise_join.id=:id")
        LiveData<TrainingGoalExerciseJoin> getTrainingGoalExercise(String id);
}
