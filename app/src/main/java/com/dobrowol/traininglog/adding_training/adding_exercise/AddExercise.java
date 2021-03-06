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
import io.reactivex.schedulers.Schedulers;


public class AddExercise extends AppCompatActivity implements View.OnClickListener, Observer<List<ExerciseDescription>>, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {


    interface ExerciseManipulationState{
        void start();
        void end();
    }
    private class AddExerciseState implements ExerciseManipulationState{

        @Override
        public void start() {
            btnSubmit.setText(getString(R.string.add_exercise_text));
        }

        @Override
        public void end() {
            addExercise();
        }
    }

    private class UpdateExerciseState implements ExerciseManipulationState{

        @Override
        public void start() {
            btnSubmit.setText(getString(R.string.updateBtn));
        }

        @Override
        public void end() {
            updateExercise();
        }
    }

    private void removeExercise() {
        if(exercise != null && exerciseViewModel != null) {
            exerciseViewModel.delete(exercise);
        }
    }

    public static final String TRAINING = "training";
    public static final String GOAL = "goal";
    private static final String EXERCISE = "exercise";
    Button btnSubmit, btnAddDescription;
    EditText distance, numberOfRepetitions, numberOfSets, intensity;
    AutoCompleteTextView description;
    private ExerciseDescriptionViewModel exerciseDescriptionViewModel;
    private TrainingGoalExerciseJoinViewModel trainingGoalExerciseJoinViewModel;
    private TrainingGoalJoinViewModel trainingGoalJoinViewModel;
    private TrainingViewModel trainingViewModel;
    private GoalViewModel goalViewModel;
    private ExerciseViewModel exerciseViewModel;
    private ArrayList<ExerciseDescription> exerciseDescription;
    private String exerciseDescriptionId;
    private Training training;
    private int numberOfExercises;
    private Exercise exercise;
    private Drawable originalEditTextBackground;
    private String goalId;
    private EditText specificity;
    private GoalExerciseJoinViewModel goalExerciseJoinViewModel;
    private ExerciseManipulationState exerciseManipulationState;

    public static void startNewInstance(Context context, Training training, Goal goal){
        Intent intent = new Intent(context, AddExercise.class);

        intent.putExtra(AddExercise.TRAINING, training.id);

        intent.putExtra(AddExercise.GOAL, goal.goalId);
        context.startActivity(intent);
    }

    public static void startNewInstance(Context context, Training training, Goal goal, Exercise item) {
        Intent intent = new Intent(context, AddExercise.class);

        intent.putExtra(AddExercise.TRAINING, training.id);

        intent.putExtra(AddExercise.GOAL, goal.goalId);
        intent.putExtra(AddExercise.EXERCISE, item.id);
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
        trainingGoalExerciseJoinViewModel = ViewModelProviders.of(this).get(TrainingGoalExerciseJoinViewModel.class);
        trainingViewModel = ViewModelProviders.of(this).get(TrainingViewModel.class);
        goalViewModel = ViewModelProviders.of(this).get(GoalViewModel.class);
        goalExerciseJoinViewModel = ViewModelProviders.of(this).get(GoalExerciseJoinViewModel.class);
        trainingGoalJoinViewModel = ViewModelProviders.of(this).get(TrainingGoalJoinViewModel.class);

        exerciseDescriptionViewModel.getAllExercisesDescriptions().observe(this, this);


        numberOfExercises = 0;
        Intent intent = getIntent();
        if (intent != null ) {

                String trainingId = intent.getStringExtra(AddExercise.TRAINING);

                goalId =  intent.getStringExtra(AddExercise.GOAL);

                String exerciseId = intent.getStringExtra(AddExercise.EXERCISE);

                if(exerciseId == null){
                    exerciseManipulationState = new AddExerciseState();
                }
                else{
                    exerciseManipulationState = new UpdateExerciseState();
                    exerciseViewModel.getExerciseById(exerciseId).observe(this, exercise1 ->{
                        if(exercise1 != null) {
                            exercise = exercise1;
                            fillFields();
                            btnSubmit.setText(getString(R.string.updateBtn));
                        }
                    });
                }
                exerciseManipulationState.start();

                trainingViewModel.getTraining(trainingId).observe(this, training1 -> {
                    training = training1;
                    setAppBarTitle();
                });



        }
    }


    private void fillFields() {
        distance.setText(String.valueOf(exercise.distance));
        intensity.setText(String.valueOf(exercise.intensity.ordinal()));
        numberOfSets.setText(String.valueOf(exercise.numberOfSetsInSeries));
        numberOfRepetitions.setText(String.valueOf(exercise.numberOfRepetitionsInSet));
    }



    private void setAppBarTitle() {
        if(training != null ) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM, dd", Locale.ENGLISH);
            String formatted = sdf.format(training.date);
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
                if (isNumberInvalid(intensity, 1, 20)){errorMessage = "intensity should be from 1 to 20";intensity.setBackgroundResource(R.drawable.edit_text_error_background);}
                if (isNumberInvalid(specificity, 1, 5)){errorMessage = "specificity should be from 1 to 5";specificity.setBackgroundResource(R.drawable.edit_text_error_background);}

                Toast.makeText(this, errorMessage,Toast.LENGTH_SHORT).show();
            } else {
                btnSubmit.setEnabled(false);
                btnSubmit.setOnClickListener(null);
                exerciseManipulationState.end();
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

    private void addExercise() {
        int numOfSets = getNumber(numberOfSets);

        if(exerciseDescriptionId == null || exerciseDescriptionId.equals("")){
            if(!description.getText().toString().equals("")){
                ExerciseDescription exerciseDescription = new ExerciseDescription();
                exerciseDescription.description = description.getText().toString();
                exerciseDescription.eid = UUID.randomUUID().toString();
                exerciseDescriptionId = exerciseDescription.eid;
                exerciseDescriptionViewModel.insert(exerciseDescription);
            }
        }
        exercise = new Exercise(UUID.randomUUID().toString(), getNumber(distance), Intensity.values()[getNumber(intensity)-1], exerciseDescriptionId,
                getNumber(numberOfRepetitions), numOfSets, new Date());
        exercise.calculateLoad();

        exerciseViewModel.insert(exercise).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                s -> {
                int nextExercise = numberOfExercises + 1;
                TrainingGoalExerciseJoin trainingGoalExerciseJoin = new TrainingGoalExerciseJoin(UUID.randomUUID().toString(), training.id, goalId, exercise.id, nextExercise, getNumber(specificity));
                trainingGoalExerciseJoinViewModel.insert(trainingGoalExerciseJoin).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                        s1 -> finish());

        });
    }

    private void updateExercise() {
        int numOfSets = getNumber(numberOfSets);

        if(exerciseDescriptionId == null || exerciseDescriptionId.equals("")){
            if(!description.getText().toString().equals("")){
                ExerciseDescription exerciseDescription = new ExerciseDescription();
                exerciseDescription.description = description.getText().toString();
                exerciseDescription.eid = UUID.randomUUID().toString();
                exerciseDescriptionId = exerciseDescription.eid;
                exerciseDescriptionViewModel.insert(exerciseDescription);
            }
        }
        exercise = new Exercise(exercise.id, getNumber(distance), Intensity.values()[getNumber(intensity)-1], exerciseDescriptionId,
                getNumber(numberOfRepetitions), numOfSets, new Date());
        exercise.calculateLoad();

        exerciseViewModel.update(exercise);

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
        String name = parent.getItemAtPosition(position).toString();
        for(ExerciseDescription exerciseDescription: this.exerciseDescription){
            if(exerciseDescription.description.equals(name)){
                exerciseDescriptionId = exerciseDescription.eid;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        exerciseDescriptionId = "";
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String name = parent.getItemAtPosition(position).toString();
        for(ExerciseDescription exerciseDescription: this.exerciseDescription){
            if(exerciseDescription.description.equals(name)){
                exerciseDescriptionId = exerciseDescription.eid;
            }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
