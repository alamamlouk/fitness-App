package com.example.fitnessapp.Entity;

public class WorkOut {
    private int workoutImg, WorkOutTime,numberOfExercises;
    private String WorkoutTile;

    public WorkOut(int workoutImg, int workOutTime, int numberOfExercises, String workoutTile) {
        this.workoutImg = workoutImg;
        WorkOutTime = workOutTime;
        this.numberOfExercises = numberOfExercises;
        WorkoutTile = workoutTile;
    }

    public int getWorkoutImg() {
        return workoutImg;
    }

    public void setWorkoutImg(int workoutImg) {
        this.workoutImg = workoutImg;
    }

    public int getWorkOutTime() {
        return WorkOutTime;
    }

    public void setWorkOutTime(int workOutTime) {
        WorkOutTime = workOutTime;
    }

    public int getNumberOfExercises() {
        return numberOfExercises;
    }

    public void setNumberOfExercises(int numberOfExercises) {
        this.numberOfExercises = numberOfExercises;
    }

    public String getWorkoutTile() {
        return WorkoutTile;
    }

    public void setWorkoutTile(String workoutTile) {
        WorkoutTile = workoutTile;
    }
}
