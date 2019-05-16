package com.dobrowol.traininglog.adding_exercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dobrowol.traininglog.R;

import java.util.List;

public class AddExercise extends AppCompatActivity implements View.OnClickListener, Observer<List<ExerciseDescription>> {
    public static final String REQUESTED_CODE = "exercise";
    public static int CREATE_EXERCISE=1;
    Button btnSubmit, btnAddDescription;
    EditText distance, numberOfRepetitions, numberOfSets;
    TextView result;
    AutoCompleteTextView description;
    private ExerciseDescriptionViewModel exerciseViewModel;

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

        exerciseViewModel = ViewModelProviders.of(this).get(ExerciseDescriptionViewModel.class);

        exerciseViewModel.getAllExercisesDescriptions().observe(this, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnSend:
            if (distance.getText().toString().isEmpty() || numberOfRepetitions.getText().toString().isEmpty() || numberOfSets.getText().toString().isEmpty() || description.getText().toString().isEmpty()) {
                result.setText(R.string.WarningSomeEmpty);
            } else {
                Exercise exercise = new Exercise();
                exercise.exerciseDescriptionId = exerciseViewModel.getExerciseDescription(description.getText().toString()).getValue().eid;
                exercise.distance = Integer.valueOf(distance.getText().toString());
                exercise.numberOfRepetitionsInSet = Integer.valueOf(numberOfRepetitions.getText().toString());
                exercise.numberOfSetsInSeries = Integer.valueOf(numberOfSets.getText().toString());
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
                adapter.notifyDataSetChanged();
    }

}
