package com.dobrowol.traininglog.adding_training.adding_goal;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

import java.util.List;

public class TrainingGoalJoinViewModel extends AndroidViewModel {


    private TrainingGoalJoinRepository mRepository;

    public void update(TrainingGoalJoin trainingGoalJoin) {
        mRepository.update(trainingGoalJoin);
    }

    private class TrainingGoal{
        public String trainingId;
        public String goalId;

        public TrainingGoal(String trainingId, String goalId) {
            this.trainingId = trainingId;
            this.goalId = goalId;
        }
    }
    private MutableLiveData<String> query  = new MutableLiveData<>();
    public LiveData<List<Goal>> goalsForTraining = Transformations.switchMap(query,
            trainingId ->
            mRepository.getAllGoalsForTraining(trainingId)
    );

    public void getAllGoalsForTraining( String trainingId){
        query.setValue(trainingId); }
    private MutableLiveData<String> query1  = new MutableLiveData<>();
    public LiveData<List<Training>> goalTrainings = Transformations.switchMap(query1,
            goalId ->
                    mRepository.getAllTrainingsForGoal(goalId)
    );

    public void getAllTrainingsForGoal( String goalId){
        query1.setValue(goalId); }


    public TrainingGoalJoinViewModel(Application application) {
        super(application);
        mRepository = new TrainingGoalJoinRepository(application);
    }
    public void insert(TrainingGoalJoin trainingGoalJoin) { mRepository.insert(trainingGoalJoin); }

    private MutableLiveData<String> query2 = new MutableLiveData<>();
    public LiveData<List<TrainingGoalJoin>> trainingGoals = Transformations.switchMap(query2,
            trainingId ->
                    mRepository.getAllTrainingGoalsForTrainingId(trainingId));
}


