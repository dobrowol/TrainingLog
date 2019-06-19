package com.dobrowol.traininglog.adding_training;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

import java.util.Date;
import java.util.List;

public class TrainingExerciseJoinViewModel extends AndroidViewModel {

    private TrainingExerciseJoinRepository mRepository;

    private MutableLiveData<String> query  = new MutableLiveData<>();
    public LiveData<List<Exercise>> trainingExercises = Transformations.switchMap(query,
    trainingId ->
            mRepository.getAllExercisesForTraining(trainingId)
    );
    private LiveData<List<Exercise>> temp( String trainingId) { return  mRepository.getAllExercisesForTraining(trainingId);}
    public void getExercisesByTrainingId( String trainingId){ query.setValue(trainingId); }

    public TrainingExerciseJoinViewModel(Application application) {
        super(application);
        mRepository = new TrainingExerciseJoinRepository(application);
    }

    public LiveData<List<Training>> getAllTrainingsForExercise(String exerciseId) { return mRepository.getAllTrainingsForExercise(exerciseId); }

    public void insert(TrainingExerciseJoin trainingExerciseJoin) { mRepository.insert(trainingExerciseJoin); }
}

