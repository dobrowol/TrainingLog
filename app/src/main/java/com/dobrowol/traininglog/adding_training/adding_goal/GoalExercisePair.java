package com.dobrowol.traininglog.adding_training.adding_goal;

import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

public class GoalExercisePair {
    Goal goal;
    Exercise exercise;

    GoalExercisePair(Goal goal, Exercise exercise){
        this.goal = goal;
        this.exercise = exercise;
    }
}
