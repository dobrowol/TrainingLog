package com.dobrowol.traininglog;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dobrowol.traininglog.adding_exercise.Exercise;

import java.util.ArrayList;
import java.util.Locale;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.CustomViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Exercise item);
    }

    private ArrayList<Exercise> exerciseList;

    MyRecyclerViewAdapter() {
        exerciseList = new ArrayList<>();
    }

    public void setExerciseList(ArrayList<Exercise> exerciseList){
        this.exerciseList = exerciseList;
    }

    public ArrayList<Exercise> getExerciseList(){
        return exerciseList;
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        Exercise textAtPosition = exerciseList.get(i);
        customViewHolder.fillView(textAtPosition);
    }

    @Override
    public int getItemCount() {
        return (null != exerciseList ? exerciseList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView descriptionText;

        CustomViewHolder(View view) {
            super(view);
            this.descriptionText = view.findViewById(R.id.description);
        }

        void fillView(Exercise textAtPosition) {

            descriptionText.setText(String.format(Locale.ENGLISH,"%dx( %d x %dm) + %s",
                    textAtPosition.numberOfSetsInSeries, textAtPosition.numberOfRepetitionsInSet, textAtPosition.distance,
                    textAtPosition.description));
        }
    }

    private OnItemClickListener onItemClickListener;
    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
