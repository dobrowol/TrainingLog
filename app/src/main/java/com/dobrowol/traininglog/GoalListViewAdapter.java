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
import androidx.recyclerview.widget.RecyclerView;

import com.dobrowol.traininglog.adding_training.adding_exercise.AddExercise;
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

        void scrollToPosition(int adapterPosition);
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
        this.goals = goals;
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
               goal.startDate = new Date();
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
    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnKeyListener {

        TextView descriptionText;
        RecyclerView exercisesRecyclerView;
        TextView addExercise;
        OnItemClickListener listener;
        Goal goal;
        ViewSwitcher viewSwitcher;
        View view;

        TrainingDetailEnterState trainingDetailEnterState;
        NewGoalEnterState newGoalEnterState;
        ExistingGoalUpdateState existingGoalUpdateState;
        private ActionMode actionMode;
        private String password;

        CustomViewHolder(View view, OnItemClickListener listener) {
            super(view);
            this.view = view;
            this.listener = listener;
            this.descriptionText = view.findViewById(R.id.goalTextView);
            this.exercisesRecyclerView = view.findViewById(R.id.exercises_rv);
            this.addExercise = view.findViewById(R.id.addingExercise);
            view.setOnClickListener(this);
            //descriptionText.setEnabled(false);
            //addExercise.setEnabled(false);
            addExercise.setOnClickListener(this);

            view.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    setState(null);
                    disableActionMode();
                }
            });

            view.setOnKeyListener(this);
            descriptionText.setOnClickListener(this);
            trainingDetailEnterState = null;
            newGoalEnterState = new NewGoalEnterState(view, listener);
            existingGoalUpdateState = new ExistingGoalUpdateState(view, listener);

        }

        public void showTextView() {
            if (viewSwitcher.getNextView() instanceof TextView) {
                viewSwitcher.showNext();
            } else {
                viewSwitcher.showPrevious();
            }
        }

        public void showEditText() {
            if (viewSwitcher.getNextView() instanceof EditText) {
                viewSwitcher.showNext();
            } else {
                viewSwitcher.showPrevious();
            }
        }

        void fillView(Goal textAtPosition) {

            this.goal = textAtPosition;
            descriptionText.setText(textAtPosition.description);

            if (this.goal.id.equals(GoalListViewAdapter.EMPTY_GOAL.id)) {
                descriptionText.setTextColor(descriptionText.getResources().getColor(R.color.adding_text_colour));
                descriptionText.setTypeface(descriptionText.getTypeface(), Typeface.BOLD);
                addExercise.setVisibility(TextView.INVISIBLE);
                exercisesRecyclerView.setVisibility(RecyclerView.INVISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.goalTextView:
                    if (descriptionText.getText().equals(GoalListViewAdapter.EMPTY_GOAL.description)) {
                        setState(newGoalEnterState);
                        //trainingDetailEnterState.start();
                        //enableActionMode(v, "Dodaj cel");
                        showAlert("Dodaj cel");

                    } else {
                        //enableActionMode(v, "Edytuj cel");
                        setState(existingGoalUpdateState);
                        //trainingDetailEnterState.start();
                        showAlert("Edytuj cel");
                    }
                    break;
                case R.id.addingExercise:
                    enableActionMode(v, "Dodaj ćwiczenie");

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
                goal.startDate = new Date();
                listener.insertGoal(goal);

                //   descriptionText.setText(descriptionEditText.getText());
            }
        }

        void discardStatus() {
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            listener.scrollToPosition(getAdapterPosition());
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
                        setState(null);
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
            if(goal.id.compareTo(GoalListViewAdapter.EMPTY_GOAL.id) != 0){
                input.setText(goal.description);
            }
            alertDialog.setView(input);
            //alertDialog.setIcon(R.drawable.key);

            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                           descriptionText.setText( input.getText().toString());
                            if (trainingDetailEnterState != null) {
                                trainingDetailEnterState.saveStatus(input.getText().toString());
                                trainingDetailEnterState = null;
                            }
                        }
                    });

            alertDialog.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            setState(null);
                        }
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
