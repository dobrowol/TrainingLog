package com.dobrowol.traininglog.new_training;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.dobrowol.traininglog.R;
import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.TrainingViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class DateTimeActivity extends AppCompatActivity implements View.OnClickListener, TimePicker.OnTimeChangedListener {
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button createButton;
    private Training training;
    private TrainingViewModel trainingViewModel;
    private int year;
    private int month;
    private int dayOfMonth;
    private int hour;
    private int minutes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.date_time_picker);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        createButton = findViewById(R.id.create_button);
        createButton.setOnClickListener(this);
        timePicker.setOnTimeChangedListener(this);
        trainingViewModel = ViewModelProviders.of(this).get(TrainingViewModel.class);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.create_button){
                year = datePicker.getYear();
                month = datePicker.getMonth();
                dayOfMonth = datePicker.getDayOfMonth();
                training = new Training();
                training.id = UUID.randomUUID().toString();

                String date = String.format("%04d-%02d-%02d %02d:%02d", year, month, dayOfMonth, hour, minutes);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try {
                    training.date = simpleDateFormat.parse(date);
                    trainingViewModel.insert(training);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                finish();
        }
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        hour = hourOfDay;
        minutes = minute;
    }
}
