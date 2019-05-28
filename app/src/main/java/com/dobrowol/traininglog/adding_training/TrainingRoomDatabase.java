package com.dobrowol.traininglog.adding_training;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.dobrowol.traininglog.adding_training.adding_exercise.Converters;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseDAO;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseDescription;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseDescriptionDAO;

@Database(entities = {ExerciseDescription.class, Exercise.class, Training.class, TrainingExerciseJoin.class}, version = 3)
@TypeConverters({Converters.class})
public abstract class TrainingRoomDatabase extends RoomDatabase {
    public abstract ExerciseDescriptionDAO exerciseDescriptionDAO();
    public abstract ExerciseDAO exerciseDAO();
    public abstract TrainingDAO trainingDAO();
    public abstract TrainingExerciseJoinDAO trainingExerciseJoinDAO();

    private static volatile TrainingRoomDatabase INSTANCE;

    public static TrainingRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TrainingRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TrainingRoomDatabase.class, "training_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}


