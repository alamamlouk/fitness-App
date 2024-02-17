package com.example.fitnessapp.Entity;

public class User {
    private long userId;
    private String userName;
    private double userWeight;
    private double userHeight;
    private double userBMI;

    public User(long userId, String userName, double userWeight, double userHeight, double userBMI) {
        this.userId = userId;
        this.userName = userName;
        this.userWeight = userWeight;
        this.userHeight = userHeight;
        this.userBMI = userBMI;
    }

    public User() {

    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(double userWeight) {
        this.userWeight = userWeight;
    }

    public double getUserHeight() {
        return userHeight;
    }

    public void setUserHeight(double userHeight) {
        this.userHeight = userHeight;
    }

    public double getUserBMI() {
        return userBMI;
    }

    public void setUserBMI(double userBMI) {
        this.userBMI = userBMI;
    }
}
