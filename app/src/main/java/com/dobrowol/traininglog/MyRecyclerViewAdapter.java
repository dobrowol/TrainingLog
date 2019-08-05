package com.dobrowol.traininglog;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseDescription;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.CustomViewHolder> {

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
    }

    private List<Exercise> exerciseList;
    private List<ExerciseDescription> exerciseDescriptionList;
    private OnItemClickListener listener;

    MyRecyclerViewAdapter(OnItemClickListener listener) {
        exerciseList = new ArrayList<>();
        this.listener = listener;
        exerciseDescriptionList = new ArrayList<>();
    }

    void setExerciseList(List<Exercise> exerciseList){
        if(this.exerciseList != null) {
            this.exerciseList.clear();
        }
        this.exerciseList = exerciseList;
    }

    void setExerciseDescriptionList(List<ExerciseDescription> exerciseList){
        this.exerciseDescriptionList = exerciseList;
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
        RelativeLayout viewBackground;
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
                descriptionText.setText(String.format(Locale.ENGLISH, "%dx( %d x %dm) + %s",
                        textAtPosition.numberOfSetsInSeries, textAtPosition.numberOfRepetitionsInSet, textAtPosition.distance,
                        ed.description
                ));
            }
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(exerciseList.get(getAdapterPosition()));
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
