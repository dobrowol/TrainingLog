package com.dobrowol.traininglog.adding_training;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity(tableName = "training_table")
public class Training implements Serializable, Parcelable {
    @PrimaryKey
    @NonNull
    public String id;

    public Date date;

    public Training(String id, Date date, Integer general_load, Integer specific_load, Integer competitive_load){
        this.id = id;
        this.date = date;

    }
    protected Training(Parcel in) {
        id = in.readString();
        date = (Date) in.readSerializable();
    }

    public static final Creator<Training> CREATOR = new Creator<Training>() {
        @Override
        public Training createFromParcel(Parcel in) {
            return new Training(in);
        }

        @Override
        public Training[] newArray(int size) {
            return new Training[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeSerializable(date);

    }
    public Training(){
        date = new Date();
    }

    public void calculateLoads(List<Exercise> exerciseList){

      /*  general_load = 0;
        specific_load = 0;
        competitive_load = 0;
        for(Exercise exercise : exerciseList){
            switch (exercise.type){
                case General:
                    general_load += exercise.loadValue;
                    break;
                case Specific:
                    specific_load += exercise.loadValue;
                    break;
                case Competitive:
                    competitive_load += exercise.loadValue;
            }

        }

    */}
}
