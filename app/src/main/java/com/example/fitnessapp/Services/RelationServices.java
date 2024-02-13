package com.example.fitnessapp.Services;

import static com.example.fitnessapp.DataBase.static_field.COLUMN_ACTIVITY_ID_FK;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_ID;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_EXERCISE_ID_FK;
import static com.example.fitnessapp.DataBase.static_field.TABLE_ACTIVITY_EXERCISE_RELATION;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fitnessapp.DataBase.DBHandler;
import com.example.fitnessapp.Entity.Activity_exercise;
import com.github.mikephil.charting.renderer.scatter.ChevronUpShapeRenderer;

import java.util.ArrayList;
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
    public long addRelation(long activity_id,long exercise_id)
    {
        ContentValues values=new ContentValues();
        values.put(COLUMN_ACTIVITY_ID_FK,activity_id);
        values.put(COLUMN_EXERCISE_ID_FK,exercise_id);
        return database.insert(TABLE_ACTIVITY_EXERCISE_RELATION,null,values);
    }
    @SuppressLint("Range")
    public List<Activity_exercise>exerciseList(){
        List<Activity_exercise>activity_exercises=new ArrayList<>();
        String[] columns={
          COLUMN_ACTIVITY_ID_FK,
          COLUMN_EXERCISE_ID_FK
        };
        Cursor cursor=database.query(
                TABLE_ACTIVITY_EXERCISE_RELATION,
                columns,
                null,
                null,
                null,
                null,
                null
        );
        if(cursor !=null && cursor.moveToFirst()){
            do {
                Activity_exercise activity_exercise=new Activity_exercise();
                activity_exercise.setActivity_id(cursor.getLong(cursor.getColumnIndex(COLUMN_ACTIVITY_ID_FK)));
                activity_exercise.setExercise_id(cursor.getLong(cursor.getColumnIndex(COLUMN_EXERCISE_ID_FK)));
                activity_exercises.add(activity_exercise);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return activity_exercises;
    }
    @SuppressLint("Range")
    public List<Long> getExercisesByActivity(long activity_id){
        List<Long> exercise_Ids=new ArrayList<>();
        String[]columns={
                COLUMN_EXERCISE_ID_FK
        };
        String selection = COLUMN_ACTIVITY_ID_FK + " = ?";
        String[] selectionArgs = {String.valueOf(activity_id)};

        @SuppressLint("Recycle") Cursor cursor=database.query(
                TABLE_ACTIVITY_EXERCISE_RELATION,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if(cursor !=null && cursor.moveToFirst()){
            do{
                exercise_Ids.add(cursor.getLong(cursor.getColumnIndex(COLUMN_EXERCISE_ID_FK)));
            }while (cursor.moveToNext());
        }
        return exercise_Ids;
    }

}
