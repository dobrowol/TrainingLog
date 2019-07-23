package com.dobrowol.traininglog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.adding_exercise.AddExercise;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.adding_training.adding_goal.Goal;
import com.dobrowol.traininglog.adding_training.adding_goal.GoalExercise;
import com.dobrowol.traininglog.adding_training.adding_goal.GoalExerciseList;
import com.dobrowol.traininglog.adding_training.adding_goal.GoalExercisePair;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GoalListViewAdapter extends RecyclerView.Adapter<GoalListViewAdapter.CustomViewHolder> {

    private Training training;

    void discardStatus() {
        viewHolder.discardStatus();
    }

    public interface OnItemClickListener extends View.OnClickListener {
        void onItemClick(Goal item);

        void onItemRemove(Goal goal);

        void onExistingGoalEdit(String description);

        void onNewExerciseEnter();

        void insertGoal(Goal goal);

        void insertExercise(Exercise exercise);

        void updateExercise(Exercise newExercise);

        void updateGoal( Goal newGoal);

        @Override
        void onClick(View v);

        void deleteGoal(Goal oldGoal);
    }

    private OnItemClickListener listener;
    private ArrayList<Goal> goals;
    private List<GoalExercisePair> goalExercisePairs;
    private LinkedHashMap<Goal, List<Exercise>> map = new LinkedHashMap<>();
    private CustomViewHolder viewHolder;

    GoalListViewAdapter(OnItemClickListener listener) {
        this.listener = listener;
        goals = new ArrayList<>();
    }
    void saveStatus(){
        viewHolder.saveStatus();
    }
    void setGoals(ArrayList<Goal> goals){
        this.goals = goals;
        for(Goal goal : goals){
            List<Exercise> list = map.get(goal);
            if(list == null){
                list = new ArrayList<>();
                map.put(goal, list);
            }
        }

    }

    void setTraining(Training training){
        this.training = training;
    }
    void setGoalsExercises(List<GoalExercisePair> goalsExercises){
        map.clear();
        if(this.goals != null) {
            this.goalExercisePairs.clear();
        }
        this.goalExercisePairs = goalsExercises;
        if(goalsExercises != null){
            for(GoalExercisePair goalExercise : goalsExercises){
                List<Exercise> list = map.get(goalExercise.goal);

                if(list == null)
                {
                    list = new ArrayList<>();
                    map.put(goalExercise.goal, list);
                }
                else {
                    list.add(goalExercise.exercise);
                }
            }
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
        Map.Entry<Goal, List<Exercise>> textAtPosition = (Map.Entry<Goal, List<Exercise>>) map.entrySet().toArray()[i];
        customViewHolder.fillView(textAtPosition.getKey(), textAtPosition.getValue());
    }

    @Override
    public int getItemCount() {
        return (null != goals ? goals.size() : 0);
    }

    private interface TrainingDetailEnterState{
        void start();
        void saveStatus(String name);
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

       // EditText descriptionEditText;

       NewGoalEnterState(View view, OnItemClickListener listener) {
           this.listener = listener;
           this.view = view;
       }
        @Override
        public void start(){
        //    descriptionEditText.setText("");
        }
       @Override
       public void saveStatus(String name) {

           if (name != null && name.compareTo("") != 0) {
               Goal goal = new Goal(UUID.randomUUID().toString(), name);
               goal.goalStartDate = new Date();
               listener.insertGoal(goal);
           }
       }

       @Override
       public void discardStatus() {
       }
   }
       private class ExistingGoalUpdateState implements TrainingDetailEnterState{
           OnItemClickListener listener;
           View view;
           Goal oldGoal;
           TextView descriptionText;
          // EditText descriptionEditText;

           ExistingGoalUpdateState(View view, OnItemClickListener listener){
               this.listener = listener;
               this.view = view;
               descriptionText = view.findViewById(R.id.goalTextView);
               oldGoal = new Goal(null, descriptionText.getText().toString());
           }
           @Override
           public void start(){
              // descriptionEditText.setText(descriptionText.getText());
               listener.onExistingGoalEdit(descriptionText.getText().toString());

           }
           @Override
           public void saveStatus(String name) {

              if (name != null && name.compareTo("")!=0) {
                  Goal newGoal = new Goal(null, name);
                  listener.updateGoal(newGoal);

                  descriptionText.setText(name);
               }
           }

           @Override
           public void discardStatus() {
           }
    }
    private class DeleteGoalState implements TrainingDetailEnterState{
        OnItemClickListener listener;
        View view;
        Goal oldGoal;
        TextView descriptionText;

        public DeleteGoalState(View view, OnItemClickListener listener, Goal goal) {
            this.listener = listener;
            this.view = view;
            descriptionText = view.findViewById(R.id.goalTextView);
            oldGoal = goal;
        }

        @Override
        public void start(){
        }
        @Override
        public void saveStatus(String name) {

            if (name != null && name.compareTo("")!=0) {
                Goal newGoal = new Goal(null, name);
                listener.deleteGoal(oldGoal);
            }
        }

        @Override
        public void discardStatus() {
        }
    }
    private class NewExerciseAdd implements TrainingDetailEnterState{
        OnItemClickListener listener;
        View view;
        Goal oldGoal;
        TextView descriptionText;
        //EditText descriptionEditText;

        NewExerciseAdd(View view, OnItemClickListener listener){
            this.listener = listener;
            this.view = view;
            descriptionText = view.findViewById(R.id.goalTextView);
            oldGoal = new Goal(null, descriptionText.getText().toString());
        }
        @Override
        public void start(){
            Intent intent = new Intent(view.getContext() ,AddExercise.class);
            ((AppCompatActivity)view.getContext()).startActivity(intent);
        }
        @Override
        public void saveStatus(String name) {

        }

        @Override
        public void discardStatus() {

        }
    }
    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView descriptionText;
        RecyclerView exercisesRecyclerView;
        TextView addExercise;
        OnItemClickListener listener;
        Goal goal;
        View view;

        TrainingDetailEnterState trainingDetailEnterState;
        NewGoalEnterState newGoalEnterState;
        ExistingGoalUpdateState existingGoalUpdateState;
        DeleteGoalState deleteGoalState;
        private ActionMode actionMode;
        private MyRecyclerViewAdapter exerciseAdapter;
        private List<Exercise> exercises;

        CustomViewHolder(View view, OnItemClickListener listener) {
            super(view);
            this.view = view;
            this.listener = listener;
            this.descriptionText = view.findViewById(R.id.goalTextView);
            this.exercisesRecyclerView = view.findViewById(R.id.exercises_rv);
            this.addExercise = view.findViewById(R.id.addingExercise);
            view.setOnClickListener(this);
            addExercise.setOnClickListener(this);
            descriptionText.setOnLongClickListener(this);

            view.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    setState(null);
                    disableActionMode();
                }
            });
            descriptionText.setOnClickListener(this);
            trainingDetailEnterState = null;
            newGoalEnterState = new NewGoalEnterState(view, listener);
            existingGoalUpdateState = new ExistingGoalUpdateState(view, listener);
            exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false));

            exerciseAdapter = new MyRecyclerViewAdapter();
            exercisesRecyclerView.setAdapter(exerciseAdapter);

        }

        void fillView(Goal goal, List<Exercise> exercises) {
            this.goal = goal;
            this.exercises = exercises;
            descriptionText.setText(goal.description);
            deleteGoalState = new DeleteGoalState(view, listener, this.goal);
            exerciseAdapter.setExerciseList(exercises);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.goalTextView:
                    setState(existingGoalUpdateState);
                    showAlert("Edytuj cel");
                    break;
                case R.id.addingExercise:
                    AddExercise.startNewInstance(view.getContext(), training, goal);
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
            if (trainingDetailEnterState != null) {
                trainingDetailEnterState.discardStatus();
            }
            trainingDetailEnterState = newGoalEnterState;
        }

        private void enableActionMode(View v, String text) {
            if (actionMode == null) {
                actionMode = ((AppCompatActivity) v.getContext()).startSupportActionMode(new ActionBarCallback(text));
            }
            if (actionMode != null) {
                actionMode.invalidate();
            }
        }

        private void disableActionMode() {
            if (actionMode != null) {
                actionMode.finish();
                actionMode = null;
            }
        }

        void saveStatus() {
            if (true) {
                // Goal goal = new Goal(UUID.randomUUID().toString(), descriptionEditText.getText().toString());
                goal.goalStartDate = new Date();
                listener.insertGoal(goal);
            }
        }

        void discardStatus() {
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()){
                case R.id.goalTextView:
                    enableActionMode(v,"Usuń cel");
                    trainingDetailEnterState = deleteGoalState;
                    break;
            }
            return false;
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
                            trainingDetailEnterState.saveStatus("");
                            trainingDetailEnterState = null;
                        }
                        disableActionMode();
                        return true;
                    case R.id.item_add:
                        if (trainingDetailEnterState != null) {
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
        public void showAlert(String text){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
            alertDialog.setTitle("GOAL");
            alertDialog.setMessage(text);

            final EditText input = new EditText(view.getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);

            input.setText(goal.description);

            alertDialog.setView(input);

            alertDialog.setPositiveButton("YES",
                    (dialog, which) -> {
                       descriptionText.setText( input.getText().toString());
                        if (trainingDetailEnterState != null) {
                            trainingDetailEnterState.saveStatus(input.getText().toString());
                            trainingDetailEnterState = null;
                        }
                    });

            alertDialog.setNegativeButton("NO",
                    (dialog, which) -> {
                        dialog.cancel();
                        setState(null);
                    });

            alertDialog.show();
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
