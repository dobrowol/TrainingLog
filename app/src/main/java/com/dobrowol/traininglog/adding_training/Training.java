package com.dobrowol.traininglog.adding_training;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;


@Entity(tableName = "training_table")
public class Training implements Serializable, Parcelable {
    @PrimaryKey
    @NonNull
    public String id;

    public Date date;

    public Integer general_load;

    public Integer specific_load;

    public Integer competitive_load;

    protected Training(Parcel in) {
        id = in.readString();
        date = (Date) in.readSerializable();
        general_load = in.readInt();
        specific_load = in.readInt();
        competitive_load = in.readInt();
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
        parcel.writeInt(general_load);
        parcel.writeInt(specific_load);
        parcel.writeInt(competitive_load);

    }
    public Training(){
    }
}
