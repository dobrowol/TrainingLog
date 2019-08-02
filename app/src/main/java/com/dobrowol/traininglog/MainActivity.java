package com.dobrowol.traininglog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.appcompat.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ViewSwitcher;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dobrowol.traininglog.adding_training.TrainingExerciseJoinViewModel;
import com.dobrowol.traininglog.adding_training.adding_exercise.Converters;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseDescriptionViewModel;
import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.TrainingViewModel;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseViewModel;
import com.dobrowol.traininglog.adding_training.adding_goal.Goal;
import com.dobrowol.traininglog.adding_training.adding_goal.GoalViewModel;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalExerciseJoinViewModel;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalJoin;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalJoinViewModel;
import com.dobrowol.traininglog.new_training.DateTimeActivity;
import com.dobrowol.traininglog.training_load.displaying.ChartActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


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
    private Goal editedGoal;
    private FloatingActionButton fab_add_goal;

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

        fab_add_goal = findViewById(R.id.fab_add_goal);
        fab_add_goal.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false) {
            @Override
            public boolean requestChildRectangleOnScreen(@NonNull RecyclerView parent, @NonNull View child, @NonNull Rect rect, boolean immediate, boolean focusedChildVisible) {

                if (((ViewGroup) child).getFocusedChild() instanceof ViewSwitcher) {
                    return false;
                }

                return super.requestChildRectangleOnScreen(parent, child, rect, immediate, focusedChildVisible);
            }
        };
        goalRecyclerView.setLayoutManager(linearLayoutManager);
        goalRecyclerView.setFocusable(false);
        goalRecyclerView.setNestedScrollingEnabled(true);
        generalAdapter = new GoalListViewAdapter(this, getApplicationContext());
        generalAdapter.setOnItemClickListener(this);
        goalRecyclerView.setAdapter(generalAdapter);

        Intent intent = getIntent();
        if(intent != null) {
                if (intent.hasExtra(MainActivity.TRAINING)) {
                    training = (Training) Objects.requireNonNull(intent.getExtras()).getSerializable(MainActivity.TRAINING);
                }
        }
        if (training == null){
            initializeTraining();
        }
        generalAdapter.setTraining(training);
        initializeObservers();
        setAppBarTitle();
        exerciseList = new ArrayList<>();
        editedGoal = null;
    }

    private void initializeObservers(){
        trainingExerciseJoinViewModel.trainingExercises.observe(this,this);

        trainingGoalJoinViewModel.goalsForTraining.observe(this, goals -> {
            if (goals != null) {
                // Update the cached copy of the exercises in the adapter.
                ArrayList<Goal> array = new ArrayList<>(goals);
                generalAdapter.setGoals(array);
                generalAdapter.notifyDataSetChanged();
            }
        });

        trainingGoalExerciseJoinViewModel.goalExercisesForTraining.observe(this, goalExercises -> {
            if(goalExercises != null){
                generalAdapter.setGoalsExercises(goalExercises);
                generalAdapter.notifyDataSetChanged();
            }
        });

        exerciseDescriptionViewModel.getAllExercisesDescriptions().observe(this, exerciseDescriptions -> generalAdapter.setExerciseDescriptions(exerciseDescriptions));

        trainingGoalJoinViewModel.getAllGoalsForTraining(training.id);
        trainingGoalExerciseJoinViewModel.getGoalExercisesForTraining(training.id);

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
            actionBar.setDisplayHomeAsUpEnabled(true);
        }else{
            android.app.ActionBar actionBar1 = getActionBar();
            if(actionBar1 != null){
                actionBar1.setTitle(formatted);
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
    public void onClick(View view) {
        if(view.getId() == R.id.fab_add_goal){
                showAlert("Dodaj cel");
        }
    }

    @Override
    public void deleteGoal(Goal oldGoal) {
        goalViewModel.delete(oldGoal);
    }

    @Override
    public void removeExercise(Exercise adapterPosition) {
        exerciseViewModel.delete(adapterPosition);
    }

    @Override
    public void insertExercise(Exercise deletedItem) {
        exerciseViewModel.insert(deletedItem);
    }

    public void showAlert(String text){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("GOAL");
        alertDialog.setMessage(text);

        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        alertDialog.setView(input);

        alertDialog.setPositiveButton("YES",
                (dialog, which) -> insertGoal(input.getText().toString()));

        alertDialog.setNegativeButton("NO",
                (dialog, which) -> dialog.cancel());

        alertDialog.show();
    }

    private void insertGoal(String goalDescription) {
        Goal goal = new Goal(UUID.randomUUID().toString(),goalDescription);
        insertGoal(goal);
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
        startActivity(new Intent(getApplicationContext(), TrainingsApp.class));
        /*if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }*/
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
    public void onExistingGoalEdit(String description) {

        goalViewModel.getGoal(description).observe(this, goal -> editedGoal = goal);
    }

    @Override
    public void insertGoal(Goal goal) {
        goalViewModel.insert(goal).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                s -> {
                TrainingGoalJoin trainingGoalJoin = new TrainingGoalJoin(UUID.randomUUID().toString(), training.id, goal.goalId);
                trainingGoalJoinViewModel.insert(trainingGoalJoin).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
            }
        );
    }

    @Override
    public void updateGoal(Goal newGoal) {
        if(newGoal!= null) {
            if (newGoal.description != null) {
                editedGoal.description = newGoal.description;
            }
            if (newGoal.goalStartDate != null) {
                editedGoal.goalStartDate = newGoal.goalStartDate;
            }
            if (newGoal.endDate != null) {
                editedGoal.endDate = newGoal.endDate;
            }
            if (newGoal.priority != -1) {
                editedGoal.priority = newGoal.priority;
            }
            goalViewModel.update(editedGoal);
        }
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

            return new DatePickerDialog(Objects.requireNonNull(getActivity()), this, year, month, day);
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


