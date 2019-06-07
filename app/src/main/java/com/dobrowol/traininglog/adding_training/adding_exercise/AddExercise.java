package com.dobrowol.traininglog.adding_training.adding_exercise;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dobrowol.traininglog.MainActivity;
import com.dobrowol.traininglog.R;
import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.TrainingExerciseJoin;
import com.dobrowol.traininglog.adding_training.TrainingExerciseJoinViewModel;
import com.dobrowol.traininglog.adding_training.TrainingViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AddExercise extends AppCompatActivity implements View.OnClickListener, Observer<List<ExerciseDescription>>, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    public static final String REQUESTED_CODE = "exercise";
    public static int CREATE_EXERCISE=1;
    Button btnSubmit, btnAddDescription;
    EditText distance, numberOfRepetitions, numberOfSets, intensity;
    TextView result;
    AutoCompleteTextView description;
    private ExerciseDescriptionViewModel exerciseDescriptionViewModel;
    private TrainingExerciseJoinViewModel trainingExerciseJoinViewModel;
    private TrainingViewModel trainingViewModel;
    private ExerciseViewModel exerciseViewModel;
    private ArrayList<ExerciseDescription> exerciseDescription;
    private String exerciseDescriptionId;
    private Training training;
    private int numberOfExercises;
    private ExerciseType exerciseType;
    private TrainingExerciseJoin trainingExerciseJoin;
    private Exercise exercise;
    private Drawable originalEditTextBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_exercise_layout);
        distance = findViewById(R.id.txtDistance);
        numberOfRepetitions = findViewById(R.id.txtNumberOfRepetitions);
        numberOfSets = findViewById(R.id.txtNumberOfSets);
        description = findViewById(R.id.txtDescription);
        intensity = findViewById(R.id.txtIntensity);

        distance.setOnClickListener(this);
        numberOfRepetitions.setOnClickListener(this);
        numberOfSets.setOnClickListener(this);
        description.setOnClickListener(this);
        intensity.setOnClickListener(this);

        originalEditTextBackground = distance.getBackground();

        btnSubmit = findViewById(R.id.btnSend);
        btnAddDescription = findViewById(R.id.btnAddDescription);
        btnSubmit.setOnClickListener(this);
        btnAddDescription.setOnClickListener(this);

        exerciseDescriptionViewModel = ViewModelProviders.of(this).get(ExerciseDescriptionViewModel.class);
        exerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        trainingExerciseJoinViewModel = ViewModelProviders.of(this).get(TrainingExerciseJoinViewModel.class);
        trainingViewModel = ViewModelProviders.of(this).get(TrainingViewModel.class);

        exerciseDescriptionViewModel.getAllExercisesDescriptions().observe(this, this);

        training = new Training();
        numberOfExercises = 0;
        exerciseType = ExerciseType.General;
        Intent intent = getIntent();
        if (intent != null ) {
            if(intent.hasExtra(MainActivity.TRAINING)) {
                training = (Training) intent.getExtras().getSerializable(MainActivity.TRAINING);
            }
            if(intent.hasExtra(MainActivity.NUMBER_OF_EXERCISES)){
                numberOfExercises = intent.getExtras().getInt(MainActivity.NUMBER_OF_EXERCISES);
            }
            if(intent.hasExtra(MainActivity.EXERCISE_TYPE)){
                exerciseType = ExerciseType.values()[intent.getExtras().getInt(MainActivity.EXERCISE_TYPE)];
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnSend:
            if (distance.getText().toString().isEmpty() || numberOfRepetitions.getText().toString().isEmpty()  || description.getText().toString().isEmpty() || isIntensityInvalid()) {
                String errorMessage = "";
                if (distance.getText().toString().isEmpty()){errorMessage = "distance cannot be empty";distance.setBackgroundResource(R.drawable.edit_text_error_background);}
                if (numberOfRepetitions.getText().toString().isEmpty()){errorMessage = "number of repetitions cannot be empty";numberOfRepetitions.setBackgroundResource(R.drawable.edit_text_error_background);}
                if (description.getText().toString().isEmpty()){errorMessage = "description cannot be empty";description.setBackgroundResource(R.drawable.edit_text_error_background);}
                if (isIntensityInvalid()){errorMessage = "intensity should be from 1 to 5";intensity.setBackgroundResource(R.drawable.edit_text_error_background);}

                Toast.makeText(this, errorMessage,Toast.LENGTH_SHORT).show();
            } else {
                int numOfSets = getNumber(numberOfSets);

                exercise = new Exercise(UUID.randomUUID().toString(), exerciseType, getNumber(distance), Intensity.values()[getNumber(intensity)-1], exerciseDescriptionId,
                        getNumber(numberOfRepetitions), numOfSets, new Date());

                exerciseViewModel.insert(exercise);
                int nextExercise = numberOfExercises+1;
                trainingExerciseJoin = new TrainingExerciseJoin(UUID.randomUUID().toString(), exercise.id, training.id, nextExercise);
                trainingViewModel.getTraining(training.id).observe(this, new Observer<Training>() {
                    @Override
                    public void onChanged(Training t) {
                        if(t == null){
                            trainingViewModel.insert(training);
                        }
                        trainingExerciseJoinViewModel.insert(trainingExerciseJoin);
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();

                    }
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

    private boolean isIntensityInvalid() {

        int res = getNumber(intensity);
        if(res>=1&&res<=5){
            return false;
        }
        return true;
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

                // Update the cached copy of the words in the adapter.
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
