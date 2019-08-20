package com.dobrowol.traininglog.holt_winters;


import com.dobrowol.traininglog.training_load.calculating.TrainingGoalLoad;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class HoltWinters {

    private ArrayList<Double> series;
    private int seasonLength;

    public HoltWinters(ArrayList<Double> series,int seasonLength){
        this.series = series;
        this.seasonLength = seasonLength;
    }

    private double calculate_initial_trend() {
        double sum = 0.0;
        for (int i = 0; i < seasonLength; i++) {
            double p1= series.get(i + seasonLength) - series.get(i);
            double p2 =  (series.get(i + seasonLength) - series.get(i)) / seasonLength;
            sum += (series.get(i + seasonLength) - series.get(i)) / seasonLength;
        }
        return sum / seasonLength;
    }

    private ArrayList<Double> initial_seasonal_components() {
        ArrayList <Double> seasonals = new ArrayList<>();
        ArrayList<Double> season_averages = new ArrayList<>();
        int n_seasons = series.size() / seasonLength;

        for (int j =0; j < n_seasons; j++) {
            double subsum = 0;
            for (int k = seasonLength *j; k < seasonLength *j + seasonLength; k++ ){
                subsum += series.get(k);
            }
            season_averages.add(subsum/ seasonLength);
        }
        for (int i = 0; i < seasonLength; i++) {
            double sum_of_vals_over_avg = 0.0;
            for (int j = 0; j < n_seasons; j++) {
                sum_of_vals_over_avg += series.get(seasonLength * j + i) - season_averages.get(j);

            }
            seasonals.add(sum_of_vals_over_avg / n_seasons);
        }
        return seasonals;
    }

    public ArrayList<Double> triple_exponential_smoothing(double alpha, double beta, double gamma, int number_of_predictions) {
        ArrayList<Double> result = new ArrayList<>();
        if(series.size() < 2* seasonLength){
            return result;
        }
        ArrayList<Double> seasonals = initial_seasonal_components();
        double smooth = series.get(0);
        double trend = calculate_initial_trend();
        for (int i = 0; i < series.size() + number_of_predictions; i++) {
            if (i == 0) {
                result.add(series.get(0));
                continue;
            }
            if (i >= series.size()) {
                double m = i - series.size() + 1;
                result.add((smooth + m * trend) + seasonals.get(i % seasonLength));
            } else {
                double val = series.get(i);
                double last_smooth = smooth;
                smooth = alpha * (val - seasonals.get(i % seasonLength)) + (1.0 - alpha) * (smooth + trend);
                trend = beta * (smooth - last_smooth) + (1 - beta) * trend;
                double seasonal = gamma * (val - smooth) + (1.0 - gamma) * seasonals.get(i % seasonLength);
                seasonals.set(i % seasonLength, seasonal);
                result.add(smooth + trend + seasonals.get(i % seasonLength));

            }

        }
        return result;
    }

    public void setSeries(List<Integer> integers) {
        for(Integer integer : integers){
            if(integer != null) {
                series.add(Double.valueOf(integer));
            }
        }
    }

    public void setDateLoadSeries(List<TrainingGoalLoad.DateLoad> dateLoads) {

    }
}
