package com.dobrowol.traininglog.adding_exercise;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dobrowol.traininglog.R;

public class AddExercise extends AppCompatActivity {
    public static final String REQUESTED_CODE = "exercise";
    public static int CREATE_EXERCISE=1;
    Button btnSubmit;
    EditText name, password, email, dob, phoneno;
    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name=(EditText)findViewById(R.id.txtDistance);
        password = (EditText)findViewById(R.id.txtNumberOfRepetitions);
        email = (EditText)findViewById(R.id.txtNumberOfSets);
        dob = (EditText)findViewById(R.id.txtNumberOfSeries);
        phoneno= (EditText)findViewById(R.id.txtPhone);
        btnSubmit = (Button)findViewById(R.id.btnSend);
        result = (TextView)findViewById(R.id.resultView);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty() || password.getText().toString().isEmpty() || email.getText().toString().isEmpty() || dob.getText().toString().isEmpty()
                        || phoneno.getText().toString().isEmpty()) {
                    result.setText("Please Fill All the Details");
                } else {
                    result.setText("Name -  " + name.getText().toString() + " \n" + "Password -  " + password.getText().toString()
                            + " \n" + "E-Mail -  " + email.getText().toString() + " \n" + "DOB -  " + dob.getText().toString()
                            + " \n" + "Contact -  " + phoneno.getText().toString());
                }
            }
        });
    }
}
