package com.dobrowol.traininglog.adding_training.adding_goal;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseDescription;

import java.util.List;

public class GoalViewModel extends AndroidViewModel {

    private GoalRepository mRepository;

    private LiveData<List<Exercise>> mAllGoals;

    public GoalViewModel(Application application) {
        super(application);
        mRepository = new GoalRepository(application);
        mAllGoals = mRepository.getAllExercises();
    }

    public LiveData<List<Exercise>> getAllExercises() { return mAllGoals; }

    public LiveData<ExerciseDescription> getExerciseDescription(String description) {return mRepository.getExerciseDescription(description);}

    public void insert(Exercise exercise) { mRepository.insert(exercise); }

    public void update(Exercise exercise) { mRepository.update(exercise); }

}

