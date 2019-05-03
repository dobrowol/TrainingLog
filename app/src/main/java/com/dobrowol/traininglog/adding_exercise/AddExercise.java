package com.dobrowol.traininglog.adding_exercise;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dobrowol.traininglog.R;

public class AddExercise extends AppCompatActivity implements View.OnClickListener {
    public static final String REQUESTED_CODE = "exercise";
    public static int CREATE_EXERCISE=1;
    Button btnSubmit;
    EditText distance, numberOfRepetitions, numberOfSets, description;
    TextView result;
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
        btnSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (distance.getText().toString().isEmpty() || numberOfRepetitions.getText().toString().isEmpty() || numberOfSets.getText().toString().isEmpty() || description.getText().toString().isEmpty()) {
            result.setText(R.string.WarningSomeEmpty);
        } else {
            result.setText(R.string.distance + distance.getText().toString() + " \n" + R.string.number_of_repetitions + numberOfRepetitions.getText().toString()
                    + " \n" + R.string.number_of_sets + numberOfSets.getText().toString() + " \n" + R.string.description + description.getText().toString()
                    + " \n");
            Exercise exercise = new Exercise();
            exercise.description=description.getText().toString();
            exercise.distance=Integer.valueOf(distance.getText().toString());
            exercise.numberOfRepetitionsInSet=Integer.valueOf(numberOfRepetitions.getText().toString());
            exercise.numberOfSetsInSeries=Integer.valueOf(numberOfSets.getText().toString());
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(REQUESTED_CODE,exercise);
            intent.putExtras(bundle);
            setResult(RESULT_OK,intent);
            finish();
        }
    }
}
