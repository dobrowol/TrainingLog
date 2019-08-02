package com.dobrowol.traininglog.training_load.calculating;

import androidx.lifecycle.LifecycleOwner;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.adding_training.adding_goal.GoalExercise;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalExerciseJoinViewModel;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalJoin;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalJoinViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TrainingGoalLoad {
    private TrainingGoalJoin trainingGoalJoin;
    private TrainingGoalJoinViewModel trainingGoalJoinViewModel;

    public TrainingGoalLoad(TrainingGoalJoin trainingGoalJoin,
                            TrainingGoalJoinViewModel trainingGoalJoinViewModel) {

        this.trainingGoalJoin = trainingGoalJoin;
        this.trainingGoalJoinViewModel = trainingGoalJoinViewModel;

    }

    public int update(Exercise exercise, GoalExercise goalExercise, Training training){
        int exerciseLoad = 0;
        if(trainingGoalJoin.trainingId.equals(training.id) && trainingGoalJoin.goalId.equals(goalExercise.goalId)) {
            long diffInMillies = Math.abs(training.date.getTime() - exercise.startDate.getTime());
            long daysFromFirstOccurence = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            int neurologicalBoostPeriod = 21;
            int neurologicalBoost = 1;
            if (daysFromFirstOccurence <= neurologicalBoostPeriod) {
                neurologicalBoost = 8;
            }
            exerciseLoad = exercise.loadValue * goalExercise.specificity * neurologicalBoost;
        }
        trainingGoalJoin.load += exerciseLoad;

        this.trainingGoalJoinViewModel.insert(trainingGoalJoin).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                s -> {
                });
        return exerciseLoad;
    }



}

