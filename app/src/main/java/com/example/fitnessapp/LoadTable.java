package com.example.fitnessapp;

import android.content.Context;

import com.example.fitnessapp.Entity.ActivityDTO;
import com.example.fitnessapp.Entity.ExerciseDTO;
import com.example.fitnessapp.Entity.User;
import com.example.fitnessapp.Services.ActivityServices;
import com.example.fitnessapp.Services.ExerciseServices;
import com.example.fitnessapp.Services.RelationServices;
import com.example.fitnessapp.Services.UserServices;

public class LoadTable {
    ActivityServices activityServices;
    ExerciseServices exerciseServices;
    RelationServices relationServices;
    UserServices userServices;

    public LoadTable(Context context) {
        activityServices=new ActivityServices(context);
        exerciseServices=new ExerciseServices(context);
        relationServices=new RelationServices(context);
        userServices=new UserServices(context);
    }
    public void loadUser(){
        userServices.open();
        User user=new User();
        user.setUserName("Ala");
        user.setUserBMI(0);
        user.setUserHeight(0);
        user.setUserWeight(0);
        userServices.addUser(user);
        userServices.close();
    }
    public void loadTheRest(){
        activityServices.open();
        exerciseServices.open();
        relationServices.open();
        ActivityDTO armsBeginner = new ActivityDTO("Arms Beginner", 17, 0, "arm_day", 0);

        ExerciseDTO exerciseArmRaise = new ExerciseDTO(
                "Arm raise 1 MN",
                1,
                0,
                "Stand  with your arm extended straight forward ar shoulder height " +
                        "raise your arms above your head.return to the start position .",
                "armraise");
        ExerciseDTO pushUps = new ExerciseDTO("Push up X 10",
                1,
                1,
                "Lay prone on the ground with arms supporting your body and puch up and down for 10 Time",
                "pushup");
        ExerciseDTO inclinePushUp = new ExerciseDTO(
                "Incline Push ups X 10",
                1,
                1,
                "Same thing as Push ups but put ur hands on something elevated",
                "incline"
        );
        ActivityDTO legsBeginners = new ActivityDTO("Leg Beginners",
                17,
                0,
                "legs_day",
                0);
        ExerciseDTO squats = new ExerciseDTO(
                "Squats X 12",
                1,
                1,
                "Stand with your feet shoulder width then up and down",
                "squat"
        );
        ExerciseDTO jumping_jack = new ExerciseDTO(
                "Jumping Jack",
                1,
                0,
                "jumping jack 3adiya",
                "jumping"
        );
        ExerciseDTO lunges = new ExerciseDTO(
                "Lunges",
                2,
                1,
                "Lean forward",
                "lunges"
        );
        ActivityDTO fullBody = new ActivityDTO(
                "Full body training",
                20,
                0,
                "full",
                0
        );
        ExerciseDTO plunk = new ExerciseDTO(
                "Plunk",
                2,
                0,
                "plunk 3adiya ",
                "plunk"
        );
        long a1 = activityServices.addActivity(armsBeginner);
        long e1 = exerciseServices.addExercise(exerciseArmRaise);
        long e2 = exerciseServices.addExercise(pushUps);
        long e3 = exerciseServices.addExercise(inclinePushUp);
        long a2 = activityServices.addActivity(legsBeginners);
        long a3=activityServices.addActivity(fullBody);
        long e4=exerciseServices.addExercise(plunk);
        long e5=exerciseServices.addExercise(lunges);
        long e6=exerciseServices.addExercise(squats);
        long e7=exerciseServices.addExercise(jumping_jack);
        relationServices.addRelation(a1,e7);
        relationServices.addRelation(a1, e1);
        relationServices.addRelation(a1, e2);
        relationServices.addRelation(a1, e3);
        relationServices.addRelation(a2,e7);
        relationServices.addRelation(a2,e4);
        relationServices.addRelation(a2,e6);
        relationServices.addRelation(a2,e5);
        relationServices.addRelation(a3,e7);
        relationServices.addRelation(a3,e1);
        relationServices.addRelation(a3,e2);
        relationServices.addRelation(a3,e3);
        relationServices.addRelation(a3,e4);
        relationServices.addRelation(a3,e5);
        relationServices.addRelation(a3,e6);

    }
}
