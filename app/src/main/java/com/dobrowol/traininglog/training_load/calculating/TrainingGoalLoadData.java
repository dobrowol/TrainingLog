package com.dobrowol.traininglog.training_load.calculating;

import androidx.annotation.Nullable;

import java.util.Date;

public class TrainingGoalLoadData {
    public String trainingJoinId;
    public String goalId;
    public String goalDescription;
    public int loadValue;
    public Date startDate;
    public Date date;
    public int specificity;

    @Override
    public boolean equals(@Nullable Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;
        TrainingGoalLoadData other = (TrainingGoalLoadData) object;
        return goalId.compareTo(other.goalId) == 0 && trainingJoinId.compareTo(other.trainingJoinId) == 0
                && loadValue == other.loadValue && specificity == other.specificity && startDate.compareTo(other.startDate) == 0
                && other.date.compareTo(date) == 0;
    }
}
