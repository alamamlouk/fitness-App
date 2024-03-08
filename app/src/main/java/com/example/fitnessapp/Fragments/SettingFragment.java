package com.example.fitnessapp.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.fitnessapp.Adapaters.SelectLanguageAdapter;
import com.example.fitnessapp.Entity.ExerciseByDay;
import com.example.fitnessapp.Entity.User;
import com.example.fitnessapp.R;
import com.example.fitnessapp.Services.RelationServices;
import com.example.fitnessapp.Services.UserServices;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SettingFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private TextInputEditText editTextUserName;
    private TextInputEditText editWeight;
    private TextInputEditText editHeight;

    private boolean isEditing = false;
    private ImageView imageView;
    private boolean userSelected = false;
    private static final String PREFS_NAME = "LanguageFile";
    private static final String SELECTED_LANGUAGE_POSITION = "SelectedLanguagePosition";
    private TextView bmiCategory;
    Spinner spinner;
    private TextView bmi;
    private UserServices userService;
    private final String[] countryName = {"French","Spanish","English","Italian"};
    private final int[] flags = {R.drawable.flagfrance,R.drawable.flagspain,R.drawable.english,R.drawable.itali};

    public SettingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = new UserServices(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        editTextUserName = view.findViewById(R.id.editTextUserName);
        editHeight = view.findViewById(R.id.editTextHeight);
        editWeight = view.findViewById(R.id.editTextWeight);
        imageView = view.findViewById(R.id.editIcon);
        bmi = view.findViewById(R.id.BMI);
        spinner = view.findViewById(R.id.languageSpinner);
        bmiCategory=view.findViewById(R.id.bmi_category);
        SelectLanguageAdapter selectLanguageAdapter = new SelectLanguageAdapter(getContext(), flags, countryName);
        spinner.setAdapter(selectLanguageAdapter);
        spinner.setOnItemSelectedListener(this);
        BarChart barChart  = view.findViewById(R.id.chart);
        RelationServices relationServices = new RelationServices(getContext());
        relationServices.open();
        List<ExerciseByDay> exerciseByDayList=relationServices.retrieveEachDaYFinishedExercised();

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 0));
        entries.add(new BarEntry(1, 0));
        entries.add(new BarEntry(2, 0));
        entries.add(new BarEntry(3, 0));
        entries.add(new BarEntry(4, 0));
        entries.add(new BarEntry(5, 0));
        entries.add(new BarEntry(6, 0));
        for (int i = 0; i < exerciseByDayList.size(); i++) {
            int dayOfWeek = exerciseByDayList.get(i).getDayOfTheWeek();
            if (dayOfWeek >= 0 && dayOfWeek < entries.size()) {
                entries.get(dayOfWeek).setY(exerciseByDayList.get(i).getNumberOfExercise());
            }
        }

        BarDataSet dataSet = new BarDataSet(entries, "Daily Activities");
        dataSet.setColor(Color.parseColor("#2FFEF6"));
        float barWidth = 0.3f;
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(barWidth);
        barChart.setData(barData);
        barChart.setFitBars(false);
        barChart.getDescription().setEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        YAxis leftYAxis = barChart.getAxisLeft();
        YAxis rightYAxis = barChart.getAxisRight();
        leftYAxis.setDrawGridLines(false);
        rightYAxis.setDrawGridLines(false);
        rightYAxis.setEnabled(false);
        leftYAxis.setAxisMinimum(0);
        leftYAxis.setAxisMaximum(20);
        rightYAxis.setEnabled(false);
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new SettingFragment.DayAxisValueFormatter());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEditing();
                if(!isEditing){
                    updateUserInfo();
                }

            }
        });


        return view;
    }

    private void enableEditing() {
        isEditing = !isEditing;

        editTextUserName.setEnabled(isEditing);
        editWeight.setEnabled(isEditing);
        editHeight.setEnabled(isEditing);
        int iconResourceId = isEditing ? R.drawable.save : R.drawable.edit;
        imageView.setImageResource(iconResourceId);
    }

    @SuppressLint("SetTextI18n")
    private void displayUserDetails() {
        userService.open();
        User user = userService.getUserDetails();

        if (user != null) {
            editTextUserName.setText(user.getUserName());
            editHeight.setText(String.valueOf(user.getUserHeight()));
            editWeight.setText(String.valueOf(user.getUserWeight()));
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String formattedBMI = decimalFormat.format(user.getUserBMI());
            bmi.setText("BMI: " + formattedBMI);
            setBmiCategory(user.getUserBMI());        }
        userService.close();
    }

    @SuppressLint("SetTextI18n")
    private void updateUserInfo() {
        if (Objects.requireNonNull(editWeight.getText()).toString().isEmpty() || Objects.requireNonNull(editHeight.getText()).toString().isEmpty()) {
            Toast.makeText(requireContext(), "Please enter all details", Toast.LENGTH_SHORT).show();
            return;
        }
        userService.open();
        User user = userService.getUserDetails();
        user.setUserName(Objects.requireNonNull(editTextUserName.getText()).toString());
        user.setUserHeight(Double.parseDouble(editHeight.getText().toString()));
        user.setUserWeight(Double.parseDouble(editWeight.getText().toString()));
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double newBMI = Double.parseDouble(editWeight.getText().toString()) / (Math.pow(Double.parseDouble(editHeight.getText().toString()), 2));
        String formattedBMI = decimalFormat.format(newBMI);
        user.setUserBMI(newBMI);
        boolean success = userService.updateUserDetails(user);

        if (success) {
            bmi.setText("BMI: " + formattedBMI);
            setBmiCategory(newBMI);


        } else {
            Log.d("Error", "updateUserInfo:Error ");
        }
        userService.close();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int selectedLanguagePosition = prefs.getInt(SELECTED_LANGUAGE_POSITION, 0);
        spinner.setSelection(selectedLanguagePosition);

        displayUserDetails();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(userSelected){
            changeLanguage(getLanguageCode(position));
            SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(SELECTED_LANGUAGE_POSITION, position);
            editor.putString("SelectedLanguage", getLanguageCode(position));
            editor.apply();
        }
        else{
            userSelected=true;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @SuppressLint("SetTextI18n")
    public void changeLanguage(String language) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_language_change, null);
        TextView textView = dialogView.findViewById(R.id.languageName);
        textView.setText("Changing language to " + getLanguage(language));
        ImageView gifImage = dialogView.findViewById(R.id.languageImg);
        int image = getGif(language);
        Glide.with(dialogView).load(image).into(gifImage);
        ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();
        final int totalProgress = 100;
        final int interval = 50;
        HandlerThread handlerThread = new HandlerThread("ProgressThread");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            int progress = 0;

            @Override
            public void run() {
                if (progress <= totalProgress) {
                    progressBar.setProgress(progress);
                    progress++;
                    handler.postDelayed(this, interval);
                } else {
                    Locale locale = new Locale(language);
                    Resources res = getResources();

                    Configuration conf = res.getConfiguration();
                    conf.setLocale(locale);
                    Configuration updatedConf = new Configuration(conf);
                    updatedConf.setLocale(locale);
                    Intent intent = requireActivity().getIntent();
                    dialog.dismiss();
                    requireActivity().finish();
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("new_locale", locale.toString()));
                }
            }
        });
    }
    private String getLanguageCode(int position) {
        switch (position) {
            case 0:
                return "fr";
            case 1:
                return "es";
            case 2:
                return "en";
            case 3:
                return "it";
            default:
                return "";
        }
    }

    private int getGif(String position) {
        switch (position) {
            case "fr":
                return R.drawable.croissant;
            case "es":
                return R.drawable.spain;
            case "en":
                return R.drawable.knight;
            case "it":
                return R.drawable.rome;
            default:
                return 0;
        }
    }

    private String getLanguage(String language) {
        switch (language) {
            case "fr":
                return "French";
            case "es":
                return "Spanish";
            case "en":
                return "English";
            case "it":
                return "Italian";
            default:
                return "";
        }
    }
    private static class DayAxisValueFormatter extends ValueFormatter {
        private final String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            int intValue = (int) value;
            if (intValue >= 0 && intValue < days.length) {
                return days[intValue];
            } else {
                return "";
            }
        }
    }
    @SuppressLint("SetTextI18n")
    private void setBmiCategory(double bmi){
        if(bmi<=18.5){

            bmiCategory.setText("Underweight");
            bmiCategory.setTextColor(Color.parseColor("#FB59F4"));
        } else if (bmi<=24.9 ) {
            bmiCategory.setText("Normal weight");
            bmiCategory.setTextColor(Color.parseColor("#01FF01"));

        } else if (bmi<=29.9) {
            bmiCategory.setText("Overweight");
            bmiCategory.setTextColor(Color.parseColor("#FF8101"));

        } else if (bmi>=30) {
            bmiCategory.setText("You fat bro");
            bmiCategory.setTextColor(Color.parseColor("#DF0101"));

        }
    }
}
