package com.dobrowol.traininglog.adding_training.adding_exercise;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.dobrowol.traininglog.adding_training.adding_goal.Goal;

import java.io.Serializable;
import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = {@Index("exerciseDescriptionId")},
        tableName = "exercise_table",
        foreignKeys = {@ForeignKey(entity = ExerciseDescription.class,
        parentColumns = "eid",
        childColumns = "exerciseDescriptionId",
        onDelete = CASCADE),
        @ForeignKey(entity = Goal.class,
                parentColumns = "id",
                childColumns = "goalId",
                onDelete = CASCADE)})
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
    public Intensity intensity;
    public int restBetweenSets;
    public int restBetweenSeries;
    public int workInterval;
    public int numberOfRepetitionsInSet;
    public int numberOfSetsInSeries;
    public int numberOfSeries;
    public int loadValue;
    public Date startDate;

    public Exercise(String id, ExerciseType type, int distance, Intensity intensity, String descritpionId, int numberOfRepetitionsInSet,
                    int numberOfSetsInSeries, Date startDate){
        this.id = id;
        this.type = type;
        this.distance = distance;
        this.intensity = intensity;
        this.exerciseDescriptionId = descritpionId;
        this.numberOfRepetitionsInSet = numberOfRepetitionsInSet;
        this.numberOfSetsInSeries = numberOfSetsInSeries;
        this.startDate = startDate;

        if (numberOfSeries <= 0){numberOfSeries = 1;}
        if(this.numberOfRepetitionsInSet <= 0){this.numberOfRepetitionsInSet = 1;}
        if(this.numberOfSetsInSeries <=0){this.numberOfSetsInSeries = 1;}
    }

    public void calculateLoad(){
        loadValue = this.distance*this.numberOfSeries*this.numberOfSetsInSeries*numberOfRepetitionsInSet *(this.intensity.ordinal()+1);
    }
    public void neurologicalBoost(){
        loadValue *= 8;
    }
    protected Exercise(Parcel in) {
        id = in.readString();
        exerciseDescriptionId = in.readString();
        type = (ExerciseType) in.readSerializable();
        distance = in.readInt();
        intensity = (Intensity) in.readSerializable();
        totalDistance = in.readInt();
        restBetweenSets = in.readInt();
        restBetweenSeries = in.readInt();
        workInterval = in.readInt();
        numberOfRepetitionsInSet = in.readInt();
        numberOfSetsInSeries = in.readInt();
        numberOfSeries = in.readInt();
        loadValue = in.readInt();
        startDate = (Date) in.readSerializable();
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
        parcel.writeSerializable(intensity);
        parcel.writeInt(totalDistance);
        parcel.writeInt(restBetweenSets);
        parcel.writeInt(restBetweenSeries);
        parcel.writeInt(workInterval);
        parcel.writeInt(numberOfRepetitionsInSet);
        parcel.writeInt(numberOfSetsInSeries);
        parcel.writeInt(numberOfSeries);
        parcel.writeInt(loadValue);
        parcel.writeSerializable(startDate);

    }
    public Exercise(){
    }
}
