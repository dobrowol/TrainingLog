package com.dobrowol.traininglog.training_load.calculating;

import androidx.lifecycle.LifecycleOwner;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.TrainingViewModel;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseViewModel;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalExerciseJoinViewModel;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalJoin;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalJoinViewModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TrainingGoalLoad {
    private TrainingGoalExerciseJoinViewModel trainingGoalExerciseJoinViewModel;
    private LifecycleOwner owner;
    private TrainingLoadCalculatedListener listener;

    public interface TrainingLoadCalculatedListener{
        public void loadCalculated(int load);
    }
    public TrainingGoalLoad(TrainingGoalExerciseJoinViewModel trainingGoalExerciseJoinViewModel, LifecycleOwner owner,
                            TrainingLoadCalculatedListener listener) {
        this.trainingGoalExerciseJoinViewModel = trainingGoalExerciseJoinViewModel;
        this.owner = owner;
        this.listener = listener;
        trainingGoalExerciseJoinViewModel.trainingGoalLoadData.observe(owner, trainingGoalLoadData -> {
            if(trainingGoalLoadData != null) {
                int load = 0;
                for (TrainingGoalLoadData loadData : trainingGoalLoadData) {
                    long diffInMillies = Math.abs(loadData.trainingDate.getTime() - loadData.exerciseStartDate.getTime());
                    long daysFromFirstOccurence = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                    int neurologicalBoostPeriod = 21;
                    int neurologicalBoost = 1;
                    if(daysFromFirstOccurence <= neurologicalBoostPeriod){
                        neurologicalBoost=8;
                    }
                    load += loadData.load*loadData.specificity*neurologicalBoost;
                }
                listener.loadCalculated(load);
            }

        });
    }


    public void calculate(String trainingId, String goalId) {

        trainingGoalExerciseJoinViewModel.getTrainingGoalData(trainingId, goalId);

    }
}

