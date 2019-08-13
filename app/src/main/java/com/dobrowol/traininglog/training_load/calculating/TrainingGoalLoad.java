package com.dobrowol.traininglog.training_load.calculating;

import androidx.lifecycle.LifecycleOwner;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.adding_training.adding_exercise.Intensity;
import com.dobrowol.traininglog.adding_training.adding_goal.GoalExercise;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalExerciseJoinViewModel;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalJoin;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalJoinViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TrainingGoalLoad {

    public TrainingGoalLoad() {

    }

    public ArrayList<Integer> calculate(List<TrainingGoalLoadData> trainingGoalLoadData){
        HashMap<String, Integer> goalLoads = calculateGoalLoads(trainingGoalLoadData);
        return new ArrayList<>(goalLoads.values());
    }

    private HashMap<String, Integer> calculateGoalLoads(List<TrainingGoalLoadData> trainingGoalLoadData) {
        HashMap<String, Integer> goalLoads = new HashMap<>();

        for(TrainingGoalLoadData trainingGoalLoadData1 : trainingGoalLoadData) {
            Integer load = goalLoads.get(trainingGoalLoadData1.goalId);

            if(load == null) {
                load = 0;
            }

            long diffInMillies = Math.abs(trainingGoalLoadData1.date.getTime() - trainingGoalLoadData1.startDate.getTime());
            long daysFromFirstOccurrence = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            int neurologicalBoostPeriod = 21;
            int neurologicalBoost = 1;
            if (daysFromFirstOccurrence <= neurologicalBoostPeriod) {
                neurologicalBoost = 8;
            }
            load += trainingGoalLoadData1.loadValue * trainingGoalLoadData1.specificity * neurologicalBoost;
            goalLoads.put(trainingGoalLoadData1.goalId, load);
        }
        return goalLoads;
    }

    public int update(TrainingGoalJoin trainingGoalJoin,
                      TrainingGoalJoinViewModel trainingGoalJoinViewModel, Exercise exercise, GoalExercise goalExercise, Training training){
        int exerciseLoad = 0;
        if(trainingGoalJoin.trainingId.equals(training.id) && trainingGoalJoin.goalId.equals(goalExercise.goalId)) {
            long diffInMillies = Math.abs(training.date.getTime() - exercise.startDate.getTime());
            long daysFromFirstOccurence = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            int neurologicalBoostPeriod = 21;
            int neurologicalBoost = 1;
            if (daysFromFirstOccurence <= neurologicalBoostPeriod) {
                neurologicalBoost = 8;
            }
            exerciseLoad = exercise.loadValue * goalExercise.specificity * neurologicalBoost;
        }
        trainingGoalJoin.load += exerciseLoad;

        trainingGoalJoinViewModel.insert(trainingGoalJoin).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                s -> {
                });
        return exerciseLoad;
    }

    public class DateLoad{
        public Date date;
        public Integer load;

        public DateLoad(Date date, Integer load) {
            this.date = date;
            this.load = load;
        }
    }
    public HashMap<String, List<DateLoad>> calculate(HashMap<String, List<TrainingGoalLoadData>> loadsByTrainings) {
        HashMap<String, List<DateLoad>> goalLoads = new HashMap<>();
        for(List<TrainingGoalLoadData> entry : loadsByTrainings.values()){
            HashMap<String, Integer> loads = calculateGoalLoads(entry);
            for(String key : loads.keySet()) {
                List<DateLoad> list = goalLoads.get(key);
                if(list == null){
                    list= new ArrayList<>();
                }
                DateLoad dateLoad = new DateLoad(entry.get(0).date, loads.get(key));
                list.add(dateLoad);
                goalLoads.put(key, list);
            }
        }
        return goalLoads;
    }
}

