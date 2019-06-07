package com.dobrowol.traininglog.training_load.calculating;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.TrainingExerciseJoinViewModel;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseType;
import com.dobrowol.traininglog.adding_training.adding_exercise.Intensity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Observable;

import static com.dobrowol.traininglog.adding_training.adding_exercise.Intensity.ZoneOne;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class ExerciseLoadTest {

    @Mock
    TrainingExerciseJoinViewModel trainingExerciseJoinViewModelMock;

    @Mock
    LifecycleOwner ownerMock;


    @Test
    public void calculate() {
        Date firstTrainingDate =  new GregorianCalendar(2019, Calendar.JUNE, 1).getTime();
        Date secondTrainingDate =  new GregorianCalendar(2019, Calendar.JUNE, 2).getTime();

        Training t1 = new Training("1", firstTrainingDate, 100, 200, 2);
        Training t2 = new Training("2", secondTrainingDate, 200, 300, 1);
        List<Training> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);

        Date firstDayOfOccurrence = new GregorianCalendar(2019, Calendar.JUNE, 22).getTime();
        int exerciseDistance = 25;
        int exerciseRepetitions = 2;
        int exerciseSeries = 1;
        Intensity intensity = ZoneOne;
        Exercise exercise = new Exercise("1", ExerciseType.General,
                exerciseDistance, intensity,"1", exerciseRepetitions, exerciseSeries, firstDayOfOccurrence);

        ExerciseLoad exerciseLoad = new ExerciseLoad(exercise, firstDayOfOccurrence, trainingExerciseJoinViewModelMock, ownerMock);
        exerciseLoad.calculate(list);

        int neurologicalBoost = 8;
        Integer expected = exerciseDistance*exerciseRepetitions*exerciseSeries*neurologicalBoost * intensity.ordinal()+1;
        assertEquals (exerciseDistance*exerciseRepetitions*exerciseSeries*neurologicalBoost, exercise.loadValue );
    }
}