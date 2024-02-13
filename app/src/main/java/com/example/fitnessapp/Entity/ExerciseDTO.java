package com.example.fitnessapp.Entity;

public class ExerciseDTO {
    private String exercise_name;
    private int time_to_finish;
    private boolean state;

    public ExerciseDTO(String exercise_name, int time_to_finish, boolean state) {
        this.exercise_name = exercise_name;
        this.time_to_finish = time_to_finish;
        this.state = state;
    }

    public ExerciseDTO() {

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

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
