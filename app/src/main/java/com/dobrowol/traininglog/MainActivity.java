package com.dobrowol.traininglog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dobrowol.traininglog.adding_training.TrainingExerciseJoinViewModel;
import com.dobrowol.traininglog.adding_training.adding_exercise.AddExercise;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseDescription;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseDescriptionViewModel;
import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.TrainingViewModel;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.OnItemClickListener,View.OnTouchListener, View.OnClickListener, TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener, TrainingListViewAdapter.OnItemClickListener, Observer<List<Exercise>> {

    public static final String TRAINING = "training";
    public static final String NUMBER_OF_EXERCISES = "number_of_exercises";
    public static final String EXERCISE_TYPE = "exercise_type";
    private RecyclerView generalRecyclerView;
    private RecyclerView specificRecyclerView;
    private RecyclerView competitiveRecyclerView;
    private MyRecyclerViewAdapter generalAdapter;
    private MyRecyclerViewAdapter specificAdapter;
    private MyRecyclerViewAdapter competitiveAdapter;
    private MyRecyclerViewAdapter currentAdapter;
    private TextView dateTxt;
    private TextView timeTxt;
    ArrayList<Exercise> exerciseList;
    private TrainingViewModel trainingViewModel;
    private TrainingExerciseJoinViewModel trainingExerciseJoinViewModel;
    private Training training;
    private int numberOfExercises;
    TrainingList detailsDialog;
    private ExerciseDescriptionViewModel exerciseDescriptionViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        generalRecyclerView = findViewById(R.id.general_rv);
        specificRecyclerView = findViewById(R.id.specific_rv);
        competitiveRecyclerView = findViewById(R.id.competitive_rv);

        generalRecyclerView.setOnTouchListener(this);
        specificRecyclerView.setOnTouchListener(this);
        competitiveRecyclerView.setOnTouchListener(this);

        trainingViewModel = ViewModelProviders.of(this).get(TrainingViewModel.class);
        trainingExerciseJoinViewModel = ViewModelProviders.of(this).get(TrainingExerciseJoinViewModel.class);
        exerciseDescriptionViewModel = ViewModelProviders.of(this).get(ExerciseDescriptionViewModel.class);

        generalRecyclerView.setBackgroundResource(R.drawable.edit_text_general_background);
        specificRecyclerView.setBackgroundResource(R.drawable.edit_text_no_focus_background);
        competitiveRecyclerView.setBackgroundResource(R.drawable.edit_text_no_focus_background);

        generalRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        generalAdapter = new MyRecyclerViewAdapter(ExerciseType.General);
        generalAdapter.setOnItemClickListener(this);
        generalRecyclerView.setAdapter(generalAdapter);

        specificRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        specificAdapter = new MyRecyclerViewAdapter(ExerciseType.Specific);
        specificAdapter.setOnItemClickListener(this);
        specificRecyclerView.setAdapter(specificAdapter);

        competitiveRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        competitiveAdapter = new MyRecyclerViewAdapter(ExerciseType.Competitive);
        competitiveAdapter.setOnItemClickListener(this);
        competitiveRecyclerView.setAdapter(competitiveAdapter);

        currentAdapter=generalAdapter;

        initializeTraining();
        initializeDate();
        initializeTime();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        FloatingActionButton fabSave = findViewById(R.id.fabSave);
        fabSave.setOnClickListener(this);

        exerciseList = new ArrayList<>();
    }
    private void initializeDate(){
        dateTxt = findViewById(R.id.dateTxt);
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(new Date());
        dateTxt.setText(date);
        dateTxt.setOnClickListener(this);
    }
    private void initializeTime(){
        timeTxt = findViewById(R.id.timeTxt);
        String pattern = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String time = simpleDateFormat.format(new Date());
        timeTxt.setText(time);
        timeTxt.setOnClickListener(this);
    }
    private void initializeTraining(){
        training = new Training();
        training.date = new Date();
        training.id = UUID.randomUUID().toString();
        trainingExerciseJoinViewModel.getAllExercisesForTraining(training.id).observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(@Nullable final List<Exercise> exercises) {
                // Update the cached copy of the exercises in the adapter.
                if (exercises != null) {
                    ArrayList<Exercise> array = new ArrayList<>(exercises);
                    numberOfExercises = array.size();
                    generalAdapter.setExerciseList(array);
                    specificAdapter.setExerciseList(array);
                    competitiveAdapter.setExerciseList(array);

                    generalAdapter.notifyDataSetChanged();
                    specificAdapter.notifyDataSetChanged();
                    competitiveAdapter.notifyDataSetChanged();

                }
            }
        });
        exerciseDescriptionViewModel.getAllExercisesDescriptions().observe(this, new Observer<List<ExerciseDescription>>() {
            @Override
            public void onChanged(@Nullable final List<ExerciseDescription> exercises) {
                if (exercises != null) {
                    // Update the cached copy of the exercises in the adapter.
                    ArrayList<ExerciseDescription> array = new ArrayList<>(exercises);
                    generalAdapter.setExerciseDescriptionList(array);
                    specificAdapter.setExerciseDescriptionList(array);
                    competitiveAdapter.setExerciseDescriptionList(array);
                }
            }
        });

        numberOfExercises = 0;
        trainingViewModel.insert(training);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.general_rv:
                currentAdapter = generalAdapter;
                generalRecyclerView.setBackgroundResource(R.drawable.edit_text_general_background);
                specificRecyclerView.setBackgroundResource(R.drawable.edit_text_no_focus_background);
                competitiveRecyclerView.setBackgroundResource(R.drawable.edit_text_no_focus_background);
                break;
            case R.id.specific_rv:
                currentAdapter = specificAdapter;
                specificRecyclerView.setBackgroundResource(R.drawable.edit_text_general_background);
                generalRecyclerView.setBackgroundResource(R.drawable.edit_text_no_focus_background);
                competitiveRecyclerView.setBackgroundResource(R.drawable.edit_text_no_focus_background);
                break;
            case R.id.competitive_rv:
                currentAdapter = competitiveAdapter;
                competitiveRecyclerView.setBackgroundResource(R.drawable.edit_text_general_background);
                specificRecyclerView.setBackgroundResource(R.drawable.edit_text_no_focus_background);
                generalRecyclerView.setBackgroundResource(R.drawable.edit_text_no_focus_background);
                break;
        }
        exerciseList = currentAdapter.getExerciseList();
        view.performClick();
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:

                Intent intent = new Intent(this,AddExercise.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(TRAINING,training);
                bundle.putInt(NUMBER_OF_EXERCISES, numberOfExercises);
                bundle.putInt(EXERCISE_TYPE, currentAdapter.getExerciseType().ordinal());
                intent.putExtras(bundle);
                startActivityForResult(intent,AddExercise.CREATE_EXERCISE);
                break;
            case R.id.fabSave:
                String date = dateTxt.getText().toString() +" "+ timeTxt.getText().toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try {
                    training.date = simpleDateFormat.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                trainingViewModel.update(training);
                initializeTraining();
                exerciseList.clear();
                Toast.makeText(getApplicationContext(),"training saved",Toast.LENGTH_SHORT).show();
                break;
            case R.id.timeTxt:
                DialogFragment timePickerFragment = new TimePickerFragment(this);
                timePickerFragment.show(getSupportFragmentManager(), "timePicker");
                break;
            case R.id.dateTxt:
                DialogFragment datePickerFragment = new DatePickerFragment(this);
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


    }

    @Override
    public void onItemClick(Exercise item) {

    }
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("generalExerciseList", generalAdapter.getExerciseList());
        outState.putParcelableArrayList("specificExerciseList", specificAdapter.getExerciseList());
        outState.putParcelableArrayList("competitiveExerciseList", competitiveAdapter.getExerciseList());
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState){
        super.onRestoreInstanceState(inState);
        generalAdapter.setExerciseList(inState.<Exercise>getParcelableArrayList("generalExerciseList"));
        specificAdapter.setExerciseList(inState.<Exercise>getParcelableArrayList("specificExerciseList"));
        competitiveAdapter.setExerciseList(inState.<Exercise>getParcelableArrayList("competitiveExerciseList"));
        currentAdapter=generalAdapter;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_list:
                detailsDialog = new TrainingList(this);
                detailsDialog.show(getSupportFragmentManager(), "timePicker");
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar, menu);

        return true;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String time = String.format("%02d::%02d", hourOfDay, minute);
        timeTxt.setText(time);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = String.format("%04d-%02d-%02d", year, month, dayOfMonth);
        dateTxt.setText(date);
    }

    @Override
    public void onItemClick(Training item) {
        detailsDialog.dismiss();
        trainingExerciseJoinViewModel.getAllExercisesForTraining(item.id).observe(this, this);
    }

    @Override
    public void onItemRemove(Training training) {
        detailsDialog.dismiss();
        trainingViewModel.delete(training);
    }

    @Override
    public void onChanged(List<Exercise> exercises) {
       ArrayList<Exercise> ex = new ArrayList(exercises);
        generalAdapter.setExerciseList(ex);
        specificAdapter.setExerciseList(ex);
        competitiveAdapter.setExerciseList(ex);
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        TimePickerDialog.OnTimeSetListener listener;
        TimePickerFragment(TimePickerDialog.OnTimeSetListener listener){
            this.listener = listener;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute,
                    android.text.format.DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            listener.onTimeSet(view,hourOfDay,minute);
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        private DatePickerDialog.OnDateSetListener listener;

        DatePickerFragment(DatePickerDialog.OnDateSetListener listener){
            this.listener = listener;
        }
        @Override
        public Dialog onCreateDialog( Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            listener.onDateSet(view, year, month, day);
        }
    }

    public static class TrainingList extends DialogFragment implements Observer<List<Training>> {
        private TrainingViewModel trainingViewModel;
        private RecyclerView trainingListRv;
        private TrainingListViewAdapter.OnItemClickListener listener;

        TrainingList(TrainingListViewAdapter.OnItemClickListener listener){
            this.listener = listener;
        }
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.training_list_fragment, container, false);

            trainingListRv = v.findViewById(R.id.training_list_rv);
            trainingViewModel = ViewModelProviders.of(this).get(TrainingViewModel.class);
            trainingViewModel.getAllTrainings().observe(this, this);
            // Do all the stuff to initialize your custom view

            return v;
        }

        @Override
        public void onChanged(List<Training> trainings) {

            trainingListRv.setLayoutManager(new LinearLayoutManager(getActivity()));
            TrainingListViewAdapter generalAdapter = new TrainingListViewAdapter(listener);
            generalAdapter.setOnItemClickListener(listener);
            generalAdapter.setTrainings(new ArrayList<>(trainings));
            trainingListRv.setAdapter(generalAdapter);
            }
        }
    }


