package com.dobrowol.traininglog.adding_training.adding_goal;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.training_load.calculating.TrainingGoalExerciseData;
import com.dobrowol.traininglog.training_load.calculating.TrainingGoalLoadData;

import java.util.List;

import io.reactivex.Single;

public class TrainingGoalExerciseJoinViewModel extends AndroidViewModel {

    private TrainingGoalExerciseJoinRepository mRepository;



    private class TrainingGoal{
        public String trainingId;
        public String goalId;
        TrainingGoal(String trainingId, String goalId){
            this.trainingId=trainingId;
            this.goalId=goalId;
        }
    }
    private MutableLiveData<TrainingGoal> query  = new MutableLiveData<>();
    public LiveData<List<Exercise>> goalTrainingExercises = Transformations.switchMap(query,
            trainingGoalId ->
            mRepository.getExercisesForTrainingAndGoal(trainingGoalId.trainingId, trainingGoalId.goalId)
    );

    public void getExercisesByTrainingIdGoalId( String trainingId, String goalId){
        TrainingGoal trainingGoal = new TrainingGoal(trainingId, goalId);
        query.setValue(trainingGoal);
    }

    private MutableLiveData<String> query1  = new MutableLiveData<>();
    public LiveData<List<GoalExercisePair>> goalExercisesForTraining = Transformations.switchMap(query1,
            trainingId ->
                    mRepository.getGoalsAndExercisesForTraining(trainingId)
    );

    public void getGoalExercisesForTraining( String trainingId){
        query1.setValue(trainingId);
    }


    private MutableLiveData<TrainingGoal> query2  = new MutableLiveData<>();
    public LiveData<List<TrainingGoalLoadData>> trainingGoalLoadData = Transformations.switchMap(query2,
            trainingGoal ->
                    mRepository.getTrainingGoalLoadData(trainingGoal.trainingId, trainingGoal.goalId)
    );

    public void getTrainingGoalData( String trainingId, String goalId){
        TrainingGoal trainingGoal = new TrainingGoal(trainingId, goalId);

        query2.setValue(trainingGoal);
    }

    private MutableLiveData<String> query3  = new MutableLiveData<>();
    public LiveData<TrainingGoalExerciseJoin> trainingGoalExercise = Transformations.switchMap(query3,
            trainingGoalExerciseId ->
                    mRepository.getTrainingGoalExercise(trainingGoalExerciseId)
    );
    public void getTrainingGoalExercise(String trainingGoalExerciseJoin) {
         query3.setValue(trainingGoalExerciseJoin);
    }

    public LiveData<List<TrainingGoalExerciseData>> getTrainingGoalExerciseDataAggregated(){
        return mRepository.getTrainingGoalExerciseDataAggregated();
    }
    public TrainingGoalExerciseJoinViewModel(Application application) {
        super(application);
        mRepository = new TrainingGoalExerciseJoinRepository(application);
    }



    public Single<Long> insert(TrainingGoalExerciseJoin trainingGoalExerciseJoin) { return mRepository.insert(trainingGoalExerciseJoin); }
}

