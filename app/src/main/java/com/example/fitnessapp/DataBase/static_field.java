package com.example.fitnessapp.DataBase;

public  class static_field {
    public static final String TABLE_NAME = "FitnessActivity";

    public static final String COLUMN_ACTIVITY_ID = "activity_id";
    public static final String COLUMN_ACTIVITY_NAME = "activity_name";
    public static final String COLUMN_ACTIVITY_NUMBER_OF_EXERCISE = "number_of_exercise";
    public static final String COLUMN_ACTIVITY_TIME_TO_FINISH = "time_to_finish";
    public static final String COLUMN_ACTIVITY_STATE = "state";
    public static final String COLUMN_ACTIVITY_PROGRESS = "progress";
    public static final String COLUMN_ACTIVITY_PATH_PHOTO = "path_photo";
    public static final String EXERCISE_TABLE_NAME ="FitnessExercise";
    public static final String COLUMN_EXERCISE_ID="exercise_id";
    public static final String COLUMN_EXERCISE_NAME="exercise_name";
    public static final String COLUMN_EXERCISE_TIME_TO_FINISH="time_to_finish";
    public static final String COLUMN_EXERCISE_STATE="state";
    public static final String COLUMN_EXERCISE_DESCRIPTION="exercise_description";
    public static final String COLUMN_EXERCISE_PATH_IMG="exercise_path_image";
    public static final String TABLE_ACTIVITY_EXERCISE_RELATION = "activity_exercise_relation";
    public static final String COLUMN_RELATION_ID = "id";
    public static final String COLUMN_ACTIVITY_ID_FK = "activity_id";
    public static final String COLUMN_EXERCISE_ID_FK = "exercise_id";
    public static final String DB_NAME="APP_FITNESS";
    public static final int DB_VERSION=8;

}
