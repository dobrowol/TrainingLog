package com.dobrowol.traininglog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.appcompat.view.ActionMode;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dobrowol.traininglog.adding_training.TrainingExerciseJoinViewModel;
import com.dobrowol.traininglog.adding_training.adding_exercise.AddExercise;
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
import com.dobrowol.traininglog.training_load.calculating.TrainingGoalLoad;
import com.dobrowol.traininglog.training_load.displaying.ChartActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements  View.OnClickListener, TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener, Observer<List<Exercise>>, GoalListViewAdapter.OnItemClickListener {

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
    private ExerciseDescriptionViewModel exerciseDescriptionViewModel;
    private ExerciseViewModel exerciseViewModel;
    private TextView generalTextView;
    private static int oneColumn = 1;
    private ActionMode actionMode;
    private Goal editedGoal;
    private FloatingActionButton fab_add_goal;

    public static void startNewInstance(Context context, Training training)
    {
        Intent intent = new Intent(context, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.TRAINING, training);
        intent.putExtras(bundle);

        context.startActivity(intent);
    }

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
            if(savedInstanceState != null)
            {
                training = (Training) savedInstanceState.getSerializable(TRAINING);
            }
            if(training == null) {
                initializeTraining();
            }
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
        setSupportActionBar(findViewById(R.id.toolbar));
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM, dd - HH:mm", Locale.ENGLISH);
        TextView textView = findViewById(R.id.toolbarTitle);
        String formatted = sdf.format(training.date);
        textView.setText(formatted);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }else{
            android.app.ActionBar actionBar1 = getActionBar();
            if(actionBar1 != null){
                actionBar1.setTitle("");
                actionBar1.setDisplayHomeAsUpEnabled(true);

            }
        }
        textView.setOnClickListener(v -> DateTimeActivity.startInstance(MainActivity.this, training));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fab_add_goal)
                showAlert(getString(R.string.add_goal));
    }

    @Override
    public void deleteGoal(Goal oldGoal) {
        goalViewModel.delete(oldGoal);
    }

    @Override
    public void removeExercise(Exercise adapterPosition) {
        exerciseViewModel.delete(adapterPosition);
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
        if(goalDescription != null && goalDescription != "") {
            Goal goal = new Goal(UUID.randomUUID().toString(), goalDescription);
            insertGoal(goal);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TRAINING, training.id);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), TrainingsApp.class));
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
    public void onItemClick(Training training, Goal item) {

        AddExercise.startNewInstance(getApplicationContext(), training, item);
    }


    @Override
    public void insertGoal(Goal goal) {
        goalViewModel.insert(goal).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();

        goalViewModel.goalByDescription.observe(this, goalFound -> {
            if(goalFound != null){
                TrainingGoalJoin trainingGoalJoin = new TrainingGoalJoin(UUID.randomUUID().toString(), training.id, goalFound.goalId);
                trainingGoalJoinViewModel.insert(trainingGoalJoin).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
            }
        });
        goalViewModel.getGoal(goal.description);
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


