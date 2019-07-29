package com.dobrowol.traininglog.adding_training.adding_goal;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.jraska.livedata.TestObserver;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.TrainingDAO;
import com.dobrowol.traininglog.adding_training.TrainingRoomDatabase;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseDAO;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseDescription;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseDescriptionDAO;
import com.dobrowol.traininglog.adding_training.adding_exercise.Intensity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class TrainingGoalExerciseJoinDAOTest {
    @Rule
    public InstantTaskExecutorRule testRule = new InstantTaskExecutorRule();
    private TrainingRoomDatabase db;
    TrainingGoalExerciseJoinDAO trainingGoalExerciseJoinDAO;
    ExerciseDAO exerciseDAO;
    GoalDAO goalDAO;
    ExerciseDescriptionDAO exerciseDescriptionDAO;
    GoalExerciseJoinDAO goalExerciseJoinDAO;
    TrainingDAO trainingDAO;

    @Before
    public void setUp() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, TrainingRoomDatabase.class).build();
        trainingGoalExerciseJoinDAO = db.trainingGoalExerciseJoinDAO();
        exerciseDAO = db.exerciseDAO();
        goalDAO = db.goalDAO();
        exerciseDescriptionDAO = db.exerciseDescriptionDAO();
        goalExerciseJoinDAO = db.goalExerciseJoinDAO();
        trainingDAO = db.trainingDAO();
    }

    @After
    public void tearDown() throws Exception {
        db.close();

    }
    @Test
    public void writeUserAndReadInList() throws Exception {
        ExerciseDescription exerciseDescription = new ExerciseDescription();
        exerciseDescription.description = "rece kraul";
        exerciseDescription.eid = "ed1";

        long res = exerciseDescriptionDAO.insert(exerciseDescription);
        if(res >0) {
            Exercise exercise = new Exercise("e1", 1, Intensity.ZoneOne, "ed1", 1, 1, new Date());
            exerciseDAO.insert(exercise);

            Goal goal = new Goal("g1", "kraul");
            goalDAO.insert(goal);
            GoalExercise goalExercise = new GoalExercise("ge1", "g1", "e1", 5);
            goalExerciseJoinDAO.insert(goalExercise);

            Training training = new Training("t1", new Date());
            trainingDAO.insert(training);
            TrainingGoalExerciseJoin trainingGoalExerciseJoin = new TrainingGoalExerciseJoin("tge1", "t1", "g1", "e1", 1);
            trainingGoalExerciseJoinDAO.insert(trainingGoalExerciseJoin);

        }
        TestObserver.test( trainingGoalExerciseJoinDAO.getTrainingGoalLoadData("t1", "g1")).awaitValue()
                .assertHasValue();

        //assertThat(byName.get(0), equalTo(user));
    }


}