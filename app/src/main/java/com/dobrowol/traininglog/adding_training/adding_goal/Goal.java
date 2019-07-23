package com.dobrowol.traininglog.adding_training.adding_goal;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "goal_table",
        foreignKeys = @ForeignKey(entity = Goal.class,
        parentColumns = "goalId",
        childColumns = "primaryGoalId",
        onDelete = CASCADE))
public class Goal implements Parcelable, Serializable {
    @PrimaryKey
    @NonNull
    public String goalId;

    public String primaryGoalId;

    public String description;

    public int priority;

    public Date goalStartDate;

    public Date endDate;

    public int hashCode(){
        return goalId.hashCode();
    }
    public boolean equals(Object object){
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;
        Goal other = (Goal) object;
        if (goalId.compareTo( other.goalId)!= 0 )
            return false;
        return true;
    }
    public static final Creator<Goal> CREATOR = new Creator<Goal>() {
        @Override
        public Goal createFromParcel(Parcel in) {
            return new Goal(in);
        }

        @Override
        public Goal[] newArray(int size) {
            return new Goal[size];
        }
    };

    public Goal(String goalId, String description) {
        this.goalId = goalId;
        this.description = description;
        goalStartDate = null;
        endDate = null;
        priority = -1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Goal(Parcel in){
        goalId = in.readString();
        description = in.readString();
        priority = in.readInt();
        goalStartDate = (Date) in.readSerializable();
        endDate = (Date) in.readSerializable();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(goalId);
        dest.writeString(primaryGoalId);
        dest.writeString(description);
        dest.writeInt(priority);
        dest.writeSerializable(goalStartDate);
        dest.writeSerializable(endDate);
    }
}
