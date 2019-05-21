package com.dobrowol.traininglog.adding_training;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

import java.util.Date;
import java.util.List;

public class TrainingExerciseJoinViewModel extends AndroidViewModel {

    private TrainingExerciseJoinRepository mRepository;

    public TrainingExerciseJoinViewModel(Application application) {
        super(application);
        mRepository = new TrainingExerciseJoinRepository(application);
    }

    public LiveData<List<Exercise>> getAllExercisesForTraining(String trainingId) { return mRepository.getAllExercisesForTraining(trainingId); }


    public void insert(TrainingExerciseJoin trainingExerciseJoin) { mRepository.insert(trainingExerciseJoin); }
}

