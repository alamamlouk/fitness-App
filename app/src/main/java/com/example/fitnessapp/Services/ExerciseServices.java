package com.example.fitnessapp.Services;

import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_NAME;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_PATH_PHOTO;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_STATE;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_DESCRIPTION;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_ID;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_NAME;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_PATH_IMG;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_STATE;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_TIME_TO_FINISH;
import static com.example.fitnessapp.DataBase.static_field.EXERCISE_TABLE_NAME;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fitnessapp.DataBase.DBHandler;
import com.example.fitnessapp.Entity.Exercise;
import com.example.fitnessapp.Entity.ExerciseDTO;

import java.util.ArrayList;
import java.util.List;

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
    public long addExercise(ExerciseDTO exerciseDTO){
        ContentValues values=new ContentValues();
        values.put(COLUMN_EXERCISE_NAME,exerciseDTO.getExercise_name());
        values.put(COLUMN_EXERCISE_TIME_TO_FINISH,exerciseDTO.getTime_to_finish());
        values.put(COLUMN_EXERCISE_PATH_IMG,"");
        values.put(COLUMN_EXERCISE_STATE,exerciseDTO.getState());
        values.put(COLUMN_EXERCISE_DESCRIPTION,"");
        return database.insert(EXERCISE_TABLE_NAME,null,values);
    }
    @SuppressLint("Range")
    public Exercise getActivityExercise(long exercise_id){
        Exercise exercise=new Exercise();
        String[]columns={
                COLUMN_EXERCISE_ID,
                COLUMN_EXERCISE_NAME,
                COLUMN_EXERCISE_TIME_TO_FINISH,
                COLUMN_EXERCISE_STATE
        };
        String selection = COLUMN_EXERCISE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(exercise_id)};
        Cursor cursor=database.query(
                EXERCISE_TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            exercise.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_EXERCISE_ID)));
            exercise.setExercise_name(cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE_NAME)));
            exercise.setTime_to_finish(cursor.getInt(cursor.getColumnIndex(COLUMN_EXERCISE_TIME_TO_FINISH)));
            exercise.setState(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY_STATE))));

            cursor.close();
        }

        return exercise;
    }
}
