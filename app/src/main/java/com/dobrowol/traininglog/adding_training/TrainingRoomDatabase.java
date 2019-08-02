package com.dobrowol.traininglog.adding_training;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.dobrowol.traininglog.adding_training.adding_exercise.Converters;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseDAO;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseDescription;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseDescriptionDAO;
import com.dobrowol.traininglog.adding_training.adding_goal.Goal;
import com.dobrowol.traininglog.adding_training.adding_goal.GoalDAO;
import com.dobrowol.traininglog.adding_training.adding_goal.GoalExercise;
import com.dobrowol.traininglog.adding_training.adding_goal.GoalExerciseJoinDAO;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalExerciseJoin;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalExerciseJoinDAO;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalJoin;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalJoinDAO;

@Database(entities = {ExerciseDescription.class, Exercise.class, Training.class, TrainingExerciseJoin.class, Goal.class, TrainingGoalExerciseJoin.class,
        TrainingGoalJoin.class, GoalExercise.class}, version = 20)
@TypeConverters({Converters.class})
public abstract class TrainingRoomDatabase extends RoomDatabase {
    public abstract ExerciseDescriptionDAO exerciseDescriptionDAO();
    public abstract ExerciseDAO exerciseDAO();
    public abstract TrainingDAO trainingDAO();
    public abstract GoalDAO goalDAO();
    public abstract TrainingExerciseJoinDAO trainingExerciseJoinDAO();
    public abstract GoalExerciseJoinDAO goalExerciseJoinDAO();
    public abstract TrainingGoalExerciseJoinDAO trainingGoalExerciseJoinDAO();
    public abstract TrainingGoalJoinDAO trainingGoalJoinDAO();

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
    static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };


}




