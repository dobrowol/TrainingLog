package com.dobrowol.traininglog.adding_training.adding_exercise;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.dobrowol.traininglog.R;

import java.util.UUID;

public class AddExerciseDescription extends AppCompatActivity implements View.OnClickListener {
    public static final String REQUESTED_CODE = "exercise_description";
    public static int CREATE_EXERCISE_DESCRIPTION=1;
    Button btnSubmit;
    EditText description, specificity;

    private ExerciseDescriptionViewModel exerciseDescriptionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_exercise_description_layout);

        description = findViewById(R.id.txtExerciseDescription);
        specificity = findViewById(R.id.txtSpecificity);
        specificity.setHint("Specificity from 1 to 5");

        btnSubmit = findViewById(R.id.btnSend);
        btnSubmit.setOnClickListener(this);

        exerciseDescriptionViewModel = ViewModelProviders.of(this).get(ExerciseDescriptionViewModel.class);
    }

    @Override
    public void onClick(View view) {
        if ( description.getText().toString().isEmpty() || specificity.getText().toString().isEmpty()) {
            Toast.makeText(this, "Not all fields are filled.", Toast.LENGTH_SHORT).show();
        } else {

            ExerciseDescription exerciseDescription = new ExerciseDescription();
            exerciseDescription.eid = UUID.randomUUID().toString();
            exerciseDescription.description = description.getText().toString();
            Integer specificityNumber = Integer.parseInt(specificity.getText().toString()) - 1;
            if(specificityNumber >4 ){
                specificityNumber = 4;
            }
            else if(specificityNumber <0)
            {
                specificityNumber = 0;
            }

            exerciseDescription.specificity = Specificity.values()[specificityNumber];

            exerciseDescriptionViewModel.insert(exerciseDescription);

            finish();
        }
    }



}
