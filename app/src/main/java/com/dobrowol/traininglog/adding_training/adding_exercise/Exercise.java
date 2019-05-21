package com.dobrowol.traininglog.adding_training.adding_exercise;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = {@Index("exerciseDescriptionId")},
        tableName = "exercise_table",
        foreignKeys = @ForeignKey(entity = ExerciseDescription.class,
        parentColumns = "eid",
        childColumns = "exerciseDescriptionId",
        onDelete = CASCADE))
public class Exercise implements Serializable, Parcelable {
    @PrimaryKey
    @NonNull
    @ColumnInfo (name = "id")
    public String id;
    @ColumnInfo (name = "exerciseDescriptionId")
    public String exerciseDescriptionId;
    public ExerciseType type;
    public int totalDistance;
    public int distance;
    public int intensity;
    public int restBetweenSets;
    public int restBetweenSeries;
    public int workInterval;
    public int numberOfRepetitionsInSet;
    public int numberOfSetsInSeries;
    public int numberOfSeries;


    protected Exercise(Parcel in) {
        id = in.readString();
        exerciseDescriptionId = in.readString();
        type = (ExerciseType) in.readSerializable();
        distance = in.readInt();
        intensity = in.readInt();
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
        parcel.writeString(id);
        parcel.writeString(exerciseDescriptionId);
        parcel.writeSerializable(type);
        parcel.writeInt(distance);
        parcel.writeInt(intensity);
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
