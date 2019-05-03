package com.dobrowol.traininglog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dobrowol.traininglog.adding_exercise.Exercise;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.CustomViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Exercise item);
    }

    private List<Exercise> exerciseList;
    private Context mContext;

    public MyRecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    public void setExerciseList(List<Exercise> exerciseList){
        this.exerciseList = exerciseList;
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final Exercise exercise = exerciseList.get(i);

        //Setting text view title
        customViewHolder.descriptionText.setText(Html.fromHtml(exercise.description +" "+
                exercise.numberOfSeries +"x( " + exercise.numberOfSetsInSeries +"x"+exercise.distance+"m )"));

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(exercise);
            }
        };
        customViewHolder.descriptionText.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return (null != exerciseList ? exerciseList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView descriptionText;

        public CustomViewHolder(View view) {
            super(view);;
            this.descriptionText = view.findViewById(R.id.description);
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
