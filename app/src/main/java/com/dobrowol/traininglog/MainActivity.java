package com.dobrowol.traininglog;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.appcompat.view.ActionMode;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dobrowol.traininglog.adding_training.TrainingExerciseJoinViewModel;
import com.dobrowol.traininglog.adding_training.adding_exercise.AddExercise;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements  View.OnClickListener, Observer<List<Exercise>>, GoalListViewAdapter.OnItemClickListener {

    public static final String TRAINING = "training";
    private RecyclerView goalRecyclerView;

    private GoalListViewAdapter generalAdapter;

    ArrayList<Exercise> exerciseList;
    private TrainingViewModel trainingViewModel;
    private GoalViewModel goalViewModel;
    private TrainingGoalExerciseJoinViewModel trainingGoalExerciseJoinViewModel;
    private TrainingGoalJoinViewModel trainingGoalJoinViewModel;
    private Training training;
    private ExerciseDescriptionViewModel exerciseDescriptionViewModel;
    private ExerciseViewModel exerciseViewModel;
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
        if(training != null) {
            generalAdapter.setTraining(training);
            initializeObservers();
            setAppBarTitle();
        }
        exerciseList = new ArrayList<>();
        editedGoal = null;
    }

    private void initializeObservers(){

        trainingGoalExerciseJoinViewModel.goalExercisesForTraining.observe(this, goalExercises -> {
            if(goalExercises != null){
                generalAdapter.setGoalsExercises(goalExercises);
                generalAdapter.notifyDataSetChanged();
            }
        });

        exerciseDescriptionViewModel.getAllExercisesDescriptions().observe(this, exerciseDescriptions -> generalAdapter.setExerciseDescriptions(exerciseDescriptions));

        trainingGoalExerciseJoinViewModel.getGoalExercisesForTraining(training.id);

        goalViewModel.goalByDescription.observe(this, goal -> {
            if(goal != null){
                generalAdapter.addGoal(goal);
                generalAdapter.notifyDataSetChanged();
            }
        });
    }
    private void initializeTraining(){
        training = new Training();
        training.date = new Date();
        training.id = UUID.randomUUID().toString();

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
    public void onClick(View view) {
        if(view.getId() == R.id.fab_add_goal)
                showAlert(getString(R.string.add_goal));
    }

    @Override
    public void deleteGoal(Goal oldGoal) {
        trainingGoalExerciseJoinViewModel.deleteGoal(training.id, oldGoal.goalId);
    }

    @Override
    public void removeExercise(Exercise adapterPosition) {
        exerciseViewModel.delete(adapterPosition);
    }

    @Override
    public void onUpdateGoal(Goal oldGoal) {
        editedGoal = oldGoal;
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
        if(goalDescription != null && !goalDescription.equals("")) {
            Goal goal = new Goal(UUID.randomUUID().toString(), goalDescription);
            insertGoal(goal);

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(TRAINING, training);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onChanged(List<Exercise> exercises) {

    }

    @Override
    public void onItemClick(Training training, Goal item) {

        AddExercise.startNewInstance(getApplicationContext(), training, item);
    }


    @Override
    public void insertGoal(Goal goal) {
        goalViewModel.insert(goal).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();

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


