package com.dobrowol.traininglog.adding_training.adding_exercise;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dobrowol.traininglog.R;
import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.TrainingExerciseJoin;
import com.dobrowol.traininglog.adding_training.TrainingExerciseJoinViewModel;
import com.dobrowol.traininglog.adding_training.TrainingViewModel;
import com.dobrowol.traininglog.adding_training.adding_goal.Goal;
import com.dobrowol.traininglog.adding_training.adding_goal.GoalExercise;
import com.dobrowol.traininglog.adding_training.adding_goal.GoalExerciseJoinViewModel;
import com.dobrowol.traininglog.adding_training.adding_goal.GoalViewModel;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalExerciseJoin;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalExerciseJoinViewModel;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalJoin;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalJoinViewModel;
import com.dobrowol.traininglog.training_load.calculating.TrainingGoalLoad;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AddExercise extends AppCompatActivity implements View.OnClickListener, Observer<List<ExerciseDescription>>, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    public static final String TRAINING = "training";
    public static final String GOAL = "goal";
    public static int CREATE_EXERCISE=1;
    Button btnSubmit, btnAddDescription;
    EditText distance, numberOfRepetitions, numberOfSets, intensity;
    TextView result;
    AutoCompleteTextView description;
    private ExerciseDescriptionViewModel exerciseDescriptionViewModel;
    private TrainingExerciseJoinViewModel trainingExerciseJoinViewModel;
    private TrainingGoalExerciseJoinViewModel trainingGoalExerciseJoinViewModel;
    private TrainingGoalJoinViewModel trainingGoalJoinViewModel;
    private TrainingViewModel trainingViewModel;
    private GoalViewModel goalViewModel;
    private ExerciseViewModel exerciseViewModel;
    private ArrayList<ExerciseDescription> exerciseDescription;
    private String exerciseDescriptionId;
    private Training training;
    private int numberOfExercises;
    private ExerciseType exerciseType;
    private TrainingExerciseJoin trainingExerciseJoin;
    private Exercise exercise;
    private Drawable originalEditTextBackground;
    private Goal goal;
    private EditText specificity;
    private GoalExerciseJoinViewModel goalExerciseJoinViewModel;
    private TrainingGoalJoin trainingGoalJoin;

    public static void startNewInstance(Context context, Training training, Goal goal){
        Intent intent = new Intent(context, AddExercise.class);

        intent.putExtra(AddExercise.TRAINING, training.id);

        intent.putExtra(AddExercise.GOAL, goal.goalId);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_exercise_layout);
        distance = findViewById(R.id.txtDistance);
        numberOfRepetitions = findViewById(R.id.txtNumberOfRepetitions);
        numberOfSets = findViewById(R.id.txtNumberOfSets);
        description = findViewById(R.id.txtDescription);
        intensity = findViewById(R.id.txtIntensity);
        specificity = findViewById(R.id.txtSpecificity);

        distance.setOnClickListener(this);
        numberOfRepetitions.setOnClickListener(this);
        numberOfSets.setOnClickListener(this);
        description.setOnClickListener(this);
        intensity.setOnClickListener(this);
        specificity.setOnClickListener(this);

        originalEditTextBackground = distance.getBackground();

        btnSubmit = findViewById(R.id.btnSend);
        btnAddDescription = findViewById(R.id.btnAddDescription);
        btnSubmit.setOnClickListener(this);
        btnAddDescription.setOnClickListener(this);

        exerciseDescriptionViewModel = ViewModelProviders.of(this).get(ExerciseDescriptionViewModel.class);
        exerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        trainingExerciseJoinViewModel = ViewModelProviders.of(this).get(TrainingExerciseJoinViewModel.class);
        trainingGoalExerciseJoinViewModel = ViewModelProviders.of(this).get(TrainingGoalExerciseJoinViewModel.class);
        trainingViewModel = ViewModelProviders.of(this).get(TrainingViewModel.class);
        goalViewModel = ViewModelProviders.of(this).get(GoalViewModel.class);
        goalExerciseJoinViewModel = ViewModelProviders.of(this).get(GoalExerciseJoinViewModel.class);
        trainingGoalJoinViewModel = ViewModelProviders.of(this).get(TrainingGoalJoinViewModel.class);

        exerciseDescriptionViewModel.getAllExercisesDescriptions().observe(this, this);

        numberOfExercises = 0;
        exerciseType = ExerciseType.General;
        Intent intent = getIntent();
        if (intent != null ) {

                String trainingId = intent.getStringExtra(AddExercise.TRAINING);

                String goalId =  intent.getStringExtra(AddExercise.GOAL);

                trainingViewModel.getTraining(trainingId).observe(this, training1 -> {
                    training = training1;
                    setAppBarTitle();
                    setTrainingGoal();
                });

                goalViewModel.getGoalById(goalId).observe(this, goal1 -> {
                    goal = goal1;
                    setAppBarTitle();
                    setTrainingGoal();
                });

        }
        trainingGoalJoin = null;

        trainingGoalJoinViewModel.trainingGoal.observe(this, trainingGoalJoin1 -> trainingGoalJoin=trainingGoalJoin1);
    }

    private void setTrainingGoal() {
        if(goal != null && training != null){
            trainingGoalJoinViewModel.getTrainingGoal(training.id, goal.goalId);
        }
    }

    private void setAppBarTitle() {
        if(training != null && goal != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM, dd", Locale.ENGLISH);
            String formatted = sdf.format(training.date);
            formatted += " " + goal.description;
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(formatted);
            } else {
                android.app.ActionBar actionBar1 = getActionBar();
                if (actionBar1 != null) {
                    actionBar1.setTitle(formatted);
                }
            }
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnSend:
            if (distance.getText().toString().isEmpty() || numberOfRepetitions.getText().toString().isEmpty()  || description.getText().toString().isEmpty() || isNumberInvalid(intensity,1,5)
            || isNumberInvalid(specificity,1,5)) {
                String errorMessage = "";
                if (distance.getText().toString().isEmpty()){errorMessage = "distance cannot be empty";distance.setBackgroundResource(R.drawable.edit_text_error_background);}
                if (numberOfRepetitions.getText().toString().isEmpty()){errorMessage = "number of repetitions cannot be empty";numberOfRepetitions.setBackgroundResource(R.drawable.edit_text_error_background);}
                if (description.getText().toString().isEmpty()){errorMessage = "description cannot be empty";description.setBackgroundResource(R.drawable.edit_text_error_background);}
                if (isNumberInvalid(intensity, 1, 5)){errorMessage = "intensity should be from 1 to 5";intensity.setBackgroundResource(R.drawable.edit_text_error_background);}
                if (isNumberInvalid(specificity, 1, 5)){errorMessage = "specificity should be from 1 to 5";specificity.setBackgroundResource(R.drawable.edit_text_error_background);}

                Toast.makeText(this, errorMessage,Toast.LENGTH_SHORT).show();
            } else {
                btnSubmit.setEnabled(false);
                btnSubmit.setOnClickListener(null);
                int numOfSets = getNumber(numberOfSets);

                exercise = new Exercise(UUID.randomUUID().toString(), getNumber(distance), Intensity.values()[getNumber(intensity)-1], exerciseDescriptionId,
                        getNumber(numberOfRepetitions), numOfSets, new Date());
                exercise.calculateLoad();

                exerciseViewModel.insert(exercise).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                        s -> {


                        int nextExercise = numberOfExercises + 1;
                        trainingExerciseJoin = new TrainingExerciseJoin(UUID.randomUUID().toString(), exercise.id, training.id, nextExercise);
                        GoalExercise goalExercise = new GoalExercise(UUID.randomUUID().toString(), goal.goalId, exercise.id, getNumber(specificity));
                        goalExerciseJoinViewModel.insert(goalExercise);
                        trainingExerciseJoinViewModel.insert(trainingExerciseJoin);
                        TrainingGoalLoad trainingGoalLoad = new TrainingGoalLoad(trainingGoalJoin, trainingGoalJoinViewModel);
                        int exerciseLoad = trainingGoalLoad.update(exercise, goalExercise, training);
                        TrainingGoalExerciseJoin trainingGoalExerciseJoin = new TrainingGoalExerciseJoin(UUID.randomUUID().toString(), training.id, goal.goalId, exercise.id, nextExercise, exerciseLoad);
                        trainingGoalExerciseJoinViewModel.insert(trainingGoalExerciseJoin).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                                s1 -> finish());

                });
            }
            break;
            case R.id.btnAddDescription:
                Intent intent = new Intent(this,AddExerciseDescription.class);
                startActivityForResult(intent,AddExerciseDescription.CREATE_EXERCISE_DESCRIPTION);
                break;
            case R.id.txtDescription:
                description.setBackground(originalEditTextBackground);
                break;
            case R.id.txtIntensity:
                intensity.setBackground(originalEditTextBackground);
                break;
            case R.id.txtNumberOfRepetitions:
                numberOfRepetitions.setBackground(originalEditTextBackground);
                break;
            case R.id.txtNumberOfSets:
                numberOfSets.setBackground(originalEditTextBackground);
                break;
        }
    }

    private boolean isNumberInvalid(EditText field, int lowerBound, int upperBound) {

        int res = getNumber(field);
        return res < lowerBound || res > upperBound;
    }

    private int getNumber(EditText editText) {
        int res = 0;
        try{
            if(editText.getText().toString().compareTo("") != 0) {
                res = Integer.parseInt(editText.getText().toString());
            }
        }catch (NumberFormatException ex){
            ex.printStackTrace();
        }
        return res;
    }

    @Override
    public void onChanged(List<ExerciseDescription> exerciseDescriptions) {
                String[] array = new String[exerciseDescriptions.size()];
                for (int i =0; i < exerciseDescriptions.size();i++){
                    array[i] = exerciseDescriptions.get(i).description;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, array);
                description.setAdapter(adapter);
                description.setOnItemSelectedListener(this);
                description.setOnItemClickListener(this);
                adapter.notifyDataSetChanged();
                exerciseDescription = new ArrayList<>(exerciseDescriptions);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        exerciseDescriptionId = exerciseDescription.get(position).eid;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        exerciseDescriptionId = "";
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        exerciseDescriptionId = exerciseDescription.get(position).eid;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
