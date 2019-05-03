package com.dobrowol.traininglog;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.dobrowol.traininglog.adding_exercise.AddExercise;
import com.dobrowol.traininglog.adding_exercise.Exercise;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.OnItemClickListener,View.OnTouchListener, View.OnClickListener {

    private RecyclerView generalRecyclerView;
    private RecyclerView specificRecyclerView;
    private RecyclerView competitiveRecyclerView;
    private MyRecyclerViewAdapter generalAdapter;
    private MyRecyclerViewAdapter specificAdapter;
    private MyRecyclerViewAdapter competitiveAdapter;
    private RecyclerView currentRecyclerView;
    List<Exercise> exerciseList;

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

        generalRecyclerView.setBackgroundResource(R.drawable.edit_text_general_background);
        specificRecyclerView.setBackgroundResource(R.drawable.edit_text_no_focus_background);
        competitiveRecyclerView.setBackgroundResource(R.drawable.edit_text_no_focus_background);

        generalRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        generalAdapter = new MyRecyclerViewAdapter(getApplicationContext());
        generalAdapter.setOnItemClickListener(this);
        generalRecyclerView.setAdapter(generalAdapter);

        specificRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        specificAdapter = new MyRecyclerViewAdapter(getApplicationContext());
        specificAdapter.setOnItemClickListener(this);
        specificRecyclerView.setAdapter(specificAdapter);

        competitiveRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        competitiveAdapter = new MyRecyclerViewAdapter(getApplicationContext());
        competitiveAdapter.setOnItemClickListener(this);
        competitiveRecyclerView.setAdapter(competitiveAdapter);

        currentRecyclerView=generalRecyclerView;
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        exerciseList = new ArrayList<>();
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.general_rv:
                currentRecyclerView = generalRecyclerView;
                generalRecyclerView.setBackgroundResource(R.drawable.edit_text_general_background);
                specificRecyclerView.setBackgroundResource(R.drawable.edit_text_no_focus_background);
                competitiveRecyclerView.setBackgroundResource(R.drawable.edit_text_no_focus_background);
                break;
            case R.id.specific_rv:
                currentRecyclerView = specificRecyclerView;
                specificRecyclerView.setBackgroundResource(R.drawable.edit_text_general_background);
                generalRecyclerView.setBackgroundResource(R.drawable.edit_text_no_focus_background);
                competitiveRecyclerView.setBackgroundResource(R.drawable.edit_text_no_focus_background);
                break;
            case R.id.competitive_rv:
                currentRecyclerView = competitiveRecyclerView;
                competitiveRecyclerView.setBackgroundResource(R.drawable.edit_text_general_background);
                specificRecyclerView.setBackgroundResource(R.drawable.edit_text_no_focus_background);
                generalRecyclerView.setBackgroundResource(R.drawable.edit_text_no_focus_background);
                break;
        }
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
            Exercise exercise = (Exercise) data.getExtras().getSerializable(AddExercise.REQUESTED_CODE);
            exerciseList.add(exercise);
            generalAdapter.setExerciseList(exerciseList);
        }

    }

    @Override
    public void onItemClick(Exercise item) {

    }
}
