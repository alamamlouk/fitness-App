package com.example.fitnessapp.Services;

import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_ID;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_ID_FK;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_PROGRESS;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_DESCRIPTION;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_ID;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_ID_FK;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_NAME;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_PATH_IMG;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_REPUTATION;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_TIME_TO_FINISH;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_FINISHED_TIME;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_RELATION_EXERCISE_FINISHED_OR_NOT;
import static com.example.fitnessapp.DataBase.static_field.EXERCISE_TABLE_NAME;
import static com.example.fitnessapp.DataBase.static_field.TABLE_ACTIVITY_EXERCISE_RELATION;
import static com.example.fitnessapp.DataBase.static_field.TABLE_ACTIVITY_NAME;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.fitnessapp.DataBase.DBHandler;
import com.example.fitnessapp.Entity.Exercise;
import com.example.fitnessapp.Entity.ExerciseByDay;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RelationServices {
    private SQLiteDatabase database;

    private final DBHandler dbHandler;

    public RelationServices(Context context) {
        this.dbHandler = new DBHandler(context);
    }

    public void open() {
        database = dbHandler.getWritableDatabase();
    }

    public void close() {
        dbHandler.close();
    }
    public void deleteRelation() {
        open();
        database.delete(TABLE_ACTIVITY_EXERCISE_RELATION, null, null);
        close();
    }

    public void addRelation(long activity_id, long exercise_id) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACTIVITY_ID_FK, activity_id);
        values.put(COLUMN_EXERCISE_ID_FK, exercise_id);
        values.put(COLUMN_RELATION_EXERCISE_FINISHED_OR_NOT, 0);
        database.insert(TABLE_ACTIVITY_EXERCISE_RELATION, null, values);
    }

    @SuppressLint("Range")
    public List<Exercise> getExercisesByActivity(long activity_id) {
        List<Exercise> exercises = new ArrayList<>();

        String[] columns = {
                EXERCISE_TABLE_NAME + "." + COLUMN_EXERCISE_ID,
                EXERCISE_TABLE_NAME + "." + COLUMN_EXERCISE_NAME,
                EXERCISE_TABLE_NAME + "." + COLUMN_EXERCISE_TIME_TO_FINISH,
                EXERCISE_TABLE_NAME + "." + COLUMN_EXERCISE_DESCRIPTION,
                EXERCISE_TABLE_NAME + "." + COLUMN_EXERCISE_REPUTATION,
                EXERCISE_TABLE_NAME + "." + COLUMN_EXERCISE_PATH_IMG
        };

        String selection = TABLE_ACTIVITY_EXERCISE_RELATION + "." + COLUMN_ACTIVITY_ID_FK + " = ?";
        String[] selectionArgs = {String.valueOf(activity_id)};

        String joinTable = EXERCISE_TABLE_NAME + " INNER JOIN " + TABLE_ACTIVITY_EXERCISE_RELATION +
                " ON " + EXERCISE_TABLE_NAME + "." + COLUMN_EXERCISE_ID + " = " +
                TABLE_ACTIVITY_EXERCISE_RELATION + "." + COLUMN_EXERCISE_ID_FK;

        @SuppressLint("Recycle") Cursor cursor = database.query(
                joinTable,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Exercise exercise = new Exercise(
                        cursor.getLong(cursor.getColumnIndex(COLUMN_EXERCISE_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE_NAME)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_EXERCISE_TIME_TO_FINISH)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_EXERCISE_REPUTATION)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE_PATH_IMG))
                        );

                exercises.add(exercise);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return exercises;
    }

    public void updateExerciseProgress(long activityId, long exerciseId, int newProgress) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_RELATION_EXERCISE_FINISHED_OR_NOT, newProgress);

        String selection = COLUMN_ACTIVITY_ID_FK + " = ? AND " + COLUMN_EXERCISE_ID_FK + " = ?";
        String[] selectionArgs = {String.valueOf(activityId), String.valueOf(exerciseId)};

        database.update(TABLE_ACTIVITY_EXERCISE_RELATION, values, selection, selectionArgs);

        if (areAllExercisesFinishedForActivity(activityId)) {
            updateActivityProgress(activityId, 1);
        }
    }

    @SuppressLint("Range")
    public int getExerciseProgress(long activityId, long exerciseId) {

        String[] columns = {COLUMN_RELATION_EXERCISE_FINISHED_OR_NOT};
        String selection = COLUMN_ACTIVITY_ID_FK + " = ? AND " + COLUMN_EXERCISE_ID_FK + " = ?";
        String[] selectionArgs = {String.valueOf(activityId), String.valueOf(exerciseId)};

        Cursor cursor = database.query(
                TABLE_ACTIVITY_EXERCISE_RELATION,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int exerciseProgress = -1;

        if (cursor != null && cursor.moveToFirst()) {
            exerciseProgress = cursor.getInt(cursor.getColumnIndex(COLUMN_RELATION_EXERCISE_FINISHED_OR_NOT));
            cursor.close();
        }
        return exerciseProgress;
    }
    public int getNumberOfExercisesForActivity(long activityId) {
        String[] columns = {COLUMN_EXERCISE_ID_FK};
        String selection = COLUMN_ACTIVITY_ID_FK + " = ?";
        String[] selectionArgs = {String.valueOf(activityId)};
        Cursor cursor = database.query(
                TABLE_ACTIVITY_EXERCISE_RELATION,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        int numberOfExercises = cursor != null ? cursor.getCount() : 0;
        if (cursor != null) {
            cursor.close();
        }
        return numberOfExercises;
    }
    public int getNumberOfFinishedExercises(long activityId) {

        String[] columns = {COLUMN_EXERCISE_ID_FK};
        String selection = COLUMN_ACTIVITY_ID_FK + " = ? AND " + COLUMN_RELATION_EXERCISE_FINISHED_OR_NOT + " = ?";
        String[] selectionArgs = {String.valueOf(activityId), "1"};

        Cursor cursor = database.query(
                TABLE_ACTIVITY_EXERCISE_RELATION,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int numberOfFinishedExercises = cursor != null ? cursor.getCount() : 0;

        if (cursor != null) {
            cursor.close();
        }
        return numberOfFinishedExercises;
    }
    private boolean areAllExercisesFinishedForActivity(long activityId) {

        String[] columns = {COLUMN_EXERCISE_ID_FK};
        String selection = COLUMN_ACTIVITY_ID_FK + " = ? AND " + COLUMN_RELATION_EXERCISE_FINISHED_OR_NOT + " = ?";
        String[] selectionArgs = {String.valueOf(activityId), "0"};
        Cursor cursor = database.query(
                TABLE_ACTIVITY_EXERCISE_RELATION,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean allExercisesFinished = cursor == null || cursor.getCount() == 0;

        if (cursor != null) {
            cursor.close();
        }
        return allExercisesFinished;
    }

    private void updateActivityProgress(long activityId, int newProgress) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_ACTIVITY_PROGRESS, newProgress);

        String selection = COLUMN_ACTIVITY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(activityId)};

        database.update(TABLE_ACTIVITY_NAME, values, selection, selectionArgs);


    }

    public void setTimeFinishedIn(long activityId, long exercise) {
        ContentValues values = new ContentValues();
        Date finishedOn = new Date();
        Log.d("TAG", "setTimeFinishedIn: " + finishedOn.getDay());

        values.put(COLUMN_FINISHED_TIME, finishedOn.getDay());
        Log.d("testTime", "setTimeFinishedIn: " + new Date().getTime());

        String selection = COLUMN_ACTIVITY_ID_FK + " = ? AND " + COLUMN_EXERCISE_ID_FK + " = ? ";
        String[] selectionArgs = {String.valueOf(activityId), String.valueOf(exercise)};
        int update = database.update(TABLE_ACTIVITY_EXERCISE_RELATION, values, selection, selectionArgs);
        if (update > 0) {
            Log.d("updated", "setTimeFinishedIn: updated");
    } else {
            Log.d("updated", "setTimeFinishedIn: no update");
        }
    }

    public List<ExerciseByDay> retrieveEachDaYFinishedExercised() {
        List<ExerciseByDay>exerciseByDays=new ArrayList<>();
        String query =
                " SELECT " + COLUMN_FINISHED_TIME + " AS day_of_week, COUNT(*) AS exercises_finished_count " +
                        "FROM " + TABLE_ACTIVITY_EXERCISE_RELATION + " " +
                        "WHERE " + COLUMN_RELATION_EXERCISE_FINISHED_OR_NOT + " = 1 " +  // Assuming 1 indicates finished
                        "GROUP BY day_of_week";

        Cursor cursor = database.rawQuery(query, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int dayOfWeek = cursor.getInt(cursor.getColumnIndex("day_of_week"));
            @SuppressLint("Range") int exercisesFinishedCount = cursor.getInt(cursor.getColumnIndex("exercises_finished_count"));
            ExerciseByDay exerciseByDay=new ExerciseByDay(dayOfWeek,exercisesFinishedCount);
            exerciseByDays.add(exerciseByDay);

        }
        return exerciseByDays;
    }




}


