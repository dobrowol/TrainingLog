package com.dobrowol.traininglog.training_load.displaying;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

class DateAxisValueFormatter implements IAxisValueFormatter {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd:hh:mm", Locale.ENGLISH);

    List<Date> dates;
    public DateAxisValueFormatter() {
        }

        void setDates(List<Date> dates){
            this.dates = dates;
        }
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if(value >=0)
        return sdf.format(dates.get((int)value));

        return "";
    }
}

