package com.dobrowol.traininglog.adding_exercise;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ExerciseDescriptionViewModel extends AndroidViewModel {

    private ExerciseDescriptionRepository mRepository;

    private LiveData<List<ExerciseDescription>> mAllWords;

    public ExerciseDescriptionViewModel (Application application) {
        super(application);
        mRepository = new ExerciseDescriptionRepository(application);
        mAllWords = mRepository.getAllExerciseDescriptions();
    }

    public LiveData<List<ExerciseDescription>> getAllExercisesDescriptions() { return mAllWords; }

    LiveData<ExerciseDescription> getExerciseDescription(String description) {return mRepository.getExerciseDescription(description);}

    public void insert(ExerciseDescription word) { mRepository.insert(word); }
}

