package com.example.fitnessapp.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
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

import com.example.fitnessapp.R;

public class DashboardFragment extends Fragment implements LocationListener, SensorEventListener {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    private LocationManager locationManager;
    private TextView speedTextView;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private TextView stepCountTextView;
    private int stepCount = 0;

    private static final float STEP_THRESHOLD = 10.0f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Initialize UI components
        speedTextView = view.findViewById(R.id.SpeedKM);
        stepCountTextView=view.findViewById(R.id.steps);

        // Initialize LocationManager
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        // Request location permissions at runtime
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted, request location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            // Register the accelerometer sensor listener
            if (accelerometerSensor != null) {
                sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Log.e("AccelerometerSensor", "Accelerometer sensor not available");
                // Handle the case where the accelerometer sensor is not available
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
                // Permission granted, request location updates
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
        // No need to register the sensor listener here, it's already done after receiving permissions
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);

        // Stop receiving location updates to save battery
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        // This method will be called whenever the location changes

        // Calculate speed in m/s
        float speed = location.getSpeed();

        // Convert speed to km/h
        float speedKmH = (speed * 3600) / 1000;

        // Update the UI with the current speed
        updateSpeedUI(speedKmH);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Handle changes in GPS provider status if needed
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Handle GPS provider enabled if needed
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Handle GPS provider disabled if needed
    }

    private void updateSpeedUI(float speed) {
        // Update the UI with the current speed
        if (speedTextView != null) {
            speedTextView.setText("Speed: " +(int)speed + " km/h");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Detect steps based on changes in acceleration values
            detectSteps(event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    private void updateStepCountUI() {
        // Update the UI with the current step count
        if (stepCountTextView != null) {
            stepCountTextView.setText("Step Count: " + stepCount);
        }
    }
    private void detectSteps(float x, float y, float z) {
        // Calculate the magnitude of acceleration
        float magnitude = (float) Math.sqrt(x * x + y * y + z * z);

        // Check if the magnitude crosses the threshold
        if (magnitude > STEP_THRESHOLD) {
            stepCount++;
            updateStepCountUI();
        }
    }
}
