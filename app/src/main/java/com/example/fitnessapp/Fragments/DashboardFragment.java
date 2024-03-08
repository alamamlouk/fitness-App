package com.example.fitnessapp.Fragments;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.fitnessapp.R;
import com.example.fitnessapp.Services.ActivityServices;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements LocationListener, SensorEventListener, OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    private LocationManager locationManager;
    private TextView speedTextView;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private TextView stepCountTextView;
    private int stepCount = 0;
    private static final int DEFAULT_ZOOM = 15;
    private ImageButton setMyLocation;

    private static final float STEP_THRESHOLD = 12.0f;
    private GoogleMap googleMap;
    private final int FINE_PERMISSION_CODE = 1;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private Marker myMarker;
    private List<Marker> markerList=new ArrayList<>();
    private List<Polyline>polylineList=new ArrayList<>();
    private TextView distance;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        speedTextView = view.findViewById(R.id.SpeedKM);
        stepCountTextView=view.findViewById(R.id.steps);
        setMyLocation=view.findViewById(R.id.currentLoc);
        distance=view.findViewById(R.id.distance);
        ActivityServices activityServices=new ActivityServices(getContext());
        activityServices.open();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();



        getLastLocation();



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

        setMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToMyLocation();
            }
        });


        return view;
    }

    private void setToMyLocation() {

        for (Marker marker:markerList){
            if (!marker.equals(myMarker)){
                marker.remove();
            }
        }
        for (Polyline polyline:polylineList){
            polyline.remove();
        }
        LatLng place = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place,16F));
    }
    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                    if (supportMapFragment == null) {
                        supportMapFragment = SupportMapFragment.newInstance();
                        getChildFragmentManager().beginTransaction().replace(R.id.map, supportMapFragment).commit();
                    }
                    supportMapFragment.getMapAsync(DashboardFragment.this);
                }
            }
        });
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
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Log.e("MAp", "Location permission denied");

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

    @SuppressLint("SetTextI18n")
    private void updateSpeedUI(float speed) {
        if (speedTextView != null) {
            speedTextView.setText((int) speed + " km/h");
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
    @SuppressLint("SetTextI18n")
    private void updateStepCountUI() {
        if (stepCountTextView != null) {
            stepCountTextView.setText(String.valueOf(stepCount));
        }
    }
    private void detectSteps(float x, float y, float z) {
        float magnitude = (float) Math.sqrt(x * x + y * y + z * z);
        if (magnitude > STEP_THRESHOLD) {
            stepCount++;
            updateStepCountUI();
        }
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng place = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        this.myMarker=this.googleMap.addMarker(new MarkerOptions().position(place).title("me").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place,DEFAULT_ZOOM));
        markerList.add(this.myMarker);

        this.googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {

                for (Marker marker:markerList){
                    if (!marker.equals(myMarker)){
                        marker.remove();
                    }
                }
                for (Polyline polyline:polylineList){
                    polyline.remove();
                }
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("New Marker");
                Marker newMarker=googleMap.addMarker(markerOptions);
                markerList.add(newMarker);
                PolylineOptions polylineOptions = new PolylineOptions()
                        .add(new LatLng(myMarker.getPosition().latitude, myMarker.getPosition().longitude))
                        .add(new LatLng(newMarker.getPosition().latitude, newMarker.getPosition().longitude))
                        .color(Color.BLUE)
                        .width(10);
                Polyline myPolyline;
                myPolyline=googleMap.addPolyline(polylineOptions);
                polylineList.add(myPolyline);
                distance.setText("Distance ≃ "+calculateDistance(myMarker,newMarker));
            }
        });
    }
    private String calculateDistance(Marker marker1, Marker marker2) {
        Location location1 = new Location("marker1");
        location1.setLatitude(marker1.getPosition().latitude);
        location1.setLongitude(marker1.getPosition().longitude);

        Location location2 = new Location("marker2");
        location2.setLatitude(marker2.getPosition().latitude);
        location2.setLongitude(marker2.getPosition().longitude);

        float distance = location1.distanceTo(location2);
        return String.format("%.2f meters", distance);
    }

}

