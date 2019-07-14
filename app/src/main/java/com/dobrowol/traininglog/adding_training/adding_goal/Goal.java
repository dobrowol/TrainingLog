package com.dobrowol.traininglog.adding_training.adding_goal;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "goal_table",
        foreignKeys = @ForeignKey(entity = Goal.class,
        parentColumns = "id",
        childColumns = "primaryGoalId",
        onDelete = CASCADE))
public class Goal implements Parcelable {
    @PrimaryKey
    @NonNull
    public String id;

    public String primaryGoalId;

    public String description;

    public int priority;

    public Date startDate;

    public Date endDate;

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

    public Goal(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Goal(Parcel in){
        id = in.readString();
        description = in.readString();
        priority = in.readInt();
        startDate = (Date) in.readSerializable();
        endDate = (Date) in.readSerializable();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(primaryGoalId);
        dest.writeString(description);
        dest.writeInt(priority);
        dest.writeSerializable(startDate);
        dest.writeSerializable(endDate);
    }
}
