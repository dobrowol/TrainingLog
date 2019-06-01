package com.dobrowol.traininglog.holt_winters;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class HoltWintersTest {

    private ArrayList<Double> series = new ArrayList<>(Arrays.asList(30.0,21.0,29.0,31.0,40.0,48.0,53.0,47.0,37.0,39.0,31.0,29.0,17.0,9.0,20.0,24.0,27.0,35.0,41.0,38.0,
            27.0,31.0,27.0,26.0,21.0,13.0,21.0,18.0,33.0,35.0,40.0,36.0,22.0,24.0,21.0,20.0,17.0,14.0,17.0,19.0,
            26.0,29.0,40.0,31.0,20.0,24.0,18.0,26.0,17.0,9.0,17.0,21.0,28.0,32.0,46.0,33.0,23.0,28.0,22.0,27.0,
            18.0,8.0,17.0,21.0,31.0,34.0,44.0,38.0,31.0,30.0,26.0,32.0));

    @Test
    public void triple_exponential_smoothing() {

       HoltWinters holtWinters = new HoltWinters(series, 12);

       ArrayList<Double> results = holtWinters.triple_exponential_smoothing(0.716, 0.029, 0.993, 24);

       assertEquals( 30.0, results.get(0) , 0.01);
       assertEquals( 20.34, results.get(1) , 0.01);
        assertEquals( 28.41, results.get(2) , 0.01);
        assertEquals( 30.43, results.get(3) , 0.01);
    }
}