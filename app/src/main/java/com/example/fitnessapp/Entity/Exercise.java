package com.example.fitnessapp.Entity;

public class Exercise {
    private long id;
    private String exercise_name;
    private int time_to_finish;
    private boolean state;
    private String exercise_description;
    private String exercise_path_image;

    public Exercise(long id, String exercise_name, int time_to_finish, boolean state, String exercise_description, String exercise_path_image) {
        this.id = id;
        this.exercise_name = exercise_name;
        this.time_to_finish = time_to_finish;
        this.state = state;
        this.exercise_description = exercise_description;
        this.exercise_path_image = exercise_path_image;
    }

    public Exercise() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getExercise_name() {
        return exercise_name;
    }

    public void setExercise_name(String exercise_name) {
        this.exercise_name = exercise_name;
    }

    public int getTime_to_finish() {
        return time_to_finish;
    }

    public void setTime_to_finish(int time_to_finish) {
        this.time_to_finish = time_to_finish;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getExercise_description() {
        return exercise_description;
    }

    public void setExercise_description(String exercise_description) {
        this.exercise_description = exercise_description;
    }

    public String getExercise_path_image() {
        return exercise_path_image;
    }

    public void setExercise_path_image(String exercise_path_image) {
        this.exercise_path_image = exercise_path_image;
    }
}
