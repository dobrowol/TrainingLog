package com.dobrowol.traininglog.training_load.calculating;

import androidx.lifecycle.LifecycleOwner;

import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalExerciseJoinViewModel;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalJoin;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalJoinViewModel;

import java.util.concurrent.TimeUnit;

public class TrainingGoalLoad {
    private TrainingGoalExerciseJoinViewModel trainingGoalExerciseJoinViewModel;
    private LifecycleOwner owner;
    private TrainingGoalJoin trainingGoalJoin;
    private TrainingGoalJoinViewModel trainingGoalJoinViewModel;

    public TrainingGoalLoad(TrainingGoalJoin trainingGoalJoin, TrainingGoalExerciseJoinViewModel trainingGoalExerciseJoinViewModel, LifecycleOwner owner,
                            TrainingGoalJoinViewModel trainingGoalJoinViewModel) {

        this.trainingGoalExerciseJoinViewModel = trainingGoalExerciseJoinViewModel;
        this.owner = owner;
        this.trainingGoalJoin = trainingGoalJoin;
        this.trainingGoalJoinViewModel = trainingGoalJoinViewModel;
        trainingGoalExerciseJoinViewModel.trainingGoalLoadData.observe(owner, trainingGoalLoadData -> {
            if(trainingGoalLoadData != null) {
                int load = -1;
                for (TrainingGoalLoadData loadData : trainingGoalLoadData) {
                /*    if(trainingGoalJoin.trainingId.equals(loadData.trainingJoinId) && trainingGoalJoin.goalId.equals(loadData.goalId)) {
                        long diffInMillies = Math.abs(loadData.date.getTime() - loadData.startDate.getTime());
                        long daysFromFirstOccurence = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                        int neurologicalBoostPeriod = 21;
                        int neurologicalBoost = 1;
                        if (daysFromFirstOccurence <= neurologicalBoostPeriod) {
                            neurologicalBoost = 8;
                        }
                        load += loadData.loadValue * loadData.specificity * neurologicalBoost;
                    }*/
                }
                if(load != -1) {
                    trainingGoalJoin.load = load;
                    this.trainingGoalJoinViewModel.update(trainingGoalJoin);
                }
            }

        });
    }


    public void calculate() {

        trainingGoalExerciseJoinViewModel.getTrainingGoalData(trainingGoalJoin.trainingId, trainingGoalJoin.goalId);

    }
}

