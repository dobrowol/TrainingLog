package com.dobrowol.traininglog.training_load.calculating;

import androidx.lifecycle.LifecycleOwner;

import com.dobrowol.traininglog.adding_training.TrainingExerciseJoinViewModel;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseViewModel;
import com.dobrowol.traininglog.adding_training.adding_goal.Goal;
import com.dobrowol.traininglog.adding_training.adding_goal.GoalViewModel;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalJoin;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalJoinViewModel;

import java.util.List;

public class GoalLoad {
    private TrainingGoalJoin trainingGoalJoin;
    private TrainingGoalJoinViewModel trainingGoalJoinViewModel;


    public GoalLoad(TrainingGoalJoin trainingGoalJoin, TrainingGoalJoinViewModel trainingGoalJoinViewModel, LifecycleOwner owner) {
        this.trainingGoalJoin = trainingGoalJoin;
        this.trainingGoalJoinViewModel = trainingGoalJoinViewModel;
    }

    public void calculate(final List<Exercise> exercises) {

        for(Exercise exercise : exercises){
            trainingGoalJoin.load +=exercise.loadValue;
        }
        trainingGoalJoinViewModel.update(trainingGoalJoin);

    }
}

