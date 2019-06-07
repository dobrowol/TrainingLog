package com.dobrowol.traininglog.training_load.calculating;

import androidx.lifecycle.LifecycleOwner;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.TrainingExerciseJoinViewModel;
import com.dobrowol.traininglog.adding_training.TrainingViewModel;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseViewModel;

import java.util.List;

public class TrainingLoad {
    private Training training;
    private TrainingViewModel trainingViewModel;
    private ExerciseViewModel exerciseViewModel;
    private TrainingExerciseJoinViewModel trainingExerciseJoinViewModel;
    private LifecycleOwner owner;
    private List<Exercise> exercises;

    public TrainingLoad(Training training, TrainingViewModel trainingViewModel, ExerciseViewModel exerciseViewModel,
                        TrainingExerciseJoinViewModel trainingExerciseJoinViewModel, LifecycleOwner owner) {
        this.training = training;
        this.trainingViewModel = trainingViewModel;
        this.exerciseViewModel = exerciseViewModel;
        this.trainingExerciseJoinViewModel = trainingExerciseJoinViewModel;
        this.owner = owner;
    }

    public void calculate(final List<Exercise> exercises) {

        this.exercises = exercises;
        for (Exercise exercise : exercises) {
            ExerciseLoad exerciseLoad = new ExerciseLoad(exercise, training.date, trainingExerciseJoinViewModel, owner);
            exerciseLoad.calculate();
            exerciseViewModel.update(exercise);
        }

        training.calculateLoads(exercises);
        trainingViewModel.update(training);
    }
}

