package com.dobrowol.traininglog;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dobrowol.traininglog.adding_exercise.AddExercise;
import com.dobrowol.traininglog.adding_exercise.Exercise;
import com.dobrowol.traininglog.adding_exercise.ExerciseDescription;
import com.dobrowol.traininglog.adding_exercise.ExerciseDescriptionViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.OnItemClickListener,View.OnTouchListener, View.OnClickListener {

    private RecyclerView generalRecyclerView;
    private RecyclerView specificRecyclerView;
    private RecyclerView competitiveRecyclerView;
    private MyRecyclerViewAdapter generalAdapter;
    private MyRecyclerViewAdapter specificAdapter;
    private MyRecyclerViewAdapter competitiveAdapter;
    private MyRecyclerViewAdapter currentAdapter;
    ArrayList<Exercise> exerciseList;
    ArrayList<ExerciseDescription> exerciseDescriptionList;
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

        exerciseDescriptionViewModel = ViewModelProviders.of(this).get(ExerciseDescriptionViewModel.class);



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

        exerciseDescriptionViewModel.getAllExercisesDescriptions().observe(this, new Observer<List<ExerciseDescription>>() {
            @Override
            public void onChanged(@Nullable final List<ExerciseDescription> words) {
                // Update the cached copy of the words in the adapter.
                ArrayList<ExerciseDescription> array = new ArrayList<>(words);
                currentAdapter.setExerciseDescriptions(array);
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        exerciseList = new ArrayList<>();
        exerciseDescriptionList = new ArrayList<>();
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
                startActivityForResult(intent,AddExercise.CREATE_EXERCISE);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){
            try {
                Exercise exercise = (Exercise) data.getExtras().getSerializable(AddExercise.REQUESTED_CODE);

                exerciseList.add(exercise);
                currentAdapter.setExerciseList(exerciseList);

                currentAdapter.notifyDataSetChanged();
            }catch (NullPointerException e){System.err.print(e.getStackTrace().toString());}
        }

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
