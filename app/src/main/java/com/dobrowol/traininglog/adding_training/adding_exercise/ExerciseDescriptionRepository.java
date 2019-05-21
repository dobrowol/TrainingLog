package com.dobrowol.traininglog.adding_training.adding_exercise;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.dobrowol.traininglog.adding_training.TrainingRoomDatabase;

import java.util.List;

public class ExerciseDescriptionRepository {

    private ExerciseDescriptionDAO mExercisesDescriptionsDao;
    private LiveData<List<ExerciseDescription>> mAllExercisesDescriptions;

    ExerciseDescriptionRepository(Application application) {
        TrainingRoomDatabase db = TrainingRoomDatabase.getDatabase(application);
        mExercisesDescriptionsDao = db.exerciseDescriptionDAO();
        mAllExercisesDescriptions = mExercisesDescriptionsDao.getAll();
    }

    LiveData<List<ExerciseDescription>> getAllExerciseDescriptions() {
        return mAllExercisesDescriptions;
    }

    LiveData<ExerciseDescription> getExerciseDescription(String description) {
        return mExercisesDescriptionsDao.findByDescription(description);
    }

    LiveData<ExerciseDescription> getExerciseDescriptionById(String id) {
        return mExercisesDescriptionsDao.findById(id);
    }

    public void insert (ExerciseDescription exerciseDescription) {
        new insertAsyncTask(mExercisesDescriptionsDao).execute(exerciseDescription);
    }

    private static class insertAsyncTask extends AsyncTask<ExerciseDescription, Void, Void> {

        private ExerciseDescriptionDAO mAsyncTaskDao;

        insertAsyncTask(ExerciseDescriptionDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ExerciseDescription... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}