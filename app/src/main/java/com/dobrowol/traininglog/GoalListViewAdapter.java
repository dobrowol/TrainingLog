package com.dobrowol.traininglog;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.adding_training.adding_goal.Goal;
import com.dobrowol.traininglog.adding_training.adding_goal.GoalExercisePair;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GoalListViewAdapter extends RecyclerView.Adapter<GoalListViewAdapter.CustomViewHolder> {


    private static Goal EMPTY_GOAL = new Goal("000000", "Dodaj cel ");

    void discardStatus() {
        viewHolder.discardStatus();
    }

    public interface OnItemClickListener {
        void onItemClick(Goal item);

        void onItemRemove(Goal goal);

        void onNewGoalEnter();

        void onExistingGoalEdit();

        void onNewExerciseEnter();

        void insertGoal(Goal goal);

        void insertExercise(Exercise exercise);
    }

    private OnItemClickListener listener;
    private ArrayList<Goal> goals;
    private ArrayList<GoalExercisePair> goalExercisePairs;
    private Map<String, List<String>> map = new HashMap<>();
    private CustomViewHolder viewHolder;

    GoalListViewAdapter(OnItemClickListener listener) {
        this.listener = listener;
        goals = new ArrayList<>();
    }
    void saveStatus(){
        viewHolder.saveStatus();
    }
    void setGoals(ArrayList<Goal> goals){
        if(goals != null && this.goals != null && goals.size() != this.goals.size()) {
            this.goals.clear();
            this.goals = goals;
        }
        if(goals !=null) {
            this.goals.add(EMPTY_GOAL);
        }
    }

    void setGoalsExercises(ArrayList<GoalExercisePair> goalsExercises){
        if(goalsExercises != null && this.goals != null && goalsExercises.size() != this.goals.size()) {
            this.goalExercisePairs.clear();
            this.goalExercisePairs = goalsExercises;
        }
    }

    ArrayList<Goal> getGoals(){
        return goals;
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.goal_layout, null);
        viewHolder = new CustomViewHolder(view, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int i) {
        Goal textAtPosition = goals.get(i);
        customViewHolder.fillView(textAtPosition);

    }

    @Override
    public int getItemCount() {
        return (null != goals ? goals.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView descriptionText;
        RecyclerView exercisesRecyclerView;
        TextView addExercise;
        OnItemClickListener listener;
        Goal goal;
        EditText descriptionEditText;
        ViewSwitcher viewSwitcher;

        CustomViewHolder(View view, OnItemClickListener listener) {
            super(view);
            this.listener = listener;
            this.descriptionText = view.findViewById(R.id.goalTextView);
            this.exercisesRecyclerView = view.findViewById(R.id.exercises_rv);
            this.addExercise = view.findViewById(R.id.addingExercise);
            view.setOnClickListener(this);
            //descriptionText.setEnabled(false);
            //addExercise.setEnabled(false);
            addExercise.setOnClickListener(this);

            viewSwitcher = view.findViewById(R.id.viewSwitcher);
            descriptionEditText = view.findViewById(R.id.generalEditText);
            descriptionText.setOnClickListener(this);

        }

        void fillView(Goal textAtPosition) {

            this.goal = textAtPosition;
            descriptionText.setText(textAtPosition.description);
            if(this.goal.id.equals(GoalListViewAdapter.EMPTY_GOAL.id)){
                addExercise.setVisibility(TextView.INVISIBLE);
                exercisesRecyclerView.setVisibility(RecyclerView.INVISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.goalTextView:
                    viewSwitcher.showNext();
                    if(descriptionText.getText().equals(GoalListViewAdapter.EMPTY_GOAL.description)) {
                        descriptionEditText.setText("");
                        listener.onNewGoalEnter();
                    }else{
                        descriptionEditText.setText(descriptionText.getText());
                        listener.onExistingGoalEdit();
                    }
                    break;

                case R.id.addingExercise:
                    listener.onNewExerciseEnter();
                    break;
                case R.id.exercises_rv:
                    listener.onItemClick(goal);
                    break;
                case R.id.btnAction:
                    listener.onItemRemove(goal);
                    break;
            }

        }

        void saveStatus() {
            switch(descriptionEditText.getId()){
                case R.id.generalEditText:
                    if (descriptionEditText.getText().toString().compareTo("")!=0) {
                        Goal goal = new Goal(UUID.randomUUID().toString(), descriptionEditText.getText().toString());
                        goal.startDate = new Date();
                        listener.insertGoal(goal);

                        descriptionText.setText(descriptionEditText.getText());
                    }
                    viewSwitcher.showPrevious();
                    break;
            }
        }

        void discardStatus() {
            switch(descriptionEditText.getId()){
                case R.id.generalEditText:
                    viewSwitcher.showPrevious();
            }
        }
    }

    private OnItemClickListener onItemClickListener;
    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
