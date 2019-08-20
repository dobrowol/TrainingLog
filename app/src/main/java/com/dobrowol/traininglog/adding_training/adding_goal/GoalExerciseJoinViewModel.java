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
    private class GoalExerciseIds{
        public String goalId;
        public String exerciseId;

        public GoalExerciseIds(String goalId, String exerciseId) {
            this.exerciseId = exerciseId;
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

    private MutableLiveData<GoalExerciseIds> query2  = new MutableLiveData<>();
    public LiveData<GoalExercise> goalExerciseJoin= Transformations.switchMap(query2,
            goalExerciseIds ->
                    mRepository.getGoalExerciseJoin(goalExerciseIds.goalId, goalExerciseIds.exerciseId)
    );

    public void getGoalExerciseJoin( String goalId, String exerciseId){
        GoalExerciseIds goalExerciseIds = new GoalExerciseIds(goalId, exerciseId);
        query2.setValue(goalExerciseIds);
    }


    public GoalExerciseJoinViewModel(Application application) {
        super(application);
        mRepository = new GoalExerciseJoinRepository(application);
    }

    public void insert(GoalExercise goalExercise) { mRepository.insert(goalExercise); }
}

