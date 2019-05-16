package com.dobrowol.traininglog.adding_exercise;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ExerciseRepository {

    private ExerciseDAO mExerciseDao;
    private LiveData<List<Exercise>> mAllExercises;

    ExerciseRepository(Application application) {
        ExerciseRoomDatabase db = ExerciseRoomDatabase.getDatabase(application);
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
}