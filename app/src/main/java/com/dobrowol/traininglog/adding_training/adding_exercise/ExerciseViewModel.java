package com.dobrowol.traininglog.adding_training.adding_exercise;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ExerciseViewModel extends AndroidViewModel {

    private ExerciseRepository mRepository;

    private LiveData<List<Exercise>> mAllExercises;

    public ExerciseViewModel(Application application) {
        super(application);
        mRepository = new ExerciseRepository(application);
        mAllExercises = mRepository.getAllExercises();
    }

    public LiveData<List<Exercise>> getAllExercises() { return mAllExercises; }

    public LiveData<ExerciseDescription> getExerciseDescription(String description) {return mRepository.getExerciseDescription(description);}

    public void insert(Exercise exercise) { mRepository.insert(exercise); }
}

