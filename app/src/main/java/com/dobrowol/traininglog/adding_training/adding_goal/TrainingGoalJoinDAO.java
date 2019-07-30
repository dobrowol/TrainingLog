package com.dobrowol.traininglog.adding_training.adding_goal;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.dobrowol.traininglog.adding_training.Training;

import java.util.List;

@Dao
public interface TrainingGoalJoinDAO {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        long insert(TrainingGoalJoin trainingGoalJoin);

        @Query("SELECT goal_table.* FROM goal_table  INNER JOIN training_goal_join ON goal_table.goalId=training_goal_join.goalId WHERE training_goal_join.trainingId=:trainingId ")
        LiveData<List<Goal>> getGoalsForTraining(final String trainingId);

        @Query("SELECT training_table.* FROM training_table  INNER JOIN training_goal_join ON training_table.id=training_goal_join.trainingId WHERE training_goal_join.goalId=:goalId ")
        LiveData<List<Training>> getTrainingsForGoal(final String goalId);

        @Update(onConflict = OnConflictStrategy.REPLACE)
        void update(TrainingGoalJoin param);

        @Query("SELECT training_goal_join.* FROM training_goal_join WHERE training_goal_join.trainingId=:trainingId")
        LiveData<List<TrainingGoalJoin>> getTrainingGoalsForTrainingId(String trainingId);

        @Query("SELECT training_goal_join.* FROM training_goal_join")
        LiveData<List<TrainingGoalJoin>> getAllTrainingGoals();

        @Query("SELECT training_goal_join.* FROM training_goal_join WHERE training_goal_join.trainingId=:trainingId AND training_goal_join.goalId=:goalId")
        LiveData<TrainingGoalJoin> getTrainingGoal(String trainingId, String goalId);

        @Query("SELECT MAX(training_goal_join.load) FROM training_goal_join")
        LiveData<Integer> getMaximumLoad();
}
