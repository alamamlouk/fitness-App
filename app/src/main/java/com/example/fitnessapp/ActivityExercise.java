package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.fitnessapp.Adapaters.ExerciseAdapter;
import com.example.fitnessapp.Entity.Exercise;
import com.example.fitnessapp.Entity.ExerciseDTO;
import com.example.fitnessapp.Services.ActivityServices;
import com.example.fitnessapp.Services.ExerciseServices;
import com.example.fitnessapp.Services.RelationServices;

import java.util.ArrayList;
import java.util.List;

public class ActivityExercise extends AppCompatActivity {
    private final RelationServices relationServices=new RelationServices(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        Intent receivedIntent = getIntent();
        long data = receivedIntent.getLongExtra("activityId",0);
        RecyclerView recyclerView=findViewById(R.id.exercises);
        relationServices.open();
        List<Exercise>exerciseList=new ArrayList<>();
        exerciseList=relationServices.getExercisesByActivity(data);
        ExerciseAdapter exerciseAdapter=new ExerciseAdapter(exerciseList,this,data);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(exerciseAdapter);
        relationServices.close();
    }
}