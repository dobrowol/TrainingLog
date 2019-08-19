package com.dobrowol.traininglog;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseDescription;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.CustomViewHolder> {

    final static Exercise EMPTY_EXERCISE = new Exercise();
    final static ExerciseDescription EMPTY_DESCRIPTION= new ExerciseDescription();
    void removeItem(int adapterPosition) {
        exerciseList.remove(adapterPosition);
        notifyDataSetChanged();
    }

    void restoreItem(Exercise deletedItem, int deletedIndex) {
        exerciseList.add(deletedIndex, deletedItem);
        notifyDataSetChanged();
    }

    public void removeItemTemporarily(int adapterPosition) {
        exerciseList.remove(adapterPosition);
        notifyDataSetChanged();
    }
     public void removeItemPermanently(int adapterPosition){

     }

    public interface OnItemClickListener {
        void onItemClick(Exercise item);

        void addExercise();
    }

    private List<Exercise> exerciseList;
    private List<ExerciseDescription> exerciseDescriptionList;
    private OnItemClickListener listener;
    private Context context;

    MyRecyclerViewAdapter(OnItemClickListener listener, Context context) {
        this.context = context;
        EMPTY_DESCRIPTION.eid="EmptyDescription";
        EMPTY_DESCRIPTION.description=context.getString(R.string.add_exercise);
        EMPTY_EXERCISE.exerciseDescriptionId="EmptyDescription";
        EMPTY_EXERCISE.id=EMPTY_DESCRIPTION.eid;
        exerciseList = new ArrayList<>();
        this.listener = listener;
        exerciseDescriptionList = new ArrayList<>();
    }

    void setExerciseList(List<Exercise> exerciseList){
        if(this.exerciseList != null) {
            this.exerciseList.clear();
        }
        this.exerciseList = exerciseList;
        this.exerciseList.add(EMPTY_EXERCISE);
    }

    void setExerciseDescriptionList(List<ExerciseDescription> exerciseList){
        this.exerciseDescriptionList = exerciseList;
        this.exerciseDescriptionList.add(EMPTY_DESCRIPTION);
    }

    List<Exercise> getExerciseList(){
        return exerciseList;
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int i) {
        Exercise textAtPosition = exerciseList.get(i);
        customViewHolder.fillView(textAtPosition);
    }

    @Override
    public int getItemCount() {
        return (null != exerciseList ? exerciseList.size() : 0);
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RelativeLayout viewForeground;
        TextView descriptionText;

        CustomViewHolder(View view) {
            super(view);
            this.viewForeground = view.findViewById(R.id.view_foreground);
            this.descriptionText = view.findViewById(R.id.description);
            descriptionText.setOnClickListener(this);
            viewForeground.setOnClickListener(this);
        }

        void fillView(Exercise textAtPosition) {
            ExerciseDescription ed = null;
            if(exerciseDescriptionList != null) {
                for (int i = 0; i < exerciseDescriptionList.size(); i++) {
                    if (exerciseDescriptionList.get(i).eid.equalsIgnoreCase(textAtPosition.exerciseDescriptionId)) {
                        ed = exerciseDescriptionList.get(i);
                    }
                }
            }
            if(ed != null) {
                if(ed.eid == EMPTY_DESCRIPTION.eid){
                    viewForeground.setBackgroundResource(R.drawable.edit_text_general_background);
                    viewForeground.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
                    descriptionText.setText(EMPTY_DESCRIPTION.description);
                    descriptionText.setBackgroundResource(R.drawable.edit_text_general_background);
                    descriptionText.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
                    descriptionText.setTextColor(ContextCompat.getColor(context, R.color.adding_text_colour));
                }
                else {
                    viewForeground.setBackgroundColor(Color.WHITE);
                    viewForeground.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    descriptionText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    descriptionText.setBackgroundColor(Color.WHITE);
                    descriptionText.setTextColor(Color.BLACK);
                    descriptionText.setText(String.format(Locale.ENGLISH, "%dx( %d x %dm) %s",
                            textAtPosition.numberOfSetsInSeries, textAtPosition.numberOfRepetitionsInSet, textAtPosition.distance,
                            ed.description
                    ));
                }
            }
        }

        @Override
        public void onClick(View v) {
            Exercise exercise = exerciseList.get(getAdapterPosition());
            if(exercise.id == EMPTY_EXERCISE.id){
                listener.addExercise();
            }
            else{
                listener.onItemClick(exerciseList.get(getAdapterPosition()));
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
