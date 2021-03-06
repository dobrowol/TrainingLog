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

    public HashMap<String, Integer> calculate(List<TrainingGoalLoadData> trainingGoalLoadData){
        HashMap<String, Integer> goalLoads = calculateGoalLoads(trainingGoalLoadData);
        if(goalLoads == null)
            return null;
        return goalLoads;
    }

    private HashMap<String, Integer> calculateGoalLoads(List<TrainingGoalLoadData> trainingGoalLoadData) {
        HashMap<String, Integer> goalLoads = new HashMap<>();
        if(trainingGoalLoadData == null)
            return null;
        for(TrainingGoalLoadData trainingGoalLoadData1 : trainingGoalLoadData) {
            Integer load = goalLoads.get(trainingGoalLoadData1.goalDescription);

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
            goalLoads.put(trainingGoalLoadData1.goalDescription, load);
        }
        return goalLoads;
    }


    public class DateLoad{
        public Date date;
        public Integer load;

        DateLoad(Date date, Integer load) {
            this.date = date;
            this.load = load;
        }
    }
    public HashMap<String, List<DateLoad>> calculateGoalLoadsForManyTrainings(List<TrainingGoalLoadData> trainingGoalLoadData) {
        HashMap<String, List<TrainingGoalLoadData>> loadsByTrainings = new HashMap<>();
        for(TrainingGoalLoadData trainingGoalLoadData1 : trainingGoalLoadData){
            List<TrainingGoalLoadData> list = loadsByTrainings.get(trainingGoalLoadData1.trainingJoinId);
            if(list == null){
                list = new ArrayList<>();
            }
            list.add(trainingGoalLoadData1);
            loadsByTrainings.put(trainingGoalLoadData1.trainingJoinId, list);
        }
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

