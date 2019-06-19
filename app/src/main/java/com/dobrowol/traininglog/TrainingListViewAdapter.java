package com.dobrowol.traininglog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.adding_exercise.Exercise;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseDescription;
import com.dobrowol.traininglog.adding_training.adding_exercise.ExerciseType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TrainingListViewAdapter extends RecyclerView.Adapter<TrainingListViewAdapter.CustomViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(Training item);

        void onItemRemove(Training training);
    }

    private OnItemClickListener listener;
    private ArrayList<Training> trainings;

    TrainingListViewAdapter(OnItemClickListener listener) {
        this.listener = listener;
        trainings = new ArrayList<>();
    }

    void setTrainings(ArrayList<Training> trainings){
        if(trainings != null && this.trainings != null && trainings.size() != this.trainings.size()) {
            this.trainings.clear();
            this.trainings = trainings;
        }

    }

    ArrayList<Training> getTrainings(){
        return trainings;
    }
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);

        return new CustomViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int i) {
        Training textAtPosition = trainings.get(i);
        customViewHolder.fillView(textAtPosition);

    }

    @Override
    public int getItemCount() {
        return (null != trainings ? trainings.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView descriptionText;
        Button btnRemove;
        OnItemClickListener listener;
        Training training;

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

        void fillView(Training textAtPosition) {
            String pattern = "yyyy-MM-dd HH:mm";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            this.training = textAtPosition;
            descriptionText.setText(simpleDateFormat.format(textAtPosition.date));
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.description:
                case R.id.list_row:
                    listener.onItemClick(training);
                    break;
                case R.id.btnAction:
                    listener.onItemRemove(training);
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
