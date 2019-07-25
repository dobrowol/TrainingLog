package com.dobrowol.traininglog.adding_training.adding_goal;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

import java.util.List;

@Dao
public interface TrainingGoalJoinDAO {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(TrainingGoalJoin trainingGoalJoin);

        @Query("SELECT goal_table.* FROM goal_table  INNER JOIN training_goal_join ON goal_table.goalId=training_goal_join.goalId WHERE training_goal_join.trainingId=:trainingId ")
        LiveData<List<Goal>> getGoalsForTraining(final String trainingId);

        @Query("SELECT training_table.* FROM training_table  INNER JOIN training_goal_join ON training_table.id=training_goal_join.trainingId WHERE training_goal_join.goalId=:goalId ")
        LiveData<List<Training>> getTrainingsForGoal(final String goalId);

        @Update(onConflict = OnConflictStrategy.REPLACE)
        void update(TrainingGoalJoin param);

        @Query("SELECT training_goal_join.* FROM training_goal_join WHERE training_goal_join.trainingId=:trainingId")
        LiveData<List<TrainingGoalJoin>> getTrainingGoalsForTrainingId(String trainingId);
}
