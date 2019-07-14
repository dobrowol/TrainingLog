package com.dobrowol.traininglog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dobrowol.traininglog.adding_training.adding_goal.Goal;
import com.dobrowol.traininglog.adding_training.adding_goal.GoalExercisePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoalListViewAdapter extends RecyclerView.Adapter<GoalListViewAdapter.CustomViewHolder> {


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
        Button btnRemove;
        OnItemClickListener listener;
        Goal goal;

        public CustomViewHolder(View view, OnItemClickListener listener) {
            super(view);
            this.listener = listener;
            this.descriptionText = view.findViewById(R.id.description);
            this.btnRemove = view.findViewById(R.id.btnAction);
            btnRemove.setVisibility(View.VISIBLE);
            btnRemove.setText("Remove");
            view.setOnClickListener(this);
            btnRemove.setOnClickListener(this);
        }

        void fillView(Goal textAtPosition) {

            this.goal = textAtPosition;
            descriptionText.setText(textAtPosition.description);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.description:
                case R.id.list_row:
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
