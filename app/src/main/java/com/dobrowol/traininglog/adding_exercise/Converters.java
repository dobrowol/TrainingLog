package com.dobrowol.traininglog.adding_exercise;

import androidx.room.TypeConverter;

public class Converters {
    @TypeConverter
    public static Specificity fromTimestamp(Integer value) {
        return value == null ? null : Specificity.values()[value];
    }

    @TypeConverter
    public static Integer dateToTimestamp(Specificity specificity) {
        return specificity == null ? null : specificity.ordinal();
    }

    @TypeConverter
    public static ExerciseType fromIntegerToExerciseType(Integer value) {
        return value == null ? null : ExerciseType.values()[value];
    }

    @TypeConverter
    public static Integer fromExerciseTypeToInteger(ExerciseType exerciseType) {
        return exerciseType == null ? null : exerciseType.ordinal();
    }
}