package com.dobrowol.traininglog.adding_training.adding_goal;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.TrainingRoomDatabase;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

import java.util.List;

public class TrainingGoalJoinRepository {

    private TrainingGoalJoinDAO trainingGoalJoinDAO;

    TrainingGoalJoinRepository(Application application) {
        TrainingRoomDatabase db = TrainingRoomDatabase.getDatabase(application);
        trainingGoalJoinDAO = db.trainingGoalJoinDAO();
    }

    LiveData<List<Training>> getAllTrainingsForGoal(String goalId) {
        return trainingGoalJoinDAO.getTrainingsForGoal(goalId);
    }

    LiveData<List<Goal>> getAllGoalsForTraining(String trainingId) {
        return trainingGoalJoinDAO.getGoalsForTraining(trainingId);
    }

    public void insert (TrainingGoalJoin trainingGoalJoin) {
        new insertAsyncTask(trainingGoalJoinDAO).execute(trainingGoalJoin);
    }

    public void update(TrainingGoalJoin trainingGoalJoin) {
        new updateAsyncTask(trainingGoalJoinDAO).execute(trainingGoalJoin);
    }

    public LiveData<List<TrainingGoalJoin>> getAllTrainingGoalsForTrainingId(String trainingId) {
        return trainingGoalJoinDAO.getTrainingGoalsForTrainingId(trainingId);
    }

    private static class insertAsyncTask extends AsyncTask<TrainingGoalJoin, Void, Void> {

        private TrainingGoalJoinDAO mAsyncTaskDao;

        insertAsyncTask(TrainingGoalJoinDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TrainingGoalJoin... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    private static class updateAsyncTask extends AsyncTask<TrainingGoalJoin, Void, Void> {

        private TrainingGoalJoinDAO mAsyncTaskDao;

        updateAsyncTask(TrainingGoalJoinDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TrainingGoalJoin... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}