package com.example.fitnessapp.Services;

import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_DESCRIPTION;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_NAME;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_PATH_IMG;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_REPUTATION;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_TIME_TO_FINISH;
import static com.example.fitnessapp.DataBase.static_field.EXERCISE_TABLE_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.fitnessapp.DataBase.DBHandler;
import com.example.fitnessapp.Entity.ExerciseDTO;

public class ExerciseServices {
    private SQLiteDatabase database;

    private final DBHandler dbHandler;

    public ExerciseServices(Context context) {
        this.dbHandler =new DBHandler(context);
    }
    public void open(){
        database=dbHandler.getWritableDatabase();
    }
    public void close(){
        dbHandler.close();
    }
    public long addExercise(ExerciseDTO exerciseDTO) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXERCISE_NAME, exerciseDTO.getExercise_name());
        values.put(COLUMN_EXERCISE_TIME_TO_FINISH, exerciseDTO.getTime_to_finish());
        values.put(COLUMN_EXERCISE_PATH_IMG, exerciseDTO.getExercise_path_image());
        values.put(COLUMN_EXERCISE_DESCRIPTION, exerciseDTO.getExercise_description());
        values.put(COLUMN_EXERCISE_REPUTATION, exerciseDTO.getReputation());
        return database.insert(EXERCISE_TABLE_NAME, null, values);
    }
}
