package com.dobrowol.traininglog.adding_training.adding_goal;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.dobrowol.traininglog.adding_training.TrainingRoomDatabase;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseDAO;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseDescription;

import java.util.List;

public class GoalRepository {

    private GoalDAO goalDAO;
    private LiveData<List<Goal>> allGoals;

    GoalRepository(Application application) {
        TrainingRoomDatabase db = TrainingRoomDatabase.getDatabase(application);
        goalDAO = db.goalDAO();
        allGoals = goalDAO.getAll();
    }

    LiveData<List<Goal>> getAllGoals() {
        return allGoals;
    }

    public void insert (Goal goal) {
        new insertAsyncTask(goalDAO).execute(goal);
    }

    public void update(Goal goal) {
        new updateAsyncTask(goalDAO).execute(goal);
    }

    public LiveData<Goal> getGoal(String description) {
        return goalDAO.getByDescription(description);
    }

    public void delete(Goal oldGoal) {
       new deleteAsyncTask(goalDAO).execute(oldGoal);
    }

    public LiveData<Goal> getGoalById(String goalId) {
        return goalDAO.getById(goalId);
    }

    private static class insertAsyncTask extends AsyncTask<Goal, Void, Void> {

        private GoalDAO mAsyncTaskDao;

        insertAsyncTask(GoalDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Goal... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    private static class updateAsyncTask extends AsyncTask<Goal, Void, Void> {

        private GoalDAO mAsyncTaskDao;

        updateAsyncTask(GoalDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Goal... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
    private static class deleteAsyncTask extends AsyncTask<Goal, Void, Void> {

        private GoalDAO mAsyncTaskDao;

        deleteAsyncTask(GoalDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Goal... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }
}