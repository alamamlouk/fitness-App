package com.example.fitnessapp.Services;

import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_ID;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_NAME;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_NUMBER_OF_EXERCISE;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_PATH_PHOTO;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_PROGRESS;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_STATE;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_TIME_TO_FINISH;
import static com.example.fitnessapp.DataBase.static_field.TABLE_NAME;
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
        values.put(COLUMN_ACTIVITY_STATE, activity.getState());
        values.put(COLUMN_ACTIVITY_PROGRESS, activity.getProgress());
        values.put(COLUMN_ACTIVITY_NUMBER_OF_EXERCISE, activity.getNumber_of_exercise());
        values.put(COLUMN_ACTIVITY_PATH_PHOTO, activity.getPath_photo());
        return database.insert(TABLE_NAME, null, values);
    }

    //Get All activity
    @SuppressLint("Range")
    public List<Activity> getAllActivity() {
        List<Activity> activities = new ArrayList<>();
        String[] columns = {
                COLUMN_ACTIVITY_ID,
                COLUMN_ACTIVITY_NAME,
                COLUMN_ACTIVITY_TIME_TO_FINISH,
                COLUMN_ACTIVITY_STATE,
                COLUMN_ACTIVITY_PROGRESS,
                COLUMN_ACTIVITY_NUMBER_OF_EXERCISE,
                COLUMN_ACTIVITY_PATH_PHOTO
        };
        Cursor cursor = database.query(
                TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Activity activity = new Activity();
                activity.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ACTIVITY_ID)));
                activity.setActivity_name(cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY_NAME)));
                activity.setNumber_of_exercise(cursor.getInt(cursor.getColumnIndex(COLUMN_ACTIVITY_NUMBER_OF_EXERCISE)));
                activity.setTime_to_finish(cursor.getInt(cursor.getColumnIndex(COLUMN_ACTIVITY_TIME_TO_FINISH)));
                activity.setState(cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY_STATE)));
                activity.setProgress(cursor.getInt(cursor.getColumnIndex(COLUMN_ACTIVITY_PROGRESS)));
                activity.setPath_photo(cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY_PATH_PHOTO)));
                activities.add(activity);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return activities;
    }
    //Delete all activity
    public void deleteActivity(){
//        String whereClause=COLUMN_ACTIVITY_ID+" =? ";
//        String[] whereArgs = {String.valueOf(activityId)};
        database.delete(TABLE_NAME, null, null);

    }
    public int getNumberOfActivityFinished(){
        String[] columns = {COLUMN_ACTIVITY_ID};
        String selection = COLUMN_ACTIVITY_PROGRESS + " = ?";
        String[] selectionArgs = {"100"};
        Cursor cursor = database.query(
                TABLE_NAME,
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

    // get number of exercises In Progress
    public Long getNumberOfExercisesInProgress(){
        String[] columns = {COLUMN_ACTIVITY_ID};
        String selection = COLUMN_ACTIVITY_STATE + " = ?";
        String[] selectionArgs = {"In Progress"};

        Cursor cursor = database.query(
                TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        long numberOfActivitiesInProgress = cursor != null ? cursor.getCount() : 0;

        if (cursor != null) {
            cursor.close();
        }

        return numberOfActivitiesInProgress;
    }
    // get time spent on activity
    public int getTimeSpent(){
        String[] columns = {COLUMN_ACTIVITY_TIME_TO_FINISH};
        String selection = COLUMN_ACTIVITY_STATE + " = ? AND " + COLUMN_ACTIVITY_PROGRESS + " = ?";
        String[] selectionArgs = {"Completed", "100"};

        Cursor cursor = database.query(
                TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int totalTimeSpent = 0;

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int timeToFinish = cursor.getInt(cursor.getColumnIndex(COLUMN_ACTIVITY_TIME_TO_FINISH));
                totalTimeSpent += timeToFinish;
            } while (cursor.moveToNext());

            cursor.close();
        }
        return totalTimeSpent;
    }
    }

