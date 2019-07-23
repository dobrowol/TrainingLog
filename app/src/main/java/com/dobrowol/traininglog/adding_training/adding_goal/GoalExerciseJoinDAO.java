package com.dobrowol.traininglog.adding_training.adding_goal;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

import java.util.List;

@Dao
public interface GoalExerciseJoinDAO {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(GoalExercise goalExercise);

        @Query("SELECT goal_table.* FROM goal_table  INNER JOIN goal_exercise_join ON goal_table.goalId=goal_exercise_join.goalId WHERE goal_exercise_join.exerciseId=:exerciseId ")
        LiveData<List<Goal>> getGoalsForExercise(final String exerciseId);

        @Query("SELECT exercise_table.* FROM exercise_table  INNER JOIN goal_exercise_join ON exercise_table.id=goal_exercise_join.exerciseId WHERE goal_exercise_join.goalId=:goalId ")
        LiveData<List<Exercise>> getExercisesForGoal(final String goalId);

}
