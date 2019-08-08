package com.dobrowol.traininglog;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalJoin;
import com.dobrowol.traininglog.training_load.calculating.TrainingGoalExerciseData;
import com.dobrowol.traininglog.training_load.displaying.MyValueFormatter;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Training} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AllTrainingRecyclerViewAdapter extends RecyclerView.Adapter<AllTrainingRecyclerViewAdapter.ViewHolder> {

    private List<Training> trainings;
    private List<TrainingGoalJoin> trainingGoalJoins;
    private List<TrainingGoalExerciseData> trainingGoalExerciseData;
    private Integer maximumLoad;
    private Integer numberOfExercises;

    void setMaximumLoad(int maximumLoad) {
        this.maximumLoad = maximumLoad;
    }

    AllTrainingRecyclerViewAdapter(OnListFragmentInteractionListener trainingsApp) {
        this.mListener = trainingsApp;
    }
    void setTrainings(List<Training> trainings){
        this.trainings = trainings;
    }
    void setTrainingGoalJoins(List<TrainingGoalJoin> trainingGoalJoins){
        this.trainingGoalJoins = trainingGoalJoins;
        notifyDataSetChanged();
    }

    public void setTrainingGoalLoads(List<TrainingGoalExerciseData> trainingGoalExerciseData) {
        this.trainingGoalExerciseData = trainingGoalExerciseData;
        notifyDataSetChanged();
    }

    public void setNumberOfExercises(Integer numberOfExercises) {
        this.numberOfExercises = numberOfExercises;
    }

    public void removeTraining(int adapterPosition) {
        mListener.onDeleteTraining(trainings.get(adapterPosition));
        trainings.remove(adapterPosition);
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Training item);
        void onDeleteTraining(Training training);
    }


    private final OnListFragmentInteractionListener mListener;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_training, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = trainings.get(position);
        List<Integer> loads = new ArrayList<>();
        if(trainingGoalJoins != null) {
            for (TrainingGoalJoin trainingGoalJoin : trainingGoalJoins) {
                if (trainingGoalJoin.trainingId.equals(holder.mItem.id)) {
                    loads.add(trainingGoalJoin.load);
                }
            }
        }
        if(trainingGoalExerciseData != null) {
            for (TrainingGoalExerciseData trainingGoalJoin : trainingGoalExerciseData) {
                if (trainingGoalJoin.trainingId.equals(holder.mItem.id)) {
                    loads.add(trainingGoalJoin.goalLoad);
                }
            }
        }

        holder.mLoads = loads;
        holder.setDate(holder.mItem.date);

        holder.setDataChart(holder.mItem, holder.mLoads);
    }

    @Override
    public int getItemCount() {
        if(trainings == null){
            return 0;
        }
        return trainings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnChartValueSelectedListener {
        final View mView;
        final TextView mIdView;
        List<Integer> mLoads;
        private HorizontalBarChart mChart;
        Training mItem;
        public RelativeLayout viewForeground;

        ViewHolder(View view) {
            super(view);
            mView = view;
            viewForeground = view.findViewById(R.id.view_training_foreground);
            mIdView = view.findViewById(R.id.item_number);

            mChart = view.findViewById(R.id.trainingLoadPreviewChart);

            mChart.setOnChartValueSelectedListener(this);

            mChart.getDescription().setEnabled(false);

            mChart.setMaxVisibleValueCount(40);

            // scaling can now only be done on x- and y-axis separately
            mChart.setPinchZoom(false);

            mChart.setDrawGridBackground(false);
            mChart.setDrawBarShadow(false);

            mChart.setDrawValueAboveBar(false);
            mChart.setHighlightFullBarEnabled(false);

            // change the position of the y-labels
            YAxis leftAxis = mChart.getAxisLeft();
            leftAxis.setValueFormatter(new MyValueFormatter("K"));
            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
            if(maximumLoad == null || numberOfExercises == null)
            {
                leftAxis.setAxisMaximum(0.0f);
            }
            else {
                leftAxis.setAxisMaximum(maximumLoad * numberOfExercises);
            }
            mChart.getAxisRight().setEnabled(false);
            mChart.getAxisLeft().setEnabled(false);

            mChart.getXAxis().setEnabled(false);
            mChart.getLegend().setEnabled(false);
        }

        public void setDate(Date date){
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM, dd", Locale.ENGLISH);
            String formatted = sdf.format(date);
            mIdView.setText(formatted);
        }

        void setDataChart(Training training, List<Integer> goalLoads){
            setListeners(training);

            ArrayList<BarEntry> values = new ArrayList<>();

            float [] trainingLoads = new float[goalLoads.size()];
            int j = 0;
            for(Integer load : goalLoads) {
                trainingLoads[j++] = load;
            }

            values.add(new BarEntry(
                        0,
                        trainingLoads));

            BarDataSet set1;

            if (mChart.getData() != null &&
                    mChart.getData().getDataSetCount() > 0) {
                set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
                set1.setValues(values);
                mChart.getData().notifyDataChanged();
                mChart.notifyDataSetChanged();
            } else {
                set1 = new BarDataSet(values,"");
                //set1.setDrawIcons(false);
                set1.setColors(getColors(3));

                ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);

                BarData data = new BarData(dataSets);
               // data.setValueFormatter(new StackedValueFormatter(false, "", 1));
               // data.setValueTextColor(Color.WHITE);

                mChart.setData(data);
            }

            mChart.setFitBars(true);
            mChart.invalidate();
        }

        private void setListeners(Training training) {
            View.OnClickListener onClickListener = v -> mListener.onListFragmentInteraction(training);
            mView.setOnClickListener(onClickListener);
            mChart.setOnClickListener(onClickListener);
            mIdView.setOnClickListener(onClickListener);
        }


        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }

        @Override
        public void onValueSelected(Entry e, Highlight h) {

        }

        @Override
        public void onNothingSelected() {

        }

        private int[] getColors(int number) {

            // have as many colors as stack-values per entry
            int[] colors = new int[number];

            System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, number);

            return colors;
        }
    }
}
