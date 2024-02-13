package com.example.fitnessapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.Adapaters.ActivityAdapter;
import com.example.fitnessapp.Adapaters.OnRecyclerViewItemClickListener;
import com.example.fitnessapp.Entity.Activity;
import com.example.fitnessapp.Entity.ActivityDTO;
import com.example.fitnessapp.Entity.Activity_exercise;
import com.example.fitnessapp.Entity.ExerciseDTO;
import com.example.fitnessapp.R;
import com.example.fitnessapp.Services.ActivityServices;
import com.example.fitnessapp.Services.ExerciseServices;
import com.example.fitnessapp.Services.RelationServices;
import com.example.fitnessapp.ActivityExercise;

import java.util.ArrayList;
import java.util.List;


public class ActivityFragment extends Fragment implements OnRecyclerViewItemClickListener {
    ActivityServices activityServices;
    ExerciseServices exerciseServices;
    RelationServices relationServices;
    private RecyclerView recyclerView;
    private ActivityAdapter adapter;
    private List<Activity> activityList;


    public ActivityFragment() {
    }

    public static ActivityFragment newInstance(String param1, String param2) {
        ActivityFragment fragment = new ActivityFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityServices = new ActivityServices(getContext());
        exerciseServices = new ExerciseServices(getContext());
        relationServices = new RelationServices(getContext());
        activityServices.open();

        exerciseServices.open();
        relationServices.open();
//        ActivityDTO activity = new ActivityDTO("arms", 20, "In Progress", 20, 20, "");
//        long a1 = activityServices.addActivity(activity);
//        ActivityDTO activity2 = new ActivityDTO("Legs", 30, "Completed", 100, 5, "");
//        ActivityDTO activity3 = new ActivityDTO("Shoulder", 40, "In Progress", 90, 5, "");
//
//        long a2 = activityServices.addActivity(activity2);
//        long a3 = activityServices.addActivity(activity3);
//        ExerciseDTO exerciseDTO = new ExerciseDTO("lift", 10, false);
//        ExerciseDTO exerciseDTO1=new ExerciseDTO("Dumbels",15,false);
//        ExerciseDTO exerciseDTO2=new ExerciseDTO("Squat",15,false);
//        ExerciseDTO exerciseDTO3=new ExerciseDTO("PushUp-incline",20,false);
//        long e1= exerciseServices.addExercise(exerciseDTO);
//        long e2=exerciseServices.addExercise(exerciseDTO1);
//        long e3=exerciseServices.addExercise(exerciseDTO2);
//        long e4=exerciseServices.addExercise(exerciseDTO3);
//        relationServices.addRelation(a1,e1);
//        relationServices.addRelation(a1,e2);
//        relationServices.addRelation(a2,e3);
//        relationServices.addRelation(a3,e4);
        exerciseServices.close();
        relationServices.close();
        activityServices.close();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        relationServices.open();
//        activityServices.open();
//        activityServices.deleteActivity();
//        activityServices.close();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.activityRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        activityList = getActivitiesFromDatabase();
        adapter = new ActivityAdapter(activityList, getContext());
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    private List<Activity> getActivitiesFromDatabase() {
        activityServices.open();
        List<Activity> activities = new ArrayList<>();
        activities = activityServices.getAllActivity();
        activityServices.close();
        return activities;
    }

    @Override
    public void onRecyclerViewItemClicked(int position, Activity item) {

    }

    @Override
    public void onRecyclerViewItemClicked(long id) {
        Intent intent = new Intent(getContext(), ActivityExercise.class);
        Log.d("The parsed id", "onRecyclerViewItemClicked: "+id);
        intent.putExtra("activityId", id);
        startActivity(intent);
    }
}