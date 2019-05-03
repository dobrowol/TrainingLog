package com.dobrowol.traininglog.adding_exercise;

import java.io.Serializable;

public class Exercise implements Serializable {
public String description;
public ExerciseType type;
public int totalDistance;
public int distance;
public int restBetweenSets;
public int restBetweenSeries;
public int workInterval;
public int numberOfRepetitionsInSet;
public int numberOfSetsInSeries;
public int numberOfSeries;
}
