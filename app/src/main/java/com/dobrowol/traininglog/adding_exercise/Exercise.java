package com.dobrowol.traininglog.adding_exercise;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Exercise implements Serializable, Parcelable {
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

    protected Exercise(Parcel in) {
        description = in.readString();
        type = (ExerciseType) in.readSerializable();
        distance = in.readInt();
        totalDistance = in.readInt();
        restBetweenSets = in.readInt();
        restBetweenSeries = in.readInt();
        workInterval = in.readInt();
        numberOfRepetitionsInSet = in.readInt();
        numberOfSetsInSeries = in.readInt();
        numberOfSeries = in.readInt();
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(description);
        parcel.writeSerializable(type);
        parcel.writeInt(distance);
        parcel.writeInt(totalDistance);
        parcel.writeInt(restBetweenSets);
        parcel.writeInt(restBetweenSeries);
        parcel.writeInt(workInterval);
        parcel.writeInt(numberOfRepetitionsInSet);
        parcel.writeInt(numberOfSetsInSeries);
        parcel.writeInt(numberOfSeries);

    }
    public Exercise(){

    }
}
