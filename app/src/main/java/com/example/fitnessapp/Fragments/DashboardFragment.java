package com.example.fitnessapp.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.fitnessapp.Entity.ActivityAndTimeExercised;
import com.example.fitnessapp.R;
import com.example.fitnessapp.Services.ActivityServices;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements LocationListener, SensorEventListener {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    private LocationManager locationManager;
    private TextView speedTextView;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private TextView stepCountTextView;
    private int stepCount = 0;

    private static final float STEP_THRESHOLD = 12.0f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        int completedExercises = 25;
        int totalExercises = 50;
        float percentage = (completedExercises * 100f) / totalExercises;
        speedTextView = view.findViewById(R.id.SpeedKM);
        stepCountTextView=view.findViewById(R.id.steps);
        BarChart barChart  = view.findViewById(R.id.chart);
        ActivityServices activityServices=new ActivityServices(getContext());
        activityServices.open();
        List<ActivityAndTimeExercised> activitiesWithTime = activityServices.getAllActivitiesWithTimeExercised();
        List<BarEntry> entries = new ArrayList<>();
        int[] colors = new int[]{Color.GREEN, Color.BLUE, Color.RED, Color.YELLOW, Color.MAGENTA, Color.CYAN};
        for (int i = 0; i < activitiesWithTime.size(); i++) {
            ActivityAndTimeExercised activityWithTime = activitiesWithTime.get(i);
            entries.add(new BarEntry(i + 1, activityWithTime.getTime_exercised()));
        }
        BarDataSet dataSet = new BarDataSet(entries, "Time Exercised");
        dataSet.setColors(colors);
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(getActivityNames(activitiesWithTime)));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularity(1f);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBorders(true);
        barChart.animateY(1000);
        barChart.invalidate();
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
        if (sensorManager != null) {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            if (accelerometerSensor != null) {
                sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Log.e("AccelerometerSensor", "Accelerometer sensor not available");
            }
        } else {
            Log.e("SensorManager", "SensorManager not available");
        }

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
            } else {
                // Permission denied, handle accordingly
                Log.e("GPS", "Location permission denied");
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);

        locationManager.removeUpdates(this);
    }
    @Override
    public void onLocationChanged(Location location) {

        float speed = location.getSpeed();

        float speedKmH = (speed * 3600) / 1000;

        updateSpeedUI(speedKmH);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    private void updateSpeedUI(float speed) {
        if (speedTextView != null) {
            speedTextView.setText("Speed: " +(int)speed + " km/h");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            detectSteps(event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    private void updateStepCountUI() {
        if (stepCountTextView != null) {
            stepCountTextView.setText("Step Count: " + stepCount);
        }
    }
    private void detectSteps(float x, float y, float z) {
        // Calculate the magnitude of acceleration
        float magnitude = (float) Math.sqrt(x * x + y * y + z * z);
        Log.d("magnitude", "detectSteps: "+magnitude);
        if (magnitude > STEP_THRESHOLD) {
            stepCount++;
            updateStepCountUI();
        }
    }
    private String[] getActivityNames(List<ActivityAndTimeExercised> activitiesWithTime) {
        String[] activityNames = new String[activitiesWithTime.size() + 1];
        activityNames[0] = "";

        for (int i = 0; i < activitiesWithTime.size(); i++) {
            activityNames[i + 1] = activitiesWithTime.get(i).getActivity_name();
        }

        return activityNames;
    }
}
