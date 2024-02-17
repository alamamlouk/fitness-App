package com.example.fitnessapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fitnessapp.Entity.ActivityDTO;
import com.example.fitnessapp.Entity.ExerciseDTO;
import com.example.fitnessapp.Entity.User;
import com.example.fitnessapp.Fragments.ActivityFragment;
import com.example.fitnessapp.Fragments.DashboardFragment;
import com.example.fitnessapp.Fragments.HomeFragment;
import com.example.fitnessapp.Fragments.SettingFragment;
import com.example.fitnessapp.Services.ActivityServices;
import com.example.fitnessapp.Services.ExerciseServices;
import com.example.fitnessapp.Services.RelationServices;
import com.example.fitnessapp.Services.UserServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    ActivityServices activityServices;
    ExerciseServices exerciseServices;
    RelationServices relationServices;
    UserServices userServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityServices = new ActivityServices(this);
        exerciseServices = new ExerciseServices(this);
        relationServices = new RelationServices(this);
        userServices=new UserServices(this);
        LoadTable loadTable=new LoadTable(this);
//        activityServices.deleteActivity();
//        exerciseServices.deleteExercises();
//        userServices.deleteUser();
//        relationServices.deleteRelation();
//          loadTable.loadUser();
//        loadTable.loadTheRest();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationBar);
        FrameLayout frameLayout = findViewById(R.id.frameLayout);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.navHome) {

                    loadFragment(new HomeFragment(), false);
                } else if (itemId == R.id.navActivity) {
                    loadFragment(new ActivityFragment(),false);

                } else if (itemId == R.id.navDashboard) {
                    loadFragment(new DashboardFragment(),false);
                } else if (itemId == R.id.navProfile) {
                    loadFragment(new SettingFragment(),false);
                }

                return true;
            }
        });
        loadFragment(new HomeFragment(),true);
    }
    private void loadFragment(Fragment fragment, boolean isAppInitialized){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        if(isAppInitialized){
            fragmentTransaction.add(R.id.frameLayout,fragment);
        }
        else {
            fragmentTransaction.replace(R.id.frameLayout,fragment);
        }
        fragmentTransaction.commit();

    }
}
