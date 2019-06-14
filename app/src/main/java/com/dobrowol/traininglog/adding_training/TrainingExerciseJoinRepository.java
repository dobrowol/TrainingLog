package com.dobrowol.traininglog.adding_training;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.dobrowol.traininglog.adding_training.adding_exercise.Converters;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.hadilq.liveevent.LiveEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TrainingExerciseJoinRepository {

    private TrainingExerciseJoinDAO trainingExerciseJoinDAO;
    private LiveData<List<TrainingExerciseJoin>> mAllExercises;

    TrainingExerciseJoinRepository(Application application) {
        TrainingRoomDatabase db = TrainingRoomDatabase.getDatabase(application);
        trainingExerciseJoinDAO = db.trainingExerciseJoinDAO();
    }

    LiveData<List<Exercise>> getAllExercisesForTraining(String trainingId) {
        return trainingExerciseJoinDAO.getExercisesForTraining(trainingId);
    }

    LiveData<List<Training>> getAllTrainingsForExercise(String exerciseId) {
        return trainingExerciseJoinDAO.getTrainingsForExercise(exerciseId);
    }

    public void insert (TrainingExerciseJoin exercise) {
        new insertAsyncTask(trainingExerciseJoinDAO).execute(exercise);
    }

    private static class insertAsyncTask extends AsyncTask<TrainingExerciseJoin, Void, Void> {

        private TrainingExerciseJoinDAO mAsyncTaskDao;

        insertAsyncTask(TrainingExerciseJoinDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TrainingExerciseJoin... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}