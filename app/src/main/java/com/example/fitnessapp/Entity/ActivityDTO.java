package com.example.fitnessapp.Entity;

public class ActivityDTO {
    private String activity_name;
    private int time_to_finish;
    private int progress;
    private String path_photo;
    private int time_exercised;

    public ActivityDTO(String activity_name, int time_to_finish, int progress, String path_photo, int time_exercised) {
        this.activity_name = activity_name;
        this.time_to_finish = time_to_finish;
        this.progress = progress;
        this.path_photo = path_photo;
        this.time_exercised = time_exercised;
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

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getPath_photo() {
        return path_photo;
    }

    public void setPath_photo(String path_photo) {
        this.path_photo = path_photo;
    }

    public int getTime_exercised() {
        return time_exercised;
    }

    public void setTime_exercised(int time_exercised) {
        this.time_exercised = time_exercised;
    }
}
