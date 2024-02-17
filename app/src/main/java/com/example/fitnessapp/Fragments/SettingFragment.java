package com.example.fitnessapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnessapp.Entity.User;
import com.example.fitnessapp.R;
import com.example.fitnessapp.Services.UserServices;

import org.w3c.dom.Text;

import java.text.DecimalFormat;


public class SettingFragment extends Fragment {

    private EditText editTextUserName;
    private EditText editWeight;
    private EditText editHeight;
    private Button buttonUpdate;
    private boolean isEditing = false;
    private ImageView imageView;

    private TextView bmi;
    UserServices userService ;

    public SettingFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService=new UserServices(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_setting, container, false);
        editTextUserName = view.findViewById(R.id.editTextUserName);
        editHeight=view.findViewById(R.id.editTextHeight);
        editWeight=view.findViewById(R.id.editTextWeight);
        buttonUpdate=view.findViewById(R.id.buttonCalculateBMI);
        imageView=view.findViewById(R.id.editIcon);
        bmi=view.findViewById(R.id.BMI);

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
    private void displayUserDetails() {
        userService.open();
        User user = userService.getUserDetails();

        if (user != null) {
            editTextUserName.setText(user.getUserName());
            editHeight.setText(String.valueOf(user.getUserHeight()));
            editWeight.setText(String.valueOf(user.getUserWeight()));
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String  formattedBMI=decimalFormat.format(user.getUserBMI()) ;
            bmi.setText("BMI: "+formattedBMI);
        }
        userService.close();
    }

    private void updateUserInfo() {
        if(editWeight.getText().toString().isEmpty()||editHeight.getText().toString().isEmpty()){
            Toast.makeText(requireContext(), "Please enter all details", Toast.LENGTH_SHORT).show();
            return;
        }
        userService.open();
        User user =userService.getUserDetails();
        user.setUserName(editTextUserName.getText().toString());
        user.setUserHeight(Double.parseDouble(editHeight.getText().toString()));
        user.setUserWeight(Double.parseDouble(editWeight.getText().toString()));
        double newBMI=Double.parseDouble(editWeight.getText().toString())/(Math.pow(Double.parseDouble(editHeight.getText().toString()),2));
        user.setUserBMI(newBMI);
        boolean success = userService.updateUserDetails(user);

        if (success) {
            bmi.setText("BMI: "+String.valueOf(newBMI));
            enableEditing();
        } else {
            Log.d("Error", "updateUserInfo:Error ");
        }
        userService.close();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayUserDetails();

    }
}