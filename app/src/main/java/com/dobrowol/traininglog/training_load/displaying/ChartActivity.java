package com.dobrowol.traininglog.training_load.displaying;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.dobrowol.traininglog.R;
import com.dobrowol.traininglog.TrainingsApp;
import com.dobrowol.traininglog.adding_training.TrainingViewModel;
import com.dobrowol.traininglog.adding_training.adding_goal.GoalViewModel;
import com.dobrowol.traininglog.adding_training.adding_goal.TrainingGoalExerciseJoinViewModel;
import com.dobrowol.traininglog.holt_winters.HoltWinters;
import com.dobrowol.traininglog.training_load.calculating.TrainingGoalLoad;
import com.dobrowol.traininglog.training_load.calculating.TrainingGoalLoadData;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ChartActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener,
        OnChartValueSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int PERMISSION_STORAGE = 0;

    private LineChart chart;
    private HoltWinters holtWinters;
    private int seasonLength;
    private Integer numberOfGoals;
    private List<Integer> colors;
    private int currentColor;
    private List<Date> dates;
    private DateAxisValueFormatter xAxixFormatter;
    private List<TrainingGoalLoadData> trainingGoalLoadData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_linechart);

        setTitle("Training Load");
        numberOfGoals=100;
        generateColors();
        currentColor = 0;
        initializeObservers();


        {   // // Chart Style // //
            chart = findViewById(R.id.chart1);

            // background color
            chart.setBackgroundColor(Color.WHITE);

            // disable description text
            chart.getDescription().setEnabled(false);

            // enable touch gestures
            chart.setTouchEnabled(true);

            // set listeners
            chart.setOnChartValueSelectedListener(this);
            chart.setDrawGridBackground(false);

            // create marker to display box when values are selected
            //MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

            // Set the marker to the chart
            //mv.setChartView(chart);
            //chart.setMarker(mv);

            // enable scaling and dragging
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart.setPinchZoom(true);
        }

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();

            // vertical grid lines
            //xAxis.enableGridDashedLine(10f, 10f, 0f);

            xAxixFormatter = new DateAxisValueFormatter();
            xAxis.setValueFormatter(xAxixFormatter);
            xAxis.setDrawLabels(true);
            xAxis.setLabelRotationAngle(-45);
        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = chart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            chart.getAxisRight().setEnabled(false);

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);

            // axis range
            yAxis.setAxisMaximum(10000f);
            yAxis.setAxisMinimum(0f);
        }


       /* {   // // Create Limit Lines // //
            LimitLine llXAxis = new LimitLine(9f, "Index 10");
            llXAxis.setLineWidth(4f);
            llXAxis.enableDashedLine(10f, 10f, 0f);
            llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            llXAxis.setTextSize(10f);
            llXAxis.setTypeface(tfRegular);

            LimitLine ll1 = new LimitLine(150f, "Upper Limit");
            ll1.setLineWidth(4f);
            ll1.enableDashedLine(10f, 10f, 0f);
            ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            ll1.setTextSize(10f);
            ll1.setTypeface(tfRegular);

            LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
            ll2.setLineWidth(4f);
            ll2.enableDashedLine(10f, 10f, 0f);
            ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            ll2.setTextSize(10f);
            ll2.setTypeface(tfRegular);

            // draw limit lines behind data instead of on top
            yAxis.setDrawLimitLinesBehindData(true);
            xAxis.setDrawLimitLinesBehindData(true);

            // add limit lines
            yAxis.addLimitLine(ll1);
            yAxis.addLimitLine(ll2);
            //xAxis.addLimitLine(llXAxis);
        }*/

        // draw points over time
        //chart.animateX(1500);

        chart.invalidate();

        // getTrainingGoalExercise the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // draw legend entries as lines
        l.setForm(Legend.LegendForm.LINE);

        seasonLength = 12;
        holtWinters = new HoltWinters(new ArrayList<>(), seasonLength);

        setAppBarTitle();
    }
    void generateColors() {
        colors = new ArrayList<>();
        Random rnd = new Random();
        if(numberOfGoals == null || numberOfGoals == 0){
            numberOfGoals = 100;
        }
        for (int i = 0; i < 360; i += 360 / numberOfGoals) {
            colors.add(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
        }
    }
    private void initializeObservers() {
        TrainingViewModel trainingViewModel;
        trainingViewModel = ViewModelProviders.of(this).get(TrainingViewModel.class);
        trainingViewModel.getTrainingDates().observe(this, this::setDates);
        TrainingGoalExerciseJoinViewModel trainingGoalExerciseJoinViewModel;

        trainingGoalExerciseJoinViewModel = ViewModelProviders.of(this).get(TrainingGoalExerciseJoinViewModel.class);
        trainingGoalExerciseJoinViewModel.getTrainingLoadData().observe(this, this::setTrainingGoalLoadData);

        trainingGoalExerciseJoinViewModel.getMaximumExerciseLoad().observe(this, maximumLoad -> {
            if (maximumLoad != null) {
                setMaximumValue(maximumLoad);
            } }
        );

        GoalViewModel goalViewModel;

        goalViewModel = ViewModelProviders.of(this).get(GoalViewModel.class);

        goalViewModel.getNumberOfGoals().observe(this, this::setNumberOfGoals);
    }

    synchronized private void setDates(List<Date> date) {
        this.dates = date;
        xAxixFormatter.setDates(dates);
        setData();
    }

    private void setNumberOfGoals(Integer integer) {
        this.numberOfGoals = integer;
        generateColors();
    }

    private void setMaximumValue(Integer maximumLoad) {
        YAxis yAxis = chart.getAxisLeft();

        // axis range
        yAxis.setAxisMaximum(maximumLoad + maximumLoad/10);
    }

    private void setAppBarTitle() {

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }else{
            android.app.ActionBar actionBar1 = getActionBar();
            if(actionBar1 != null){
                actionBar1.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

   @Override
   public boolean onNavigateUp() {
       finish();
       return true;
   }
    private void setTrainingGoalLoadData(List<TrainingGoalLoadData> trainingGoalLoadData) {
        this.trainingGoalLoadData =trainingGoalLoadData;

        setData();
    }

    synchronized private void setData() {

        if(trainingGoalLoadData == null || dates ==null)
            return;

        TrainingGoalLoad trainingGoalLoad = new TrainingGoalLoad();

        HashMap<String, List<TrainingGoalLoad.DateLoad>> goalLoads;
        goalLoads = trainingGoalLoad.calculateGoalLoadsForManyTrainings(trainingGoalLoadData);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        Set<Long> dateSet = new HashSet<>();

        for(String key : goalLoads.keySet()) {
            ArrayList<Entry> values = new ArrayList<>();
            ArrayList<Double> forecastValues = new ArrayList<>();
            ArrayList<Entry> forecastEntries = new ArrayList<>();

            int i = 0;
            List<TrainingGoalLoad.DateLoad> dateLoads = goalLoads.get(key);
            if(dateLoads != null) {
                for (TrainingGoalLoad.DateLoad dateLoad : dateLoads) {
                    if (dateLoad != null) {
                        dateSet.add(dateLoad.date.getTime());
                        values.add(new Entry(dates.indexOf(dateLoad.date), dateLoad.load));
                    }
                }
                if (dateLoads.size() >= seasonLength) {
                    holtWinters.setDateLoadSeries(dateLoads);
                    forecastValues = holtWinters.triple_exponential_smoothing(0.716, 0.029, 0.993, 12);
                    for (Double forecast : forecastValues) {
                        if (forecast != null) {
                            forecastEntries.add(new Entry(i++, forecast.floatValue()));
                        }
                    }
                }
            }
            LineDataSet set1;
            LineDataSet forecastSet;


            set1 = getLineDataSet(key, values, R.drawable.fade_red);

            /*set1.setMode(set1.getMode() == LineDataSet.Mode.HORIZONTAL_BEZIER
                    ? LineDataSet.Mode.LINEAR
                    :  LineDataSet.Mode.HORIZONTAL_BEZIER);*/
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            dataSets.add(set1); // add the data sets

            if (!forecastValues.isEmpty()) {
                forecastSet = getLineDataSet("Forecast Load " + key, forecastEntries, R.drawable.fade_blue);
                dataSets.add(forecastSet);
            }
            // create a data object with the data sets

        }
            // set data
        LineData data = new LineData(dataSets);
        chart.setData(data);

        chart.invalidate();
    }

    private LineDataSet getLineDataSet(String load_name, ArrayList<Entry> values, int set_colour) {
        LineDataSet set1;
        set1 = new LineDataSet(values, load_name);

        // draw dashed line
        //set1.enableDashedLine(10f, 5f, 0f);

        // black lines and points
        set1.setColor(colors.get(currentColor));
        set1.setCircleColor(colors.get(currentColor));

        // line thickness and point size
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);

        // draw points as solid circles
        set1.setDrawCircleHole(false);

        // customize legend entry
        set1.setFormLineWidth(1f);
        //set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set1.setFormSize(15.f);

        // text size of values
        set1.setValueTextSize(9f);

        // draw selection line as dashed
        //set1.enableDashedHighlightLine(10f, 5f, 0f);

        // set the filled area
        set1.setDrawFilled(true);
        set1.setFillFormatter((dataSet, dataProvider) -> chart.getAxisLeft().getAxisMinimum());

        set1.setFillColor(colors.get(currentColor));
        // set color of filled area
        /*if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(this, set_colour);
            set1.setFillDrawable(drawable);
        } else {

        }*/
        currentColor++;
        return set1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.line, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionToggleValues: {
                List<ILineDataSet> sets = chart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setDrawValues(!set.isDrawValuesEnabled());
                }

                chart.invalidate();
                break;
            }
            case R.id.actionToggleIcons: {
                List<ILineDataSet> sets = chart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setDrawCircles(true);
                }

                chart.invalidate();
                break;
            }
            case R.id.actionToggleHighlight: {
                if(chart.getData() != null) {
                    chart.getData().setHighlightEnabled(!chart.getData().isHighlightEnabled());
                    chart.invalidate();
                }
                break;
            }
            case R.id.actionToggleFilled: {

                List<ILineDataSet> sets = chart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    if (set.isDrawFilledEnabled())
                        set.setDrawFilled(false);
                    else
                        set.setDrawFilled(true);
                }
                chart.invalidate();
                break;
            }
            case R.id.actionToggleCircles: {
                List<ILineDataSet> sets = chart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    if (set.isDrawCirclesEnabled())
                        set.setDrawCircles(false);
                    else
                        set.setDrawCircles(true);
                }
                chart.invalidate();
                break;
            }
            case R.id.actionToggleCubic: {
                List<ILineDataSet> sets = chart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setMode(set.getMode() == LineDataSet.Mode.CUBIC_BEZIER
                            ? LineDataSet.Mode.LINEAR
                            :  LineDataSet.Mode.CUBIC_BEZIER);
                }
                chart.invalidate();
                break;
            }
            case R.id.actionToggleStepped: {
                List<ILineDataSet> sets = chart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setMode(set.getMode() == LineDataSet.Mode.STEPPED
                            ? LineDataSet.Mode.LINEAR
                            :  LineDataSet.Mode.STEPPED);
                }
                chart.invalidate();
                break;
            }
            case R.id.actionToggleHorizontalCubic: {
                List<ILineDataSet> sets = chart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setMode(set.getMode() == LineDataSet.Mode.HORIZONTAL_BEZIER
                            ? LineDataSet.Mode.LINEAR
                            :  LineDataSet.Mode.HORIZONTAL_BEZIER);
                }
                chart.invalidate();
                break;
            }
            case R.id.actionTogglePinch: {
                if (chart.isPinchZoomEnabled())
                    chart.setPinchZoom(false);
                else
                    chart.setPinchZoom(true);

                chart.invalidate();
                break;
            }
            case R.id.actionToggleAutoScaleMinMax: {
                chart.setAutoScaleMinMaxEnabled(!chart.isAutoScaleMinMaxEnabled());
                chart.notifyDataSetChanged();
                break;
            }
            case R.id.animateX: {
                chart.animateX(2000);
                break;
            }
            case R.id.animateY: {
                chart.animateY(2000, Easing.EasingOption.EaseInBounce);
                break;
            }
            case R.id.animateXY: {
                chart.animateXY(2000, 2000);
                break;
            }
            case R.id.actionSave: {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    saveToGallery();
                } else {
                    requestStoragePermission(chart);
                }
                break;
            }
            case android.R.id.home:
                finish();
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    protected void saveToGallery() {
        saveToGallery(chart, "LineChartActivity1");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
        Log.i("LOW HIGH", "low: " + chart.getLowestVisibleX() + ", high: " + chart.getHighestVisibleX());
        Log.i("MIN MAX", "xMin: " + chart.getXChartMin() + ", xMax: " + chart.getXChartMax() + ", yMin: " + chart.getYChartMin() + ", yMax: " + chart.getYChartMax());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveToGallery();
            } else {
                Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    protected void requestStoragePermission(View view) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Snackbar.make(view, "Write permission is required to save image to gallery", Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, v -> ActivityCompat.requestPermissions(ChartActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_STORAGE)).show();
        } else {
            Toast.makeText(getApplicationContext(), "Permission Required!", Toast.LENGTH_SHORT)
                    .show();
            ActivityCompat.requestPermissions(ChartActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_STORAGE);
        }
    }

    protected void saveToGallery(Chart chart, String name) {
        if (chart.saveToGallery(name + "_" + System.currentTimeMillis(), 70))
            Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",
                    Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                    .show();
    }

}