package com.example.fitnessapp.Services;

import static com.example.fitnessapp.DataBase.static_field.COLUMN_USER_BMI;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_USER_HEIGHT;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_USER_ID;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_USER_NAME;
import static com.example.fitnessapp.DataBase.static_field.COLUMN_USER_WEIGHT;
import static com.example.fitnessapp.DataBase.static_field.TABLE_USER_NAME;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fitnessapp.DataBase.DBHandler;
import com.example.fitnessapp.Entity.User;

public class UserServices {

    private SQLiteDatabase database;

    private final DBHandler dbHandler;

    public UserServices(Context context) {
        this.dbHandler = new DBHandler(context);
    }

    //Add  a user
    public void addUser(User user){
        ContentValues values=new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUserName());
        values.put(COLUMN_USER_WEIGHT,user.getUserWeight());
        values.put(COLUMN_USER_HEIGHT,user.getUserHeight());
        values.put(COLUMN_USER_BMI, user.getUserBMI());
        database.insert(TABLE_USER_NAME,null,values);
    }

    // delete user
    public void deleteUser() {
        open();
        database.delete(TABLE_USER_NAME, null, null);
        close();
    }

    //return the user Details
    @SuppressLint("Range")
    public User getUserDetails() {
        User user = null;
        String[] columns = {
               COLUMN_USER_ID,
               COLUMN_USER_NAME,
               COLUMN_USER_WEIGHT,
               COLUMN_USER_HEIGHT,
               COLUMN_USER_BMI
        };

        Cursor cursor = database.query(
                TABLE_USER_NAME,
                columns,
               null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setUserId(cursor.getLong(cursor.getColumnIndex(COLUMN_USER_ID)));
            user.setUserName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
            user.setUserWeight(cursor.getDouble(cursor.getColumnIndex(COLUMN_USER_WEIGHT)));
            user.setUserHeight(cursor.getDouble(cursor.getColumnIndex(COLUMN_USER_HEIGHT)));
            user.setUserBMI(cursor.getDouble(cursor.getColumnIndex(COLUMN_USER_BMI)));
            cursor.close();
        }

        return user;
    }
    public void open() {
        database = dbHandler.getWritableDatabase();
    }

    public void close() {
        dbHandler.close();
    }

    //Update the user details
    public boolean updateUserDetails(User user) {
        boolean success;

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUserName());
        values.put(COLUMN_USER_WEIGHT, user.getUserWeight());
        values.put(COLUMN_USER_HEIGHT, user.getUserHeight());
        values.put(COLUMN_USER_BMI, user.getUserBMI());
        //where user Id hethi
        String selection = COLUMN_USER_ID + " = ?";
        //ou hethi bich it3awidh kil ?
        String[] selectionArgs = {String.valueOf(user.getUserId())};

        int rowsUpdated = database.update(
                TABLE_USER_NAME,
                values,
                selection,
                selectionArgs
        );

        success = rowsUpdated > 0;

        return success;
    }
}
