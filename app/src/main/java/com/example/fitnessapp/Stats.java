package com.example.fitnessapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.fitnessapp.Services.ActivityServices;

public class Stats extends AppWidgetProvider {
    ActivityServices activityServices;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            int finishedWorkOutCount = getFinishedWorkOutCount(context);
            long inProgressCount = getInProgressCount(context);
            int timeSpentMinutes = getTimeSpentMinutes(context);
            remoteViews.setTextViewText(R.id.finishedWorkOutCountTextView, String.valueOf(finishedWorkOutCount));
            remoteViews.setTextViewText(R.id.inProgressCountTextView, String.valueOf(inProgressCount));
            remoteViews.setTextViewText(R.id.timeSpentMinutesTextView, String.valueOf(timeSpentMinutes));
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            remoteViews.setOnClickPendingIntent(R.id.mainActivity, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        }
    }


    private int getFinishedWorkOutCount(Context context) {
        activityServices=new ActivityServices(context);
        activityServices.open();
        int number=activityServices.getCompletedActivitiesCount();
        activityServices.close();
        return number;
    }

    private long getInProgressCount(Context context) {
        activityServices=new ActivityServices(context);
        activityServices.open();
        int number=activityServices.getActivitiesWithTimeExercisedGreaterThanZeroCount();
        activityServices.close();
        return number;
    }

    private int getTimeSpentMinutes(Context context) {
        activityServices=new ActivityServices(context);
        activityServices.open();
        int number=activityServices.getSumOfTimeExercised();
        activityServices.close();
        return number;
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}
