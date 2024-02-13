package com.example.fitnessapp.Entity;

public class ActivityDTO {
    private String activity_name;
    private int time_to_finish;
    private String state;
    private int progress;
    private int number_of_exercise;
    private String path_photo;

    public ActivityDTO(String activity_name, int time_to_finish, String state, int progress, int number_of_exercise, String path_photo) {
        this.activity_name = activity_name;
        this.time_to_finish = time_to_finish;
        this.state = state;
        this.progress = progress;
        this.number_of_exercise = number_of_exercise;
        this.path_photo = path_photo;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public int getTime_to_finish() {
        return time_to_finish;
    }

    public void setTime_to_finish(int time_to_finish) {
        this.time_to_finish = time_to_finish;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getNumber_of_exercise() {
        return number_of_exercise;
    }

    public void setNumber_of_exercise(int number_of_exercise) {
        this.number_of_exercise = number_of_exercise;
    }

    public String getPath_photo() {
        return path_photo;
    }

    public void setPath_photo(String path_photo) {
        this.path_photo = path_photo;
    }
}
