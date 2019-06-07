package com.dobrowol.traininglog.adding_training_cycle;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

import java.util.ArrayList;
import java.util.List;

public class TrainingCycle {

    Training forecastTraining(){return new Training();}

    List<Training> forecastTrainings(int numberOfTrainings){return new ArrayList<>();}

    double calculateTrainingCycleLoadValue(){return 0.0;}

    void calculateTrainingLoadValues(){}

}
