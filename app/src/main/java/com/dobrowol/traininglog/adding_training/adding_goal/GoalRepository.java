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

    private ExerciseDAO mExerciseDao;
    private LiveData<List<Exercise>> mAllExercises;

    GoalRepository(Application application) {
        TrainingRoomDatabase db = TrainingRoomDatabase.getDatabase(application);
        mExerciseDao = db.exerciseDAO();
        mAllExercises = mExerciseDao.getAll();
    }

    LiveData<List<Exercise>> getAllExercises() {
        return mAllExercises;
    }

    LiveData<ExerciseDescription> getExerciseDescription(String description) {
        return mExerciseDao.findExerciseDescriptonByDescription(description);
    }

    public void insert (Exercise exercise) {
        new insertAsyncTask(mExerciseDao).execute(exercise);
    }

    public void update(Exercise exercise) {
        new updateAsyncTask(mExerciseDao).execute(exercise);
    }

    private static class insertAsyncTask extends AsyncTask<Exercise, Void, Void> {

        private ExerciseDAO mAsyncTaskDao;

        insertAsyncTask(ExerciseDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Exercise... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    private static class updateAsyncTask extends AsyncTask<Exercise, Void, Void> {

        private ExerciseDAO mAsyncTaskDao;

        updateAsyncTask(ExerciseDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Exercise... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}