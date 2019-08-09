package com.dobrowol.traininglog.new_training;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.dobrowol.traininglog.MainActivity;
import com.dobrowol.traininglog.R;
import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.TrainingViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class DateTimeActivity extends AppCompatActivity implements View.OnClickListener, TimePicker.OnTimeChangedListener {
    public final static String TRAINING = "training";
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

        Intent intent = getIntent();
        if(intent != null)
        {
            if(intent.hasExtra(TRAINING)){
                training = (Training) intent.getExtras().getSerializable(TRAINING);
            }
        }
        setDateAndTime();
        setAppBar();
    }

    private void setAppBar() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }else{
            android.app.ActionBar actionBar1 = getActionBar();
            if(actionBar1 != null){
                actionBar1.setDisplayHomeAsUpEnabled(true);

            }
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        MainActivity.startNewInstance(getApplicationContext(), training);
    }

    private void setDateAndTime() {
        if(training != null){
            timePicker.setCurrentHour(training.date.getHours());
            timePicker.setCurrentMinute(training.date.getMinutes());
            datePicker.updateDate(training.date.getYear(), training.date.getMonth(), training.date.getDay());
        }
    }

    public static void startInstance(Context context, Training training) {
        Intent intent = new Intent(context, DateTimeActivity.class);
        Bundle args = new Bundle();
        args.putSerializable(TRAINING, training);
        intent.putExtras(args);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.create_button){
                year = datePicker.getYear();
                month = datePicker.getMonth();
                dayOfMonth = datePicker.getDayOfMonth();
                training = new Training();
                training.id = UUID.randomUUID().toString();

                String date = String.format(Locale.ENGLISH,"%04d-%02d-%02d %02d:%02d", year, month, dayOfMonth, hour, minutes);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
                try {
                    training.date = simpleDateFormat.parse(date);
                    trainingViewModel.update(training);
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
