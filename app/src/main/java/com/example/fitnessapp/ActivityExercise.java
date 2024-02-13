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
    private ArrayList<ExerciseDTO>exerciseArrayList=new ArrayList<>();
    private ExerciseServices exerciseServices=new ExerciseServices(this);
    private RelationServices relationServices=new RelationServices(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        Intent receivedIntent = getIntent();
        long data = receivedIntent.getLongExtra("activityId",0);
        RecyclerView recyclerView=findViewById(R.id.exercises);
        relationServices.open();
        List<Long> longList=new ArrayList<>();
        List<Exercise>exerciseList=new ArrayList<>();
        longList=relationServices.getExercisesByActivity(data);
        exerciseServices.open();
        for (long i:longList){
            exerciseList.add(exerciseServices.getActivityExercise(i));
        }
        ExerciseAdapter exerciseAdapter=new ExerciseAdapter(exerciseList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(exerciseAdapter);
        exerciseServices.close();
        relationServices.close();
    }
}