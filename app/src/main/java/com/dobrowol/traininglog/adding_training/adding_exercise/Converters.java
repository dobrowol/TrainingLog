package com.dobrowol.traininglog.adding_training.adding_exercise;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {
    public static final String DATE_FORMAT="YYYY-mm-dd";
    @TypeConverter
    private static Specificity fromTimestamp(Integer value) {
        return value == null ? null : Specificity.values()[value];
    }

    @TypeConverter
    private static Integer dateToTimestamp(Specificity specificity) {
        return specificity == null ? null : specificity.ordinal();
    }

    @TypeConverter
    public static ExerciseType fromIntegerToExerciseType(Integer value) {
        return value == null ? null : ExerciseType.values()[value];
    }

    @TypeConverter
    private static Integer fromExerciseTypeToInteger(ExerciseType exerciseType) {
        return exerciseType == null ? null : exerciseType.ordinal();
    }

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Intensity fromIntegerToIntensity(Integer value) {
        return value == null ? null : Intensity.values()[value];
    }

    @TypeConverter
    public static Integer fromIntensityToInteger(Intensity intensity) {
        return intensity == null ? null : intensity.ordinal();
    }
}