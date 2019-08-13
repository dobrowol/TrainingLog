package com.dobrowol.traininglog.training_load.displaying;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class DateAxisValueFormatter implements IAxisValueFormatter {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd:hh:mm", Locale.ENGLISH);

    public DateAxisValueFormatter() {
        }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return sdf.format(new Date((long) value));
    }
}

