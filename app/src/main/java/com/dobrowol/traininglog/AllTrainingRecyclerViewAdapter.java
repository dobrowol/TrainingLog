package com.dobrowol.traininglog;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dobrowol.traininglog.adding_training.Training;
import com.dobrowol.traininglog.training_load.displaying.MyValueFormatter;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
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

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Training item);
    }
    private final List<Training> mValues;
    private final OnListFragmentInteractionListener mListener;

    public AllTrainingRecyclerViewAdapter(List<Training> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_training, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.setDate(holder.mItem.date);

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
        holder.setDataChart(holder.mItem);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnChartValueSelectedListener {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        private HorizontalBarChart mChart;
        public Training mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.item_number);
            mContentView = view.findViewById(R.id.content);
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
            leftAxis.setAxisMaximum(20000);
            mChart.getAxisRight().setEnabled(false);
            mChart.getAxisLeft().setEnabled(false);

            mChart.getXAxis().setEnabled(false);
            mChart.getLegend().setEnabled(false);
            //XAxis xLabels = mChart.getXAxis();
            //xLabels.setPosition(XAxis.XAxisPosition.TOP);

        }

        public void setDate(Date date){
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM, dd", Locale.ENGLISH);
            String formatted = sdf.format(date);
            mIdView.setText(formatted);
        }

        public void setDataChart(Training training){
            ArrayList<BarEntry> values = new ArrayList<>();

            float [] trainingLoads = new float[3];
            trainingLoads[0] = training.general_load!=null?
                 training.general_load:0;
            trainingLoads[1] = training.specific_load!=null?
                    training.specific_load:0;
            trainingLoads[2] = training.competitive_load!=null?
                    training.competitive_load:0;

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
                set1.setColors(getColors());

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


        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        @Override
        public void onValueSelected(Entry e, Highlight h) {

        }

        @Override
        public void onNothingSelected() {

        }

        private int[] getColors() {

            // have as many colors as stack-values per entry
            int[] colors = new int[3];

            System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, 3);

            return colors;
        }
    }
}
