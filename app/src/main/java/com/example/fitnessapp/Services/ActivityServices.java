package com.example.fitnessapp.Services;

import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_ID;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_NAME;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_PATH_PHOTO;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_PROGRESS;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_TIME_EXERCISED;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_TIME_TO_FINISH;
import static com.example.fitnessapp.DataBase.static_field.TABLE_ACTIVITY_NAME;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.fitnessapp.DataBase.DBHandler;
import com.example.fitnessapp.Entity.Activity;
import com.example.fitnessapp.Entity.ActivityDTO;
import java.util.ArrayList;
import java.util.List;

public class ActivityServices {
    private SQLiteDatabase database;

    private final DBHandler dbHandler;

    public ActivityServices(Context context) {
        dbHandler = new DBHandler(context);
    }

    public void open() {
        database = dbHandler.getWritableDatabase();
    }

    public void close() {
        dbHandler.close();
    }

    //Add activity
    public long addActivity(ActivityDTO activity) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACTIVITY_NAME, activity.getActivity_name());
        values.put(COLUMN_ACTIVITY_TIME_TO_FINISH, activity.getTime_to_finish());
        values.put(COLUMN_ACTIVITY_PROGRESS, activity.getProgress());
        values.put(COLUMN_ACTIVITY_PATH_PHOTO, activity.getPath_photo());
        values.put(COLUMN_ACTIVITY_TIME_EXERCISED, activity.getTime_exercised());
        return database.insert(TABLE_ACTIVITY_NAME, null, values);
    }
    //Get All activity
    @SuppressLint("Range")
    public List<Activity> getAllActivity() {
        List<Activity> activities = new ArrayList<>();
        String[] columns = {
                COLUMN_ACTIVITY_ID,
                COLUMN_ACTIVITY_NAME,
                COLUMN_ACTIVITY_TIME_TO_FINISH,
                COLUMN_ACTIVITY_PROGRESS,
                COLUMN_ACTIVITY_PATH_PHOTO,
                COLUMN_ACTIVITY_TIME_EXERCISED
        };
        Cursor cursor = database.query(
                TABLE_ACTIVITY_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Activity activity = new Activity(
                        cursor.getLong(cursor.getColumnIndex(COLUMN_ACTIVITY_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY_NAME)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_ACTIVITY_TIME_TO_FINISH)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_ACTIVITY_PROGRESS)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY_PATH_PHOTO)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_ACTIVITY_TIME_EXERCISED))
                );

                activities.add(activity);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return activities;
    }

    //Delete all activity
    public void deleteActivity() {
        database.delete(TABLE_ACTIVITY_NAME, null, null);
    }
    public int getCompletedActivitiesCount() {
        String[] columns = {COLUMN_ACTIVITY_ID};
        String selection = COLUMN_ACTIVITY_PROGRESS + " = ?";
        String[] selectionArgs = {"1"};
        @SuppressLint("Recycle") Cursor cursor = database.query(
                TABLE_ACTIVITY_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        int completedActivitiesCount = cursor != null ? cursor.getCount() : 0;
        if (cursor != null) {
            cursor.close();
        }
        return completedActivitiesCount;
    }
    public int getActivitiesWithTimeExercisedGreaterThanZeroCount() {
        String[] columns = {COLUMN_ACTIVITY_ID};
        String selection = COLUMN_ACTIVITY_TIME_EXERCISED + " > ?";
        String[] selectionArgs = {"0"};

        @SuppressLint("Recycle") Cursor cursor = database.query(
                TABLE_ACTIVITY_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int activitiesWithTimeExercisedGreaterThanZeroCount = cursor != null ? cursor.getCount() : 0;

        if (cursor != null) {
            cursor.close();
        }

        return activitiesWithTimeExercisedGreaterThanZeroCount;
    }

    public int getSumOfTimeExercised() {
        String[] columns = {"SUM(" + COLUMN_ACTIVITY_TIME_EXERCISED + ")"};
        String selection = COLUMN_ACTIVITY_TIME_EXERCISED + " > ?";
        String[] selectionArgs = {"0"};

        @SuppressLint("Recycle") Cursor cursor = database.query(
                TABLE_ACTIVITY_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int sumOfTimeExercised = 0;

        if (cursor != null && cursor.moveToFirst()) {
            sumOfTimeExercised = cursor.getInt(0);
            cursor.close();
        }

        return sumOfTimeExercised;
    }

    public void updateTimeExercised(long activityId, int newTimeExercised) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_ACTIVITY_TIME_EXERCISED, newTimeExercised);
        String selection = COLUMN_ACTIVITY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(activityId)};
        database.update(TABLE_ACTIVITY_NAME, values, selection, selectionArgs);
    }

}

