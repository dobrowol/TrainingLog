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
    private class TrainingGoal{
        public String trainingId;
        public String goalId;

        public TrainingGoal(String trainingId, String goalId) {
            this.trainingId = trainingId;
            this.goalId = goalId;
        }
    }
    private MutableLiveData<TrainingGoal> query  = new MutableLiveData<>();
    public LiveData<List<Exercise>> goalTrainingExercises = Transformations.switchMap(query,
            trainingGoal ->
            mRepository.getExercisesForTrainingAndGoal(trainingGoal.goalId, trainingGoal.trainingId)
    );

    public void getExercisesByTrainingIdGoalId( String trainingId, String goalId){ TrainingGoal trainingGoal = new TrainingGoal(trainingId, goalId);
        query.setValue(trainingGoal); }


    private MutableLiveData<String> query1  = new MutableLiveData<>();
    public LiveData<List<Goal>> trainingGoals = Transformations.switchMap(query1,
            trainingId ->
                    mRepository.getUniqueGoalsForTraining(trainingId)
    );
    public void getUniqueGoalsForTraining( String trainingId){
        query1.setValue(trainingId); }

    private MutableLiveData<String> query2  = new MutableLiveData<>();
    public LiveData<List<GoalExercisePair>> trainingGoalsExercises = Transformations.switchMap(query2,
            trainingId ->
                    mRepository.getExerciseGoalsForTraining(trainingId)
    );
    public void getExerciseGoalsForTraining( String trainingId){
        query2.setValue(trainingId); }

    public TrainingGoalExerciseJoinViewModel(Application application) {
        super(application);
        mRepository = new TrainingGoalExerciseJoinRepository(application);
    }



    public void insert(TrainingGoalExerciseJoin trainingGoalExerciseJoin) { mRepository.insert(trainingGoalExerciseJoin); }
}

