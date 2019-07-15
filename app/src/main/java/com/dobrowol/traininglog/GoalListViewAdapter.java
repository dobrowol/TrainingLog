package com.dobrowol.traininglog;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dobrowol.traininglog.adding_training.adding_goal.Goal;
import com.dobrowol.traininglog.adding_training.adding_goal.GoalExercisePair;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoalListViewAdapter extends RecyclerView.Adapter<GoalListViewAdapter.CustomViewHolder> {


    public static Goal EMPTY_GOAL = new Goal("000000", "Dodaj cel ");

    public interface OnItemClickListener {
        void onItemClick(Goal item);

        void onItemRemove(Goal goal);
    }

    private OnItemClickListener listener;
    private ArrayList<Goal> goals;
    private ArrayList<GoalExercisePair> goalExercisePairs;
    private Map<String, List<String>> map = new HashMap<>();

    GoalListViewAdapter(OnItemClickListener listener) {
        this.listener = listener;
        goals = new ArrayList<>();
    }

    void setGoals(ArrayList<Goal> goals){
        if(goals != null && this.goals != null && goals.size() != this.goals.size()) {
            this.goals.clear();
            this.goals = goals;
        }

        this.goals.add(EMPTY_GOAL);
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
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.goal_layout, null);

        return new CustomViewHolder(view, listener);
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


        public CustomViewHolder(View view, OnItemClickListener listener) {
            super(view);
            this.listener = listener;
            this.descriptionText = view.findViewById(R.id.generalTextView);
            this.exercisesRecyclerView = view.findViewById(R.id.exercises_rv);
            this.addExercise = view.findViewById(R.id.addingExercise);
            view.setOnClickListener(this);
            //descriptionText.setEnabled(false);
            //addExercise.setEnabled(false);
            descriptionText.setOnClickListener(this);
            addExercise.setOnClickListener(this);
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
                case R.id.generalTextView:
                    descriptionText.setEnabled(true);
                    descriptionText.setText("");
                    descriptionText.setCursorVisible(true);
                    descriptionText.setFocusableInTouchMode(true);
                    descriptionText.setInputType(InputType.TYPE_CLASS_TEXT);
                    descriptionText.requestFocus();
                    break;
                case R.id.exercises_rv:
                    listener.onItemClick(goal);
                    break;
                case R.id.btnAction:
                    listener.onItemRemove(goal);
                    break;
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
