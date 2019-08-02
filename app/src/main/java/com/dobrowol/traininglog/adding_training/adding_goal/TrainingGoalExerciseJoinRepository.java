package com.dobrowol.traininglog.adding_training.adding_goal;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.dobrowol.traininglog.adding_training.TrainingRoomDatabase;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.training_load.calculating.TrainingGoalExerciseData;
import com.dobrowol.traininglog.training_load.calculating.TrainingGoalLoadData;

import java.util.List;

import io.reactivex.Single;

public class TrainingGoalExerciseJoinRepository {

    private TrainingGoalExerciseJoinDAO trainingGoalExerciseJoinDAO;

    TrainingGoalExerciseJoinRepository(Application application) {
        TrainingRoomDatabase db = TrainingRoomDatabase.getDatabase(application);
        trainingGoalExerciseJoinDAO = db.trainingGoalExerciseJoinDAO();
    }
    LiveData<List<Exercise>> getExercisesForTrainingAndGoal(final String trainingId, final String goalId){
        return trainingGoalExerciseJoinDAO.getExercisesForTrainingAndGoal(trainingId, goalId);
    }

    LiveData<List<TrainingGoalExerciseData>> getTrainingGoalExerciseDataAggregated()
    {
        return trainingGoalExerciseJoinDAO.getTrainingGoalExerciseDataAggregated();
    }
    public Single<Long> insert (TrainingGoalExerciseJoin exercise) {
        return Single.fromCallable(() -> trainingGoalExerciseJoinDAO.insert(exercise));

       // new insertAsyncTask(trainingGoalExerciseJoinDAO).execute(exercise);
    }

    public LiveData<List<GoalExercisePair>> getGoalsAndExercisesForTraining(String trainingId) {
        return trainingGoalExerciseJoinDAO.getGoalsAndExercisesForTraining(trainingId);
    }

    public LiveData<TrainingGoalExerciseJoin> getTrainingGoalExercise(String id) {
        return trainingGoalExerciseJoinDAO.getTrainingGoalExercise(id);
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

    public LiveData<List<TrainingGoalLoadData>> getTrainingGoalLoadData(String trainingId, String goalId){
        return trainingGoalExerciseJoinDAO.getTrainingGoalLoadData(trainingId, goalId);
    }
    private static class getAsyncTask extends AsyncTask<TrainingGoalExerciseJoin, Void, Void> {

        private TrainingGoalExerciseJoinDAO mAsyncTaskDao;

        getAsyncTask(TrainingGoalExerciseJoinDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TrainingGoalExerciseJoin... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}