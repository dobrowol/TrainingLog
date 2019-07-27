package com.dobrowol.traininglog.adding_training.adding_goal;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;

import androidx.room.Room;

import com.dobrowol.traininglog.adding_training.TrainingRoomDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class TrainingGoalExerciseJoinDAOTest {
    private TrainingRoomDatabase db;
    TrainingGoalExerciseJoinDAO trainingGoalExerciseJoinDAO;

    @Before
    public void setUp() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, TrainingRoomDatabase.class).build();
        trainingGoalExerciseJoinDAO = db.trainingGoalExerciseJoinDAO();
    }

    @After
    public void tearDown() throws Exception {
    }
}