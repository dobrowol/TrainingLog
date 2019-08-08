package com.dobrowol.traininglog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.lifecycle.Observer;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.TrainingViewModel;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalExerciseJoinViewModel;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalJoinViewModel;
import com.dobrowol.traininglog.adding_training.deleting_exercise.RecyclerItemTouchHelper;
import com.dobrowol.traininglog.deleting_training.RecyclerTrainingTouchHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link AllTrainingRecyclerViewAdapter.OnListFragmentInteractionListener}
 * interface.
 */
public class TrainingsApp extends AppCompatActivity implements Observer<List<Training>>, AllTrainingRecyclerViewAdapter.OnListFragmentInteractionListener, View.OnClickListener, RecyclerTrainingTouchHelper.RecyclerItemTouchHelperListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private TrainingViewModel trainingViewModel;
    private TrainingGoalJoinViewModel trainingGoalViewModel;
    private TrainingGoalExerciseJoinViewModel trainingGoalExerciseJoinViewModel;
    RecyclerView recyclerView;
    AllTrainingRecyclerViewAdapter adapter;
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
                mColumnCount = Objects.requireNonNull(intent.getExtras()).getInt(ARG_COLUMN_COUNT);
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
        adapter = new AllTrainingRecyclerViewAdapter( this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerTrainingTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        trainingViewModel = ViewModelProviders.of(this).get(TrainingViewModel.class);
            trainingViewModel.getAllTrainings().observe(this, this);

        trainingGoalViewModel = ViewModelProviders.of(this).get(TrainingGoalJoinViewModel.class);
        //trainingGoalViewModel.trainingGoalsForTrainingId.observe(this, trainingGoalJoins -> adapter.setTrainingGoalJoins(trainingGoalJoins));

        trainingGoalExerciseJoinViewModel = ViewModelProviders.of(this).get(TrainingGoalExerciseJoinViewModel.class);
        trainingGoalExerciseJoinViewModel.getTrainingGoalExerciseDataAggregated().observe(this, trainingGoalExerciseData -> adapter.setTrainingGoalLoads(trainingGoalExerciseData));
        FloatingActionButton floatingActionButton = findViewById(R.id.fab_add_training);
        floatingActionButton.setOnClickListener(this);
        trainingGoalExerciseJoinViewModel.getMaximumExerciseLoad().observe(this, maximumLoad -> {
            if (maximumLoad != null) {
                adapter.setMaximumLoad(maximumLoad);
            } }
        );
        trainingGoalExerciseJoinViewModel.getMaximumNumberOfExercisesForTraining().observe(this, numberOfExercises ->{
            if(numberOfExercises != null){
                adapter.setNumberOfExercises(numberOfExercises);
            }
        });
    }

    @Override
    public void onChanged(List<Training> trainings) {
        adapter.setTrainings(trainings);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onListFragmentInteraction(Training item) {
        //trainingGoalViewModel.getAllTrainingGoalJoinsForTraining(item.id);

        Intent intent = new Intent(this,MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.TRAINING, item);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void onDeleteTraining(Training training) {
        trainingViewModel.delete(training);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fab_add_training){
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
        }
    }
    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof AllTrainingRecyclerViewAdapter.ViewHolder) {
            adapter.removeTraining(viewHolder.getAdapterPosition());
            adapter.notifyDataSetChanged();
        }
    }
}
