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
            int old_load = exercise.loadValue;
            ExerciseLoad exerciseLoad = new ExerciseLoad(exercise, training.date, trainingExerciseJoinViewModel, owner);
            exerciseLoad.calculate();
            if (old_load != exercise.loadValue) {
                exerciseViewModel.update(exercise);
            }
        }
        int general_load = 0;
        int specific_load = 0;
        int competitive_load = 0;
        if (training.general_load != null) {
            general_load = training.general_load;
        }
        if (training.specific_load != null){
            specific_load = training.specific_load;
        }
        if (training.competitive_load != null){
             competitive_load = training.competitive_load;
        }

        training.calculateLoads(exercises);

        if(general_load != training.general_load || specific_load != training.specific_load ||
        competitive_load != training.competitive_load) {
            trainingViewModel.update(training);
        }
    }
}

