package com.dobrowol.traininglog.adding_training.adding_goal;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.dobrowol.traininglog.adding_training.Training;

import java.util.List;

import io.reactivex.Single;

public class TrainingGoalJoinViewModel extends AndroidViewModel {


    private TrainingGoalJoinRepository mRepository;

    public LiveData<List<TrainingGoalJoin>> getAllTrainingGoalJoins(){
        return mRepository.getAllTrainingGoals();
    }

    public void update(TrainingGoalJoin trainingGoalJoin) {
        mRepository.update(trainingGoalJoin);
    }

    public void getAllTrainingGoalJoinsForTraining(String id) {
        query2.setValue(id);
    }

    private class TrainingGoal{
        public String trainingId;
        public String goalId;

        TrainingGoal(String trainingId, String goalId) {
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
    public Single<Long> insert(TrainingGoalJoin trainingGoalJoin) { return mRepository.insert(trainingGoalJoin); }

    private MutableLiveData<String> query2 = new MutableLiveData<>();
    public LiveData<List<TrainingGoalJoin>> trainingGoalsForTrainingId = Transformations.switchMap(query2,
            trainingId ->
                    mRepository.getAllTrainingGoalsForTrainingId(trainingId));

    private MutableLiveData<TrainingGoal> query3  = new MutableLiveData<>();

    public LiveData<TrainingGoalJoin> trainingGoal = Transformations.switchMap(query3,
            trainingGoal -> mRepository.getTrainingGoal(trainingGoal.trainingId, trainingGoal.goalId)
            );
    public void getTrainingGoal(String trainingId, String goalId){
        TrainingGoal trainingGoal = new TrainingGoal(trainingId, goalId);
        query3.setValue(trainingGoal);
    }
    public LiveData<Integer> getMaximumLoad(){
        return mRepository.getMaximumLoad();
    }

}


