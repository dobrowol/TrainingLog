package com.dobrowol.traininglog.adding_training.adding_goal;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.TrainingRoomDatabase;

import java.util.List;

import io.reactivex.Single;

public class TrainingGoalJoinRepository {

    private TrainingGoalJoinDAO trainingGoalJoinDAO;

    private class TrainingGoalIds{
        String trainingId;
        String goalId;

        public TrainingGoalIds(String trainingId, String goalId) {
            this.trainingId = trainingId;
            this.goalId = goalId;
        }
    }
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

    LiveData<Integer> getMaximumLoad(){
        return trainingGoalJoinDAO.getMaximumLoad();
    }

    public void insert (TrainingGoalJoin trainingGoalJoin) {
        new insertAsyncTask(trainingGoalJoinDAO).execute(trainingGoalJoin);
    }

    public void update(TrainingGoalJoin trainingGoalJoin) {
        new updateAsyncTask(trainingGoalJoinDAO).execute(trainingGoalJoin);
    }

    LiveData<List<TrainingGoalJoin>> getAllTrainingGoalsForTrainingId(String trainingId) {
        return trainingGoalJoinDAO.getTrainingGoalsForTrainingId(trainingId);
    }

    LiveData<List<TrainingGoalJoin>> getAllTrainingGoals() {
        return trainingGoalJoinDAO.getAllTrainingGoals();
    }

    LiveData<TrainingGoalJoin> getTrainingGoal(String trainingId, String goalId) {
        return trainingGoalJoinDAO.getTrainingGoal(trainingId, goalId);
    }

    public void deleteGoal(String trainingId, String goalId) {
        new deleteAsyncTask(trainingGoalJoinDAO).execute(new TrainingGoalIds(trainingId, goalId));
    }

    public LiveData<List<TrainingGoalJoin>> getAllGroupedByGoal(){
        return trainingGoalJoinDAO.getAllGroupedByGoal();
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
    private static class deleteAsyncTask extends AsyncTask<TrainingGoalIds, Void, Void> {

        private TrainingGoalJoinDAO mAsyncTaskDao;

        deleteAsyncTask(TrainingGoalJoinDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TrainingGoalIds... params) {
            mAsyncTaskDao.delete(params[0].trainingId, params[0].goalId);
            return null;
        }
    }
}