package com.dobrowol.traininglog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalJoin;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalJoinViewModel;
import com.dobrowol.traininglog.deleting_training.RecyclerTrainingTouchHelper;
import com.dobrowol.traininglog.training_load.calculating.TrainingGoalLoad;
import com.dobrowol.traininglog.training_load.calculating.TrainingGoalLoadData;
import com.dobrowol.traininglog.training_load.displaying.ChartActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link AllTrainingRecyclerViewAdapter.OnListFragmentInteractionListener}
 * interface.
 */
public class TrainingsFragment extends Fragment implements Observer<List<Training>>, AllTrainingRecyclerViewAdapter.OnListFragmentInteractionListener, View.OnClickListener, RecyclerTrainingTouchHelper.RecyclerItemTouchHelperListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";

    private TrainingViewModel trainingViewModel;
    private TrainingGoalJoinViewModel trainingGoalJoinViewModel;
    private TrainingGoalExerciseJoinViewModel trainingGoalExerciseJoinViewModel;
    RecyclerView recyclerView;
    AllTrainingRecyclerViewAdapter adapter;
    List<Training> trainings;
    ArrayList<ArrayList<Integer>> listOfLoads;
    private View rootView;

    private HashMap<String, List<TrainingGoalLoadData>> trainingGoalLoadData;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        listOfLoads = new ArrayList<>();
        trainingGoalLoadData = new HashMap<>();


        rootView =
                inflater.inflate(R.layout.fragment_training_list, container, false);

        recyclerView = rootView.findViewById(R.id.list);
        // Set the adapter

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new AllTrainingRecyclerViewAdapter( this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerTrainingTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        trainingGoalJoinViewModel = ViewModelProviders.of(this).get(TrainingGoalJoinViewModel.class);

        trainingViewModel = ViewModelProviders.of(this).get(TrainingViewModel.class);
            trainingViewModel.getAllTrainings().observe(this, this);

        //trainingGoalViewModel.trainingGoalsForTrainingId.observe(this, trainingGoalJoins -> adapter.setTrainingGoalJoins(trainingGoalJoins));

        trainingGoalExerciseJoinViewModel = ViewModelProviders.of(this).get(TrainingGoalExerciseJoinViewModel.class);
        //trainingGoalExerciseJoinViewModel.getTrainingGoalExerciseDataAggregated().observe(this, trainingGoalExerciseData -> adapter.setTrainingGoalLoads(trainingGoalExerciseData));
        trainingGoalExerciseJoinViewModel.getTrainingLoadData().observe(this, trainingGoalLoadData ->{ calculateLoads(trainingGoalLoadData);
        adapter.notifyDataSetChanged();}
        );

        trainingGoalExerciseJoinViewModel.getMaximumNumberOfExercisesForTraining().observe(this, numberOfExercises ->{
            if(numberOfExercises != null){
                adapter.setNumberOfExercises(numberOfExercises);
            }
        });

        FloatingActionButton floatingActionButton = rootView.findViewById(R.id.fab_add_training);
        floatingActionButton.setOnClickListener(this);

        return rootView;
    }

    void calculateLoads(List<TrainingGoalLoadData> trainingGoalLoadData){
        if(trainingGoalLoadData != null) {
            if(this.trainingGoalLoadData != null){
                this.trainingGoalLoadData.clear();
            }
            for (TrainingGoalLoadData trainingGoalLoadData1 : trainingGoalLoadData) {
                List<TrainingGoalLoadData> list = this.trainingGoalLoadData.get(trainingGoalLoadData1.trainingJoinId);

                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(trainingGoalLoadData1);
                this.trainingGoalLoadData.put(trainingGoalLoadData1.trainingJoinId, list);
            }
        }
        if(this.trainingGoalLoadData != null && !this.trainingGoalLoadData.isEmpty() && trainings != null) {

            listOfLoads.clear();
            TrainingGoalLoad trainingGoalLoad = new TrainingGoalLoad();
            int max = 0;
            for (int i = 0; i < trainings.size(); i++) {
                Training training = trainings.get( i);
                HashMap<String, Integer> loads = trainingGoalLoad.calculate(this.trainingGoalLoadData.get(training.id));

                int localMax = 0;
                if(loads != null) {
                    for (Integer load : loads.values()) {
                        localMax += load;
                    }
                    if (localMax > max)
                        max = localMax;
                    listOfLoads.add(new ArrayList<>(loads.values()));

                    for (String key : loads.keySet()) {
                        TrainingGoalJoin trainingGoalJoin = new TrainingGoalJoin(UUID.randomUUID().toString(), training.id, key, loads.get(key));
                        trainingGoalJoinViewModel.insert(trainingGoalJoin);
                    }
                }
            }
            adapter.setMaximumLoad(max);
            adapter.setListOfLoads(listOfLoads);
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onChanged(List<Training> trainings) {
        this.trainings = trainings;
        adapter.setTrainings(trainings);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onListFragmentInteraction(Training item) {
      //TODO start Characteristics Fragment
    }

    @Override
    public void onDeleteTraining(Training training) {
        trainingViewModel.delete(training);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fab_add_training){

        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof AllTrainingRecyclerViewAdapter.ViewHolder) {
            adapter.removeTraining(viewHolder.getAdapterPosition());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_chart:
                //TODO chartActivity start
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
