package com.dobrowol.traininglog.adding_training.adding_goal;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dobrowol.traininglog.training_load.calculating.TrainingGoalLoadData;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TrainingGoalExerciseJoinDAOTest {
    @Rule
    public InstantTaskExecutorRule testRule = new InstantTaskExecutorRule();
    private TrainingRoomDatabase db;
    private TrainingGoalExerciseJoinDAO trainingGoalExerciseJoinDAO;
    private ExerciseDAO exerciseDAO;
    private GoalDAO goalDAO;
    private ExerciseDescriptionDAO exerciseDescriptionDAO;
    private GoalExerciseJoinDAO goalExerciseJoinDAO;
    private TrainingDAO trainingDAO;

    @Before
    public void setUp() {
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
    public void tearDown() {
        db.close();

    }
    @Test
    public void writeUserAndReadInList() throws Exception {
        ExerciseDescription exerciseDescription = new ExerciseDescription();
        exerciseDescription.description = "rece kraul";
        exerciseDescription.eid = "ed1";

        long res = exerciseDescriptionDAO.insert(exerciseDescription);

            Exercise exercise1 = new Exercise("e1", 1, Intensity.ZoneOne, "ed1", 1, 1, new Date());
            exercise1.calculateLoad();
            Exercise exercise2 = new Exercise("e2", 2, Intensity.ZoneOne, "ed1", 2, 2, new Date());
            exercise2.calculateLoad();
            exerciseDAO.insert(exercise1);
            exerciseDAO.insert(exercise2);

            Goal goal = new Goal("g1", "kraul");
            goalDAO.insert(goal);
            GoalExercise goalExercise = new GoalExercise("ge1", "g1", "e1", 5);
            GoalExercise goalExercise2 = new GoalExercise("ge2", "g1", "e2", 4);

            goalExerciseJoinDAO.insert(goalExercise);
            goalExerciseJoinDAO.insert(goalExercise2);

            Training training = new Training("t1", new Date());
            trainingDAO.insert(training);
            TrainingGoalExerciseJoin trainingGoalExerciseJoin = new TrainingGoalExerciseJoin("tge1", "t1", "g1", "e1", 1);
            TrainingGoalExerciseJoin trainingGoalExerciseJoin2 = new TrainingGoalExerciseJoin("tge2", "t1", "g1", "e2", 2);

            trainingGoalExerciseJoinDAO.insert(trainingGoalExerciseJoin);
            trainingGoalExerciseJoinDAO.insert(trainingGoalExerciseJoin2);


        List<TrainingGoalLoadData> expectedList = new ArrayList<>();
        TrainingGoalLoadData trainingGoalLoad = new TrainingGoalLoadData();
        trainingGoalLoad.goalId="g1";
        trainingGoalLoad.trainingJoinId = "t1";
        trainingGoalLoad.loadValue=exercise1.loadValue;
        trainingGoalLoad.startDate=exercise1.startDate;
        trainingGoalLoad.date = training.date;
        trainingGoalLoad.specificity=goalExercise.specificity;

        TrainingGoalLoadData trainingGoalLoad2 = new TrainingGoalLoadData();
        trainingGoalLoad2.goalId="g1";
        trainingGoalLoad2.trainingJoinId = "t1";
        trainingGoalLoad2.loadValue = exercise2.loadValue;
        trainingGoalLoad2.startDate=exercise2.startDate;
        trainingGoalLoad2.date=training.date;
        trainingGoalLoad2.specificity=goalExercise2.specificity;
        expectedList.add(trainingGoalLoad);
        expectedList.add(trainingGoalLoad2);
        TestObserver<List<TrainingGoalLoadData>> testObserver = TestObserver.test( trainingGoalExerciseJoinDAO.getTrainingGoalLoadData("t1", "g1")).awaitValue();
        testObserver.assertValue(expectedList);
    }


}