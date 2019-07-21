package com.dobrowol.traininglog.adding_training.adding_goal;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

import java.util.List;

public class GoalExerciseJoinViewModel extends AndroidViewModel {

    private GoalExerciseJoinRepository mRepository;
    private class TrainingGoal{
        public String trainingId;
        public String goalId;

        public TrainingGoal(String trainingId, String goalId) {
            this.trainingId = trainingId;
            this.goalId = goalId;
        }
    }
    private MutableLiveData<String> query  = new MutableLiveData<>();
    public LiveData<List<Goal>> exerciseGoal = Transformations.switchMap(query,
            exerciseId ->
            mRepository.getAllGoalsForTraining(exerciseId)
    );

    public void getAllGoalsForExercise( String trainingId){
        query.setValue(trainingId); }

    private MutableLiveData<String> query1  = new MutableLiveData<>();
    public LiveData<List<Exercise>> goalExercise = Transformations.switchMap(query1,
            goalId ->
                    mRepository.getAllExercisesForGoal(goalId)
    );

    public void getAlExercisesForGoal( String goalId){
        query1.setValue(goalId); }

    public GoalExerciseJoinViewModel(Application application) {
        super(application);
        mRepository = new GoalExerciseJoinRepository(application);
    }

    public void insert(GoalExercise goalExercise) { mRepository.insert(goalExercise); }
}

