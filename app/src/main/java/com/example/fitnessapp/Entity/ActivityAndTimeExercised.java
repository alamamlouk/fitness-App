package com.example.fitnessapp.Entity;

public class ActivityAndTimeExercised {
    private String activity_name;
    private int time_exercised;

    public ActivityAndTimeExercised(String activity_name, int time_exercised) {
        this.activity_name = activity_name;
        this.time_exercised = time_exercised;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public int getTime_exercised() {
        return time_exercised;
    }

    public void setTime_exercised(int time_exercised) {
        this.time_exercised = time_exercised;
    }
}
