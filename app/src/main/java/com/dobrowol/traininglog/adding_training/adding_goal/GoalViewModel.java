package com.dobrowol.traininglog.adding_training.adding_goal;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

import java.util.List;

public class GoalViewModel extends AndroidViewModel {

    private GoalRepository mRepository;

    private LiveData<List<Goal>> mAllGoals;

    public GoalViewModel(Application application) {
        super(application);
        mRepository = new GoalRepository(application);
        mAllGoals = mRepository.getAllGoals();
    }

    public LiveData<List<Goal>> getAllGoals() { return mAllGoals; }

    public void insert(Goal goal) { mRepository.insert(goal); }

    public void update(Goal goal) { mRepository.update(goal); }

    public LiveData<Goal> getGoal(String description) {
        return mRepository.getGoal(description);
    }

    public LiveData<Goal> getGoalById(String goalId) {
        return mRepository.getGoalById(goalId);
    }

    public void delete(Goal oldGoal) {
        mRepository.delete(oldGoal);
    }
}

