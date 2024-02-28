package com.example.fitnessapp.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnessapp.Adapaters.SelectLanguageAdapter;
import com.example.fitnessapp.Entity.User;
import com.example.fitnessapp.R;
import com.example.fitnessapp.Services.UserServices;

import java.text.DecimalFormat;
import java.util.Locale;

public class SettingFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private EditText editTextUserName;
    private EditText editWeight;
    private EditText editHeight;
    private Button buttonUpdate;
    private boolean isEditing = false;
    private ImageView imageView;
    private boolean userSelected = false;
    private static final String PREFS_NAME = "LanguageFile";
    private static final String SELECTED_LANGUAGE_POSITION = "SelectedLanguagePosition";

    private int selectedLanguagePosition = 0;
    Spinner spinner;
    private TextView bmi;
    private UserServices userService;
    private final String[] countryName = {"French","Spanish","English","Italian"};
    private final int[] flags = {R.drawable.flagfrance,R.drawable.flagspain,R.drawable.english,R.drawable.itali};

    public SettingFragment() {
        // Required empty public constructor
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
        buttonUpdate = view.findViewById(R.id.buttonCalculateBMI);
        imageView = view.findViewById(R.id.editIcon);
        bmi = view.findViewById(R.id.BMI);
        spinner = view.findViewById(R.id.languageSpinner);
        SelectLanguageAdapter selectLanguageAdapter = new SelectLanguageAdapter(getContext(), flags, countryName);
        spinner.setAdapter(selectLanguageAdapter);
        spinner.setOnItemSelectedListener(this);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEditing();
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
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
        buttonUpdate.setVisibility(isEditing ? View.VISIBLE : View.GONE);
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
        }
        userService.close();
    }

    @SuppressLint("SetTextI18n")
    private void updateUserInfo() {
        if (editWeight.getText().toString().isEmpty() || editHeight.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Please enter all details", Toast.LENGTH_SHORT).show();
            return;
        }
        userService.open();
        User user = userService.getUserDetails();
        user.setUserName(editTextUserName.getText().toString());
        user.setUserHeight(Double.parseDouble(editHeight.getText().toString()));
        user.setUserWeight(Double.parseDouble(editWeight.getText().toString()));
        double newBMI = Double.parseDouble(editWeight.getText().toString()) / (Math.pow(Double.parseDouble(editHeight.getText().toString()), 2));
        user.setUserBMI(newBMI);
        boolean success = userService.updateUserDetails(user);

        if (success) {
            bmi.setText("BMI: " + newBMI);
            enableEditing();
        } else {
            Log.d("Error", "updateUserInfo:Error ");
        }
        userService.close();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        selectedLanguagePosition = prefs.getInt(SELECTED_LANGUAGE_POSITION, 0);
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
    public void changeLanguage(String language) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_language_change, null);
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
}
