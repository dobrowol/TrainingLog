package com.dobrowol.traininglog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.lifecycle.Observer;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.TrainingViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link AllTrainingRecyclerViewAdapter.OnListFragmentInteractionListener}
 * interface.
 */
public class TrainingsApp extends AppCompatActivity implements Observer<List<Training>>, AllTrainingRecyclerViewAdapter.OnListFragmentInteractionListener, View.OnClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private TrainingViewModel trainingViewModel;
    RecyclerView recyclerView;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TrainingsApp() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static void startInstance(Context context, int columnCount) {
            Intent intent = new Intent(context, TrainingsApp.class);
            Bundle args = new Bundle();
            args.putInt(ARG_COLUMN_COUNT, columnCount);
            intent.putExtras(args);
            context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(ARG_COLUMN_COUNT)) {
                mColumnCount = intent.getExtras().getInt(ARG_COLUMN_COUNT);
            }
            mColumnCount = 1;
        }

        setContentView(R.layout.fragment_training_list);

        recyclerView = findViewById(R.id.list);
        // Set the adapter

            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), mColumnCount));
            }

        trainingViewModel = ViewModelProviders.of(this).get(TrainingViewModel.class);
        trainingViewModel.getAllTrainings().observe(this, this);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_add_training);
        floatingActionButton.setOnClickListener(this);
    }

    @Override
    public void onChanged(List<Training> trainings) {

        recyclerView.setAdapter(new AllTrainingRecyclerViewAdapter(trainings, this));
    }

    @Override
    public void onListFragmentInteraction(Training item) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add_training:
                Intent intent = new Intent(this,MainActivity.class);

                startActivity(intent);
                break;

        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}