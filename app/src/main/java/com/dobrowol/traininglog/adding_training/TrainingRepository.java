package com.dobrowol.traininglog.adding_training;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.dobrowol.traininglog.adding_training.adding_exercise.Converters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TrainingRepository {

    private TrainingDAO trainingDAO;
    private LiveData<List<Training>> mAllTrainings;

    TrainingRepository(Application application) {
        TrainingRoomDatabase db = TrainingRoomDatabase.getDatabase(application);
        trainingDAO = db.trainingDAO();
        mAllTrainings = trainingDAO.getAll();
    }

    LiveData<List<Training>> getAllExercises() {
        return mAllTrainings;
    }

    LiveData<List<Training>> getTrainingFrom(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Converters.DATE_FORMAT);
        String stringDate = simpleDateFormat.format(date);
        return trainingDAO.findTrainingByDate(stringDate);
    }

    public void insert (Training exercise) {
        new insertAsyncTask(trainingDAO).execute(exercise);
    }


    public void update(final Training training) {

        new updateAsyncTask(trainingDAO).execute(training);
    }

    private static class insertAsyncTask extends AsyncTask<Training, Void, Void> {

        private TrainingDAO mAsyncTaskDao;

        insertAsyncTask(TrainingDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Training... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Training, Void, Void> {

        private TrainingDAO mAsyncTaskDao;

        updateAsyncTask(TrainingDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Training... params) {
            mAsyncTaskDao.updateTask(params[0]);
            return null;
        }
    }
}