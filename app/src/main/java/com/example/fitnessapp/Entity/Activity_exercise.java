package com.example.fitnessapp.Entity;

import java.util.Date;

public class Activity_exercise {
    private long activity_exercise_relation_id;
    private long activity_id;
    private long exercise_id;
    private int exerciseProgress;
    private Date finished_on;

    public Activity_exercise(long activity_exercise_relation_id, long activity_id, long exercise_id) {
        this.activity_exercise_relation_id = activity_exercise_relation_id;
        this.activity_id = activity_id;
        this.exercise_id = exercise_id;
    }

    public Date getFinished_on() {
        return finished_on;
    }

    public void setFinished_on(Date finished_on) {
        this.finished_on = finished_on;
    }

    public long getActivity_exercise_relation_id() {
        return activity_exercise_relation_id;
    }

    public void setActivity_exercise_relation_id(long activity_exercise_relation_id) {
        this.activity_exercise_relation_id = activity_exercise_relation_id;
    }

    public long getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(long activity_id) {
        this.activity_id = activity_id;
    }

    public long getExercise_id() {
        return exercise_id;
    }

    public void setExercise_id(long exercise_id) {
        this.exercise_id = exercise_id;
    }

    public int getExerciseProgress() {
        return exerciseProgress;
    }

    public void setExerciseProgress(int exerciseProgress) {
        this.exerciseProgress = exerciseProgress;
    }
}
