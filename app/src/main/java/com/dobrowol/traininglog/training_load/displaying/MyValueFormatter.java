package com.dobrowol.traininglog.training_load.displaying;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class MyValueFormatter implements  IAxisValueFormatter {

    private final DecimalFormat mFormat;
    private String suffix;

    public MyValueFormatter(String suffix) {
        mFormat = new DecimalFormat("###,###,###,##0.0");
        this.suffix = suffix;
    }


    public String getFormattedValue(float value) {
        return mFormat.format(value) + suffix;
    }


    public String getAxisLabel(float value, AxisBase axis) {
        if (axis instanceof XAxis) {
            return mFormat.format(value);
        } else if (value > 0) {
            return mFormat.format(value) + suffix;
        } else {
            return mFormat.format(value);
        }
    }


    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value) + suffix;
    }
}
