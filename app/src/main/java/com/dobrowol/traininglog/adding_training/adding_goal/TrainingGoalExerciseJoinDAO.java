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

        @Query("SELECT exercise_table.* FROM exercise_table  INNER JOIN training_goal_exercise_join ON exercise_table.id=training_goal_exercise_join.exerciseId " +
                "WHERE training_goal_exercise_join.trainingJoinId=:trainingId AND training_goal_exercise_join.goalId = :goalId")
        LiveData<List<Exercise>> getExercisesForTrainingAndGoal(final String trainingId, final String goalId);

        @Query("SELECT goal_table.*, exercise_table.* FROM goal_table INNER JOIN training_goal_exercise_join ON goal_table.goalId=training_goal_exercise_join.goalId INNER JOIN exercise_table ON exercise_table.id=training_goal_exercise_join.exerciseId WHERE training_goal_exercise_join.trainingJoinId=:trainingId")
        LiveData<List<GoalExercisePair>> getGoalsAndExercisesForTraining(final String trainingId);
}
