package com.dobrowol.traininglog.adding_exercise;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {ExerciseDescription.class, Exercise.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class ExerciseRoomDatabase extends RoomDatabase {
    public abstract ExerciseDescriptionDAO exerciseDescriptionDAO();
    public abstract ExerciseDAO exerciseDAO();
    private static volatile ExerciseRoomDatabase INSTANCE;

    static ExerciseRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ExerciseRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ExerciseRoomDatabase.class, "exercise_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}


