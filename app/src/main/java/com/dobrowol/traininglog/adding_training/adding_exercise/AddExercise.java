package com.dobrowol.traininglog.adding_training.adding_exercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dobrowol.traininglog.MainActivity;
import com.dobrowol.traininglog.R;
import com.dobrowol.traininglog.adding_training.TrainingExerciseJoin;
import com.dobrowol.traininglog.adding_training.TrainingExerciseJoinViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddExercise extends AppCompatActivity implements View.OnClickListener, Observer<List<ExerciseDescription>>, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    public static final String REQUESTED_CODE = "exercise";
    public static int CREATE_EXERCISE=1;
    Button btnSubmit, btnAddDescription;
    EditText distance, numberOfRepetitions, numberOfSets;
    TextView result;
    AutoCompleteTextView description;
    private ExerciseDescriptionViewModel exerciseDescriptionViewModel;
    private TrainingExerciseJoinViewModel trainingExerciseJoinViewModel;
    private ExerciseViewModel exerciseViewModel;
    private ArrayList<ExerciseDescription> exerciseDescription;
    private String exerciseDescriptionId;
    private String trainingId;
    private int numberOfExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_exercise_layout);
        distance = findViewById(R.id.txtDistance);
        numberOfRepetitions = findViewById(R.id.txtNumberOfRepetitions);
        numberOfSets = findViewById(R.id.txtNumberOfSets);
        description = findViewById(R.id.txtDescription);

        btnSubmit = findViewById(R.id.btnSend);
        result = findViewById(R.id.resultView);
        btnAddDescription = findViewById(R.id.btnAddDescription);
        btnSubmit.setOnClickListener(this);
        btnAddDescription.setOnClickListener(this);

        exerciseDescriptionViewModel = ViewModelProviders.of(this).get(ExerciseDescriptionViewModel.class);
        exerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        trainingExerciseJoinViewModel = ViewModelProviders.of(this).get(TrainingExerciseJoinViewModel.class);

        exerciseDescriptionViewModel.getAllExercisesDescriptions().observe(this, this);

        trainingId = "";
        numberOfExercises = 0;
        Intent intent = getIntent();
        if (intent != null & intent.hasExtra(MainActivity.TRAINING_ID)) {
            trainingId = intent.getExtras().getString(MainActivity.TRAINING_ID);
            numberOfExercises = intent.getExtras().getInt(MainActivity.NUMBER_OF_EXERCISES);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnSend:
            if (distance.getText().toString().isEmpty() || numberOfRepetitions.getText().toString().isEmpty()  || description.getText().toString().isEmpty()) {
                result.setText(R.string.WarningSomeEmpty);
            } else {
                Exercise exercise = new Exercise();
                exercise.id = UUID.randomUUID().toString();
                exercise.exerciseDescriptionId = exerciseDescriptionId;
                exercise.distance = Integer.valueOf(distance.getText().toString());

                exercise.numberOfRepetitionsInSet = Integer.valueOf(numberOfRepetitions.getText().toString());
                if(!numberOfSets.getText().toString().equalsIgnoreCase("")) {
                    exercise.numberOfSetsInSeries = Integer.valueOf(numberOfSets.getText().toString());
                }
                exerciseViewModel.insert(exercise);
                int nextExercise = numberOfExercises+1;
                TrainingExerciseJoin trainingExerciseJoin = new TrainingExerciseJoin(exercise.id, trainingId, nextExercise);

                trainingExerciseJoinViewModel.insert(trainingExerciseJoin);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(REQUESTED_CODE, exercise);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
            break;
            case R.id.btnAddDescription:
                Intent intent = new Intent(this,AddExerciseDescription.class);
                startActivityForResult(intent,AddExerciseDescription.CREATE_EXERCISE_DESCRIPTION);
                break;
        }
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
}
