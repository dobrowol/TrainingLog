package com.dobrowol.traininglog.training_load.calculating;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.TrainingExerciseJoinViewModel;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
interface LoadCalculator<T>{
    void calculate();
    void calculate(List<T> data);

}
public class ExerciseLoad implements LoadCalculator<Training>{
    private Exercise exercise;
    private Date firstDateOfOccurrence;
    private ExerciseLoadDataProvider exerciseLoadDataProvider;

    public ExerciseLoad(Exercise exercise, Date firstDateOfOccurrence, TrainingExerciseJoinViewModel trainingExerciseJoinViewModel, LifecycleOwner owner){
        this.exercise = exercise;
        this.firstDateOfOccurrence = firstDateOfOccurrence;
        this.exerciseLoadDataProvider = new ExerciseLoadDataProvider(exercise, this, trainingExerciseJoinViewModel, owner);
    }

    public void calculate(){
        exerciseLoadDataProvider.getData();
    }

    public void calculate(List<Training> trainings){
        exercise.calculateLoad();
        if (trainings != null && trainings.size() > 0 ){
            long diffInMillies = Math.abs(trainings.get(0).date.getTime() - firstDateOfOccurrence.getTime());
            long daysFromFirstOccurence = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            int neurologicalBoostPeriod = 21;
            if (daysFromFirstOccurence <= neurologicalBoostPeriod){
                exercise.neurologicalBoost();
            }
        }
        else {
            exercise.neurologicalBoost();
        }
    }


}
class ExerciseLoadDataProvider {

    private LoadCalculator<Training> loadCalculator;
    private TrainingExerciseJoinViewModel trainingExerciseJoinViewModel;
    private LifecycleOwner owner;
    private Exercise exercise;
    public ExerciseLoadDataProvider(Exercise exercise, LoadCalculator<Training> exerciseLoad, TrainingExerciseJoinViewModel trainingExerciseJoinViewModel, LifecycleOwner owner) {
        this.exercise = exercise;
        loadCalculator = exerciseLoad;
        this.trainingExerciseJoinViewModel = trainingExerciseJoinViewModel;
        this.owner = owner;
    }

    public void getData(){
        LiveData<List<Training>> listLiveData = trainingExerciseJoinViewModel.getAllTrainingsForExercise(exercise.id);
        listLiveData.observe(owner, trainings -> {
            // Update the cached copy of the trainings in the adapter.
           loadCalculator.calculate(trainings);

        });

    }
}
