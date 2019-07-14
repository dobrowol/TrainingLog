package com.dobrowol.traininglog.adding_training.adding_goal;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.dobrowol.traininglog.adding_training.TrainingRoomDatabase;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

import java.util.List;

public class TrainingGoalExerciseJoinRepository {

    private TrainingGoalExerciseJoinDAO trainingGoalExerciseJoinDAO;

    TrainingGoalExerciseJoinRepository(Application application) {
        TrainingRoomDatabase db = TrainingRoomDatabase.getDatabase(application);
        trainingGoalExerciseJoinDAO = db.goalExerciseJoinDAO();
    }

    LiveData<List<Exercise>> getAllExercisesForGoal(String goalId) {
        return trainingGoalExerciseJoinDAO.getExercisesForGoal(goalId);
    }

    LiveData<List<Goal>> getAllGoalsForExercise(String exerciseId) {
        return trainingGoalExerciseJoinDAO.getGoalsForExercise(exerciseId);
    }
    LiveData<List<Exercise>> getExercisesForTrainingAndGoal(final String goalId, final String trainingId){
        return trainingGoalExerciseJoinDAO.getExercisesForTrainingAndGoal(goalId, trainingId);
    }

    LiveData<List<Goal>> getUniqueGoalsForTraining(final String trainingId){
        return trainingGoalExerciseJoinDAO.getUniqueGoalsForTraining(trainingId);
    }


    public void insert (TrainingGoalExerciseJoin exercise) {
        new insertAsyncTask(trainingGoalExerciseJoinDAO).execute(exercise);
    }

    private static class insertAsyncTask extends AsyncTask<TrainingGoalExerciseJoin, Void, Void> {

        private TrainingGoalExerciseJoinDAO mAsyncTaskDao;

        insertAsyncTask(TrainingGoalExerciseJoinDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TrainingGoalExerciseJoin... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}