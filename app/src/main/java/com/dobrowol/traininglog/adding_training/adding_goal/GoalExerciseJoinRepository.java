package com.dobrowol.traininglog.adding_training.adding_goal;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.dobrowol.traininglog.adding_training.TrainingRoomDatabase;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

import java.util.List;

public class GoalExerciseJoinRepository {

    private GoalExerciseJoinDAO goalExerciseJoinDAO;

    GoalExerciseJoinRepository(Application application) {
        TrainingRoomDatabase db = TrainingRoomDatabase.getDatabase(application);
        goalExerciseJoinDAO = db.goalExerciseJoinDAO();
    }

    LiveData<List<Exercise>> getAllExercisesForGoal(String goalId) {
        return goalExerciseJoinDAO.getExercisesForGoal(goalId);
    }

    LiveData<List<Goal>> getAllGoalsForTraining(String exerciseId) {
        return goalExerciseJoinDAO.getGoalsForExercise(exerciseId);
    }

    public void insert (GoalExercise goalExercise) {
        new insertAsyncTask(goalExerciseJoinDAO).execute(goalExercise);
    }

    private static class insertAsyncTask extends AsyncTask<GoalExercise, Void, Void> {

        private GoalExerciseJoinDAO mAsyncTaskDao;

        insertAsyncTask(GoalExerciseJoinDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final GoalExercise... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}