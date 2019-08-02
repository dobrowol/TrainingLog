package com.dobrowol.traininglog;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.adding_exercise.AddExercise;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseDescription;
import com.dobrowol.traininglog.adding_training.adding_goal.Goal;
import com.dobrowol.traininglog.adding_training.adding_goal.GoalExercisePair;
import com.dobrowol.traininglog.adding_training.deleting_exercise.RecyclerItemTouchHelper;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GoalListViewAdapter extends RecyclerView.Adapter<GoalListViewAdapter.CustomViewHolder> {

    private Training training;
    private List<ExerciseDescription> exerciseDescriptions;
    private OnItemClickListener listener;
    private ArrayList<Goal> goals;
    private List<GoalExercisePair> goalExercisePairs;
    private LinkedHashMap<Goal, List<Exercise>> map = new LinkedHashMap<>();
    private CustomViewHolder viewHolder;
    private Context context;

    void discardStatus() {
        viewHolder.discardStatus();
    }

    public interface OnItemClickListener extends View.OnClickListener {
        void onItemClick(Goal item);

        void onItemRemove(Goal goal);

        void onExistingGoalEdit(String description);

        void insertGoal(Goal goal);

        void updateGoal( Goal newGoal);

        @Override
        void onClick(View v);

        void deleteGoal(Goal oldGoal);
    }

    GoalListViewAdapter(OnItemClickListener listener, Context context) {
        this.listener = listener;
        this.context = context;
        goals = new ArrayList<>();
    }
    void saveStatus(){
        viewHolder.saveStatus();
    }
    void setGoals(ArrayList<Goal> goals){
        this.goals = goals;
        setMap();
    }

    void setTraining(Training training){
        this.training = training;
    }
    void setExerciseDescriptions(List<ExerciseDescription> exerciseDescriptions){
        this.exerciseDescriptions = exerciseDescriptions;
    }
    private void setMap(){
        map.clear();
        if(goals != null){
            for(Goal goal : goals){
                List<Exercise> list = map.get(goal);
                if(list == null){
                    list = new ArrayList<>();
                    map.put(goal, list);
                }
            }
        }
        if(goalExercisePairs != null){
            for(GoalExercisePair goalExercise : goalExercisePairs){
                List<Exercise> list = map.get(goalExercise.goal);

                if(list == null) {
                    list = new ArrayList<>();
                }
                list.add(goalExercise.exercise);
                map.put(goalExercise.goal, list);

            }
        }
    }
    void setGoalsExercises(List<GoalExercisePair> goalsExercises){
        if(this.goalExercisePairs != null) {
            this.goalExercisePairs.clear();
        }
        this.goalExercisePairs = goalsExercises;
        setMap();

    }

    ArrayList<Goal> getGoals(){
        return goals;
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.goal_layout, null);
        viewHolder = new CustomViewHolder(view, listener, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int i) {
        int j = 0;
        for (Map.Entry<Goal, List<Exercise>> entry : map.entrySet()) {
            if(j == i) {
                customViewHolder.fillView(entry.getKey(), entry.getValue());
                return;
            }
            j++;
        }

    }

    @Override
    public int getItemCount() {
        return (null != goals ? goals.size() : 0);
    }

    private interface TrainingDetailEnterState{
        void saveStatus(String name);
        void discardStatus();
    }

    private class NewGoalEnterState implements TrainingDetailEnterState {
       OnItemClickListener listener;
       View view;

       // EditText descriptionEditText;

       NewGoalEnterState(View view, OnItemClickListener listener) {
           this.listener = listener;
           this.view = view;
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

        DeleteGoalState(View view, OnItemClickListener listener, Goal goal) {
            this.listener = listener;
            this.view = view;
            descriptionText = view.findViewById(R.id.goalTextView);
            oldGoal = goal;
        }

        @Override
        public void saveStatus(String name) {
            listener.deleteGoal(oldGoal);
        }

        @Override
        public void discardStatus() {
        }
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, MyRecyclerViewAdapter.OnItemClickListener {

        TextView descriptionText;
        RecyclerView exercisesRecyclerView;
        TextView addExercise;
        OnItemClickListener listener;
        Goal goal;
        View view;
        List<Exercise> exercises;
        Context context;

        TrainingDetailEnterState trainingDetailEnterState;
        NewGoalEnterState newGoalEnterState;
        ExistingGoalUpdateState existingGoalUpdateState;
        DeleteGoalState deleteGoalState;
        private ActionMode actionMode;
        private MyRecyclerViewAdapter exerciseAdapter;
        private ConstraintLayout constraintLayout;

        CustomViewHolder(View view, OnItemClickListener listener, Context context) {
            super(view);
            this.context = context;
            this.view = view;
            this.listener = listener;
            this.descriptionText = view.findViewById(R.id.goalTextView);
            this.exercisesRecyclerView = view.findViewById(R.id.exercises_rv);
            this.addExercise = view.findViewById(R.id.addingExercise);
            this.constraintLayout = view.findViewById(R.id.constraint_layout);
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

            exerciseAdapter = new MyRecyclerViewAdapter(this);
            exerciseAdapter.setExerciseDescriptionList(exerciseDescriptions);
            exercisesRecyclerView.setItemAnimator(new DefaultItemAnimator());
            exercisesRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

            exercisesRecyclerView.setAdapter(exerciseAdapter);

            ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(exercisesRecyclerView);
        }

        void fillView(Goal goal, List<Exercise> exercises) {
            this.goal = goal;
            this.exercises = exercises;
            descriptionText.setText(goal.description);
            deleteGoalState = new DeleteGoalState(view, listener, this.goal);
            exerciseAdapter.setExerciseList(exercises);
            exerciseAdapter.notifyDataSetChanged();
        }

        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
            if (viewHolder instanceof MyRecyclerViewAdapter.CustomViewHolder) {
                // get the removed item name to display it in snack bar
                String name = exercises.get(viewHolder.getAdapterPosition()).id;

// backup of removed item for undo purpose
                final Exercise deletedItem = exercises.get(viewHolder.getAdapterPosition());
                final int deletedIndex = viewHolder.getAdapterPosition();

                // remove the item from recycler view
                exerciseAdapter.removeItem(viewHolder.getAdapterPosition());

                // showing snack bar with Undo option
                Snackbar snackbar = Snackbar
                        .make(constraintLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", view -> {

                    // undo is selected, restore the deleted item
                    exerciseAdapter.restoreItem(deletedItem, deletedIndex);
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
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
                goal.goalStartDate = new Date();
                listener.insertGoal(goal);
        }

        void discardStatus() {
        }

        @Override
        public boolean onLongClick(View v) {
            if (v.getId() == R.id.goalTextView){
                    enableActionMode(v,"Usuń cel");
                    trainingDetailEnterState = deleteGoalState;
            }
            return true;
        }

        @Override
        public void onItemClick(Exercise item) {
            exercisesRecyclerView.requestFocus();
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
                        if(deleteGoalState != null) {
                            deleteGoalState.saveStatus(null);
                        }
                        disableActionMode();
                        return true;
                    case R.id.item_add:
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
