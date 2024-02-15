package com.example.fitnessapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fitnessapp.Entity.WorkOut;
import com.example.fitnessapp.R;
import com.example.fitnessapp.Adapaters.WorkOutAdapter;
import com.example.fitnessapp.Services.ActivityServices;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private ArrayList<WorkOut>workOuts=new ArrayList<>();

    ActivityServices activityServices;
    TextView finishedWorkout,inProgressWorkout,timeSpent;

    public HomeFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityServices=new ActivityServices(getContext());
        activityServices.open();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_home, container, false);
        finishedWorkout=view.findViewById(R.id.finishedWorkOutHome);
        inProgressWorkout=view.findViewById(R.id.inProgressHome);
        timeSpent=view.findViewById(R.id.TimeSpentHome);
        finishedWorkout.setText(String.valueOf(activityServices.getCompletedActivitiesCount()));
        inProgressWorkout.setText(String.valueOf(activityServices.getActivitiesWithTimeExercisedGreaterThanZeroCount()));
        timeSpent.setText(String.valueOf(activityServices.getSumOfTimeExercised()));
        activityServices.close();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WorkOut workOut=new WorkOut(R.drawable.workout1,30,5,"ABS");
        WorkOut workOut2=new WorkOut(R.drawable.workout1,30,5,"ABS");
        this.workOuts.add(workOut);
        this.workOuts.add(workOut2);
        RecyclerView recyclerView=view.findViewById(R.id.workOuts);
        WorkOutAdapter workOutAdapter=new WorkOutAdapter(workOuts,this.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(workOutAdapter);
    }
}