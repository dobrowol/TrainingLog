package com.dobrowol.traininglog;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.RecyclerView;

import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.adding_training.adding_goal.Goal;
import com.dobrowol.traininglog.adding_training.adding_goal.GoalExercisePair;

import org.w3c.dom.Text;

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

    public interface OnItemClickListener extends View.OnClickListener {
        void onItemClick(Goal item);

        void onItemRemove(Goal goal);

        void onNewGoalEnter();

        void onExistingGoalEdit(String description);

        void onNewExerciseEnter();

        void insertGoal(Goal goal);

        void insertExercise(Exercise exercise);

        void updateExercise(Exercise newExercise);

        void updateGoal( Goal newGoal);

        @Override
        void onClick(View v);
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
        if(this.goals !=null) {
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
    private interface TrainingDetailEnterState{

        void saveStatus();
        void discardStatus();
    }
   /* private class TrainingDetailsSM{
         TrainingDetailEnterState trainingDetailEnterState;
         void saveStatus(){
             trainingDetailEnterState.saveStatus();
         }
         void discardStatus(){
             trainingDetailEnterState.discardStatus();
         }
         void setTrainingDetailEnterState(TrainingDetailEnterState trainingDetailEnterState){
             this.trainingDetailEnterState = trainingDetailEnterState;
         }
    }*/
    private class NewGoalEnterState implements TrainingDetailEnterState {
       OnItemClickListener listener;
       View view;
       ViewSwitcher viewSwitcher;

       NewGoalEnterState(View view, OnItemClickListener listener) {
           this.listener = listener;
           this.view = view;
           viewSwitcher = view.findViewById(R.id.viewSwitcher);
       }

       @Override
       public void saveStatus() {
           TextView descriptionText = view.findViewById(R.id.goalTextView);
           EditText descriptionEditText = view.findViewById(R.id.generalEditText);

           if (descriptionEditText.getText().toString().compareTo("") != 0) {
               Goal goal = new Goal(UUID.randomUUID().toString(), descriptionEditText.getText().toString());
               goal.startDate = new Date();
               listener.insertGoal(goal);

               descriptionText.setText(descriptionEditText.getText());
           }
           viewSwitcher.showPrevious();
       }

       @Override
       public void discardStatus() {
           viewSwitcher.showPrevious();
       }
   }
       private class ExistingGoalUpdateState implements TrainingDetailEnterState{
           OnItemClickListener listener;
           View view;
           ViewSwitcher viewSwitcher;
           Goal oldGoal;
           TextView descriptionText;
           ExistingGoalUpdateState(View view, OnItemClickListener listener){
               this.listener = listener;
               this.view = view;
               viewSwitcher = view.findViewById(R.id.viewSwitcher);
               descriptionText = view.findViewById(R.id.goalTextView);
               oldGoal = new Goal(null, descriptionText.getText().toString());
           }
           @Override
           public void saveStatus() {

               EditText descriptionEditText = view.findViewById(R.id.generalEditText);

               if (descriptionEditText.getText().toString().compareTo("")!=0) {
                  Goal newGoal = new Goal(null, descriptionEditText.getText().toString());
                   listener.updateGoal(newGoal);

                   descriptionText.setText(descriptionEditText.getText());
               }
               viewSwitcher.showPrevious();
           }

           @Override
           public void discardStatus() {
               viewSwitcher.showPrevious();
           }
    }
    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView descriptionText;
        RecyclerView exercisesRecyclerView;
        TextView addExercise;
        OnItemClickListener listener;
        Goal goal;
        EditText descriptionEditText;
        ViewSwitcher viewSwitcher;
        TrainingDetailEnterState trainingDetailEnterState;
        private ActionMode actionMode;

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
            trainingDetailEnterState = null;
        }

        void fillView(Goal textAtPosition) {

            this.goal = textAtPosition;
            descriptionText.setText(textAtPosition.description);
            if(this.goal.id.equals(GoalListViewAdapter.EMPTY_GOAL.id)){
                descriptionText.setTextColor(descriptionText.getResources().getColor(R.color.adding_text_colour));
                descriptionText.setTypeface(descriptionText.getTypeface(),Typeface.BOLD);
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
                        enableActionMode(v,"Dodaj cel");
                        setState(new NewGoalEnterState(v, listener));
                    }else{
                        descriptionEditText.setText(descriptionText.getText());
                        enableActionMode(v,"Edytuj cel");
                        listener.onExistingGoalEdit(descriptionText.getText().toString());
                        setState(new ExistingGoalUpdateState(v, listener));
                    }
                    if(actionMode != null) {
                        actionMode.invalidate();
                    }
                    break;

                case R.id.addingExercise:
                    enableActionMode(v,"Dodaj ćwiczenie");

                    break;
                case R.id.exercises_rv:
                    listener.onItemClick(goal);
                    break;
                case R.id.btnAction:
                    listener.onItemRemove(goal);
                    break;
            }

        }

        private void setState(TrainingDetailEnterState newGoalEnterState) {
            if(trainingDetailEnterState != null){
                trainingDetailEnterState.discardStatus();
            }
            trainingDetailEnterState = newGoalEnterState;
        }

        private void enableActionMode(View v, String text) {
            if (actionMode == null) {
                actionMode = ((AppCompatActivity)v.getContext()).startSupportActionMode(new ActionBarCallback(text));
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
        void saveStatus() {
            if (descriptionEditText.getText().toString().compareTo("") != 0) {
                Goal goal = new Goal(UUID.randomUUID().toString(), descriptionEditText.getText().toString());
                goal.startDate = new Date();
                listener.insertGoal(goal);

                descriptionText.setText(descriptionEditText.getText());
            }
            viewSwitcher.showPrevious();
        }

        void discardStatus() {
            viewSwitcher.showPrevious();
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
                        if(trainingDetailEnterState != null) {
                            trainingDetailEnterState.discardStatus();
                            trainingDetailEnterState = null;
                        }
                        disableActionMode();
                        return true;
                    case R.id.item_add:
                        if(trainingDetailEnterState != null) {
                            trainingDetailEnterState.saveStatus();
                            trainingDetailEnterState = null;
                        }
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
    }

    private OnItemClickListener onItemClickListener;
    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
