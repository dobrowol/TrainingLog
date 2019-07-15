package com.dobrowol.traininglog.adding_training.adding_goal;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.TrainingExerciseJoin;
import com.dobrowol.traininglog.adding_training.TrainingExerciseJoinRepository;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

import java.util.List;

public class TrainingGoalExerciseJoinViewModel extends AndroidViewModel {

    private TrainingGoalExerciseJoinRepository mRepository;

    private MutableLiveData<String> query  = new MutableLiveData<>();
    public LiveData<List<Exercise>> goalTrainingExercises = Transformations.switchMap(query,
            trainingGoalId ->
            mRepository.getExercisesForTrainingAndGoal(trainingGoalId)
    );

    public void getExercisesByTrainingIdGoalId( String trainingGoalId){
        query.setValue(trainingGoalId);
    }


    public TrainingGoalExerciseJoinViewModel(Application application) {
        super(application);
        mRepository = new TrainingGoalExerciseJoinRepository(application);
    }

    public void insert(TrainingGoalExerciseJoin trainingGoalExerciseJoin) { mRepository.insert(trainingGoalExerciseJoin); }
}

