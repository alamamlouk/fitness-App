package com.example.fitnessapp.Entity;

public class ExerciseByDay {
    private int dayOfTheWeek;
    private int numberOfExercise;

    public ExerciseByDay(int dayOfTheWeek, int numberOfExercise) {
        this.dayOfTheWeek = dayOfTheWeek;
        this.numberOfExercise = numberOfExercise;
    }

    public int getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(int dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public int getNumberOfExercise() {
        return numberOfExercise;
    }

    public void setNumberOfExercise(int numberOfExercise) {
        this.numberOfExercise = numberOfExercise;
    }
}
