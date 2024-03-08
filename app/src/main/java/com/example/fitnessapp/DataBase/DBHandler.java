package com.example.fitnessapp.DataBase;

import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_ID;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_ID_FK;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_NAME;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_PATH_PHOTO;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_PROGRESS;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_TIME_EXERCISED;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_TIME_TO_FINISH;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_DESCRIPTION;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_ID;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_ID_FK;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_NAME;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_PATH_IMG;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_REPUTATION;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_TIME_TO_FINISH;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_FINISHED_TIME;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_RELATION_EXERCISE_FINISHED_OR_NOT;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_RELATION_ID;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_USER_BMI;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_USER_HEIGHT;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_USER_ID;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_USER_NAME;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_USER_WEIGHT;
import static com.example.fitnessapp.DataBase.static_field.DB_NAME;
import static com.example.fitnessapp.DataBase.static_field.DB_VERSION;
import static com.example.fitnessapp.DataBase.static_field.EXERCISE_TABLE_NAME;
import static com.example.fitnessapp.DataBase.static_field.TABLE_ACTIVITY_EXERCISE_RELATION;
import static com.example.fitnessapp.DataBase.static_field.TABLE_ACTIVITY_NAME;
import static com.example.fitnessapp.DataBase.static_field.TABLE_USER_NAME;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String userQuery = "CREATE TABLE " +
                TABLE_USER_NAME + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_NAME + " TEXT, " +
                COLUMN_USER_WEIGHT + " REAL, " +
                COLUMN_USER_HEIGHT + " REAL, " +
                COLUMN_USER_BMI + " REAL);";
        String activityQuery = " CREATE TABLE " + TABLE_ACTIVITY_NAME + " ( " +
                COLUMN_ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ACTIVITY_NAME + " TEXT ," +
                COLUMN_ACTIVITY_TIME_TO_FINISH + " INTEGER, " +
                COLUMN_ACTIVITY_TIME_EXERCISED + " INTEGER, " +
                COLUMN_ACTIVITY_PROGRESS + " INTEGER ," +
                COLUMN_ACTIVITY_PATH_PHOTO + " TEXT ); ";
        String exerciseQuery = " CREATE TABLE " + EXERCISE_TABLE_NAME + " ( " +
                COLUMN_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EXERCISE_NAME + " TEXT ," +
                COLUMN_EXERCISE_TIME_TO_FINISH + " INTEGER ," +
                COLUMN_EXERCISE_DESCRIPTION + " TEXT ," +
                COLUMN_EXERCISE_REPUTATION + " INTEGER ," +
                COLUMN_EXERCISE_PATH_IMG + " TEXT );";
        String relationQuery =
                "CREATE TABLE " + TABLE_ACTIVITY_EXERCISE_RELATION + " (" +
                        COLUMN_RELATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_ACTIVITY_ID_FK + " INTEGER, " +
                        COLUMN_EXERCISE_ID_FK + " INTEGER, " +
                        COLUMN_FINISHED_TIME + " INTEGER ," +
                        COLUMN_RELATION_EXERCISE_FINISHED_OR_NOT + " INTEGER ," +
                        "FOREIGN KEY(" + COLUMN_ACTIVITY_ID_FK + ") REFERENCES " + TABLE_ACTIVITY_NAME + "(" + COLUMN_ACTIVITY_ID + "), " +
                        "FOREIGN KEY(" + COLUMN_EXERCISE_ID_FK + ") REFERENCES " + EXERCISE_TABLE_NAME + "(" + COLUMN_EXERCISE_ID + "));";
        db.execSQL(userQuery);
        db.execSQL(activityQuery);
        db.execSQL(exerciseQuery);
        db.execSQL(relationQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_ACTIVITY_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " + EXERCISE_TABLE_NAME);
        db.execSQL(" DROP TABLE IF EXISTS "+ TABLE_ACTIVITY_EXERCISE_RELATION);
        onCreate(db);
    }


}
