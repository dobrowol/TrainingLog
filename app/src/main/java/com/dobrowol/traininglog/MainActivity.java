package com.dobrowol.traininglog;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
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
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.OnItemClickListener,View.OnTouchListener, View.OnClickListener {

    public static final String TRAINING_ID = "training_id";
    public static final String NUMBER_OF_EXERCISES = "number_of_exercises";
    private RecyclerView generalRecyclerView;
    private RecyclerView specificRecyclerView;
    private RecyclerView competitiveRecyclerView;
    private MyRecyclerViewAdapter generalAdapter;
    private MyRecyclerViewAdapter specificAdapter;
    private MyRecyclerViewAdapter competitiveAdapter;
    private MyRecyclerViewAdapter currentAdapter;
    ArrayList<Exercise> exerciseList;
    private ExerciseViewModel exerciseViewModel;
    private TrainingViewModel trainingViewModel;
    private TrainingExerciseJoinViewModel trainingExerciseJoinViewModel;
    private Training training;
    private int numberOfExercises;
    private ExerciseDescriptionViewModel exerciseDescrptionViewModel;

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

        exerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        trainingViewModel = ViewModelProviders.of(this).get(TrainingViewModel.class);
        trainingExerciseJoinViewModel = ViewModelProviders.of(this).get(TrainingExerciseJoinViewModel.class);
        exerciseDescrptionViewModel = ViewModelProviders.of(this).get(ExerciseDescriptionViewModel.class);

        generalRecyclerView.setBackgroundResource(R.drawable.edit_text_general_background);
        specificRecyclerView.setBackgroundResource(R.drawable.edit_text_no_focus_background);
        competitiveRecyclerView.setBackgroundResource(R.drawable.edit_text_no_focus_background);

        generalRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        generalAdapter = new MyRecyclerViewAdapter();
        generalAdapter.setOnItemClickListener(this);
        generalRecyclerView.setAdapter(generalAdapter);

        specificRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        specificAdapter = new MyRecyclerViewAdapter();
        specificAdapter.setOnItemClickListener(this);
        specificRecyclerView.setAdapter(specificAdapter);

        competitiveRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        competitiveAdapter = new MyRecyclerViewAdapter();
        competitiveAdapter.setOnItemClickListener(this);
        competitiveRecyclerView.setAdapter(competitiveAdapter);

        currentAdapter=generalAdapter;

        initializeTraining();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        FloatingActionButton fabSave = findViewById(R.id.fabSave);
        fabSave.setOnClickListener(this);

        exerciseList = new ArrayList<>();
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
                    currentAdapter.setExerciseList(array);
                    currentAdapter.notifyDataSetChanged();

                }
            }
        });
        exerciseDescrptionViewModel.getAllExercisesDescriptions().observe(this, new Observer<List<ExerciseDescription>>() {
            @Override
            public void onChanged(@Nullable final List<ExerciseDescription> exercises) {
                if (exercises != null) {
                    // Update the cached copy of the exercises in the adapter.
                    ArrayList<ExerciseDescription> array = new ArrayList<>(exercises);
                    currentAdapter.setExerciseDescriptionList(array);
                }
            }
        });
        trainingViewModel.insert(training);
        numberOfExercises = 0;
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
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                Intent intent = new Intent(this,AddExercise.class);
                Bundle bundle = new Bundle();
                bundle.putString(TRAINING_ID,training.id);
                bundle.putInt(NUMBER_OF_EXERCISES, numberOfExercises);
                intent.putExtras(bundle);
                startActivityForResult(intent,AddExercise.CREATE_EXERCISE);
                break;
            case R.id.fabSave:
                trainingViewModel.update(training);
                initializeTraining();
                exerciseList.clear();
                Toast.makeText(getApplicationContext(),"training saved",Toast.LENGTH_SHORT).show();
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
}
