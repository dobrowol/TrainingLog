package com.dobrowol.traininglog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import androidx.appcompat.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dobrowol.traininglog.adding_training.TrainingExerciseJoin;
import com.dobrowol.traininglog.adding_training.TrainingExerciseJoinViewModel;
import com.dobrowol.traininglog.adding_training.adding_exercise.AddExercise;
import com.dobrowol.traininglog.adding_training.adding_exercise.Converters;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseDescription;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseDescriptionViewModel;
import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.TrainingViewModel;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseType;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseViewModel;
import com.dobrowol.traininglog.adding_training.adding_goal.Goal;
import com.dobrowol.traininglog.adding_training.adding_goal.GoalExercisePair;
import com.dobrowol.traininglog.adding_training.adding_goal.GoalViewModel;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalExerciseJoinViewModel;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalJoin;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalJoinViewModel;
import com.dobrowol.traininglog.new_training.DateTimeActivity;
import com.dobrowol.traininglog.training_load.calculating.TrainingLoad;
import com.dobrowol.traininglog.training_load.displaying.ChartActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.OnItemClickListener, View.OnClickListener, TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener, TrainingListViewAdapter.OnItemClickListener, Observer<List<Exercise>>, GoalListViewAdapter.OnItemClickListener {

    public static final String TRAINING = "training";
    public static final String NUMBER_OF_EXERCISES = "number_of_exercises";
    public static final String EXERCISE_TYPE = "exercise_type";
    private RecyclerView goalRecyclerView;

    private GoalListViewAdapter generalAdapter;

    private TextView dateTxt;
    private TextView timeTxt;
    ArrayList<Exercise> exerciseList;
    private TrainingViewModel trainingViewModel;
    private GoalViewModel goalViewModel;
    private TrainingExerciseJoinViewModel trainingExerciseJoinViewModel;
    private TrainingGoalExerciseJoinViewModel trainingGoalExerciseJoinViewModel;
    private TrainingGoalJoinViewModel trainingGoalJoinViewModel;
    private Training training;
    private int numberOfExercises;
    TrainingList detailsDialog;
    private ExerciseDescriptionViewModel exerciseDescriptionViewModel;
    private ExerciseViewModel exerciseViewModel;
    private TextView generalTextView;
    private static int oneColumn = 1;
    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        goalRecyclerView = findViewById(R.id.goal_rv);

        trainingViewModel = ViewModelProviders.of(this).get(TrainingViewModel.class);
        goalViewModel = ViewModelProviders.of(this).get(GoalViewModel.class);
        trainingExerciseJoinViewModel = ViewModelProviders.of(this).get(TrainingExerciseJoinViewModel.class);
        trainingGoalExerciseJoinViewModel = ViewModelProviders.of(this).get(TrainingGoalExerciseJoinViewModel.class);

        trainingGoalJoinViewModel = ViewModelProviders.of(this).get(TrainingGoalJoinViewModel.class);
        exerciseDescriptionViewModel = ViewModelProviders.of(this).get(ExerciseDescriptionViewModel.class);
        exerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);

        goalRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        generalAdapter = new GoalListViewAdapter(this);
        generalAdapter.setOnItemClickListener(this);
        goalRecyclerView.setAdapter(generalAdapter);


        Intent intent = getIntent();
        if(intent != null) {
                if (intent.hasExtra(MainActivity.TRAINING)) {
                    training = (Training) intent.getExtras().getSerializable(MainActivity.TRAINING);
                }
        }
        if (training == null){
            initializeTraining();
        }
        initializeObservers();
        setAppBarTitle();
        exerciseList = new ArrayList<>();
    }

    private void initializeObservers(){
        trainingExerciseJoinViewModel.trainingExercises.observe(this,this);

        trainingGoalJoinViewModel.trainingGoals.observe(this, goals -> {
            if (goals != null) {
                // Update the cached copy of the exercises in the adapter.
                ArrayList<Goal> array = new ArrayList<>(goals);
                generalAdapter.setGoals(array);
                generalAdapter.notifyDataSetChanged();
            }
        });
        trainingExerciseJoinViewModel.getExercisesByTrainingId(training.id);
        trainingGoalJoinViewModel.getAllGoalsForTraining(training.id);

    }
    private void initializeTraining(){
        training = new Training();
        training.date = new Date();
        training.id = UUID.randomUUID().toString();

        numberOfExercises = 0;

         trainingViewModel.insert(training);
    }

    private void setAppBarTitle() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM, dd", Locale.ENGLISH);
        String formatted = sdf.format(training.date);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(formatted);
        }else{
            android.app.ActionBar actionBar1 = getActionBar();
            if(actionBar1 != null){
                actionBar1.setTitle(formatted);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            default:
                break;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        if (requestCode == AddExercise.CREATE_EXERCISE ){

        }

    }
    @Override
    public void onItemClick(Exercise item) {

    }
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("generalExerciseList", generalAdapter.getGoals());

    }

    @Override
    protected void onRestoreInstanceState(Bundle inState){
        super.onRestoreInstanceState(inState);
        generalAdapter.setGoals(inState.getParcelableArrayList("generalExerciseList"));


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
            case R.id.action_chart:
                Intent intent = new Intent(this, ChartActivity.class);
                startActivity(intent);
                return true;
            case R.id.new_training:
                initializeTraining();
                exerciseList.clear();
                Intent dateTimeIntent = new Intent(this, DateTimeActivity.class);
                startActivity(dateTimeIntent);
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
        String time = String.format(Locale.ENGLISH,"%02d:%02d", hourOfDay, minute);
        timeTxt.setText(time);
        updateTrainingDate();
    }

    private void updateTrainingDate() {
        String date = dateTxt.getText().toString() +" "+ timeTxt.getText().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Converters.DATE_FORMAT + " " + Converters.TIME_FORMAT, Locale.ENGLISH);
        try {
            training.date = simpleDateFormat.parse(date);
            trainingViewModel.update(training);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = String.format(Locale.ENGLISH,"%04d-%02d-%02d", year, month, dayOfMonth);
        dateTxt.setText(date);
        updateTrainingDate();
    }

    @Override
    public void onItemClick(Training item) {
        detailsDialog.dismiss();
        trainingExerciseJoinViewModel.getExercisesByTrainingId(item.id);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Converters.DATE_FORMAT, Locale.ENGLISH);
        String stringDate = simpleDateFormat.format(item.date);
        dateTxt.setText(stringDate);
        simpleDateFormat = new SimpleDateFormat(Converters.TIME_FORMAT, Locale.ENGLISH);
        stringDate = simpleDateFormat.format(item.date);
        timeTxt.setText(stringDate);
        training = item;

    }

    @Override
    public void onItemRemove(Training training) {
        trainingViewModel.delete(training);
    }

    @Override
    public void onChanged(List<Exercise> exercises) {
        if (exercises != null) {
            ArrayList<Exercise> array = new ArrayList<>(exercises);
            numberOfExercises = array.size();
            setExercises(array);
            TrainingLoad trainingLoad = new TrainingLoad(training, trainingViewModel,
                    exerciseViewModel, trainingExerciseJoinViewModel, MainActivity.this);
            trainingLoad.calculate(exercises);
        }
    }

    private void setExercises(ArrayList<Exercise> ex) {
            generalAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(Goal item) {

    }

    @Override
    public void onItemRemove(Goal goal) {

    }

    @Override
    public void onNewGoalEnter() {
        enableActionMode("Dodaj nowy cel");
    }

    @Override
    public void onExistingGoalEdit() {
        enableActionMode("Edytuj cel");
    }

    @Override
    public void onNewExerciseEnter() {
        enableActionMode("Dodaj nowe cwiczenie");
    }

    @Override
    public void insertGoal(Goal goal) {
        goalViewModel.insert(goal);
        TrainingGoalJoin trainingGoalJoin = new TrainingGoalJoin(UUID.randomUUID().toString(),training.id, goal.id);
        trainingGoalJoinViewModel.insert(trainingGoalJoin);
    }

    @Override
    public void insertExercise(Exercise exercise) {
        exerciseViewModel.insert(exercise);
        //TrainingExerciseJoin trainingExerciseJoin = new TrainingExerciseJoin(UUID.randomUUID().toString(),exercise.id,training.id,TODO);
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        TimePickerDialog.OnTimeSetListener listener;
        TimePickerFragment(TimePickerDialog.OnTimeSetListener listener){
            this.listener = listener;
        }
        @NonNull
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
        @NonNull
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
            trainingViewModel.getAllTrainings().observe(getViewLifecycleOwner(), this);
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
    private class ActionBarCallback implements ActionMode.Callback {
        private String text;
        ActionBarCallback(String text) {
        this.text = text;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.contextual_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            mode.setTitle(text);
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.item_delete:
                    generalAdapter.discardStatus();
                    disableActionMode();
                    return true;
                case R.id.item_add:
                    generalAdapter.saveStatus();
                    disableActionMode();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {


        }
    }
    private void enableActionMode( String text) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(new ActionBarCallback(text));
        }
        if(actionMode != null) {
            actionMode.invalidate();
        }
    }
    private void disableActionMode()
    {
        actionMode.finish();
        actionMode = null;
    }
}


