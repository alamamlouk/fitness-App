package com.example.fitnessapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
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

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    private int getFinishedWorkOutCount(Context context) {
        activityServices=new ActivityServices(context);
        activityServices.open();
        int number= activityServices.getNumberOfActivityFinished();
        activityServices.close();
        return number;
         // Example value
    }

    private long getInProgressCount(Context context) {
        activityServices=new ActivityServices(context);
        activityServices.open();
        long number= activityServices.getNumberOfExercisesInProgress();
        activityServices.close();
        return number; // Example value
    }

    private int getTimeSpentMinutes(Context context) {
        activityServices=new ActivityServices(context);
        activityServices.open();
        int number= activityServices.getTimeSpent();
        activityServices.close();
        return number; // Example value
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
