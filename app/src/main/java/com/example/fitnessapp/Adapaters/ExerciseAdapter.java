package com.example.fitnessapp.Adapaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.Entity.Exercise;
import com.example.fitnessapp.R;
import com.example.fitnessapp.Services.ActivityServices;
import com.example.fitnessapp.Services.RelationServices;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private List<Exercise> exerciseList;
    private Context context;
    private long activityId;
    RelationServices relationServices;
    ActivityServices activityServices;


    public ExerciseAdapter(List<Exercise> exerciseList, Context context,long activityId) {
        this.exerciseList = exerciseList;
        this.context = context;
        this.activityId=activityId;
        relationServices=new RelationServices(context);
        activityServices=new ActivityServices(context);
    }

    @NonNull
    @Override
    public ExerciseAdapter.ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.exercice_items, parent, false);

        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapter.ExerciseViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);
        relationServices.open();
        activityServices.open();
        holder.ExerciseName.setText(exercise.getExercise_name());
        holder.timeToFinishTheExercise.setText(String.valueOf(exercise.getTime_to_finish()));
        holder.exerciseDescription.setText(exercise.getExercise_description());
        if(relationServices.getExerciseProgress(activityId,exercise.getId())==0){
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    relationServices.updateExerciseProgress(activityId, exercise.getId(), 1);
                    activityServices.updateTimeExercised(activityId,exercise.getTime_to_finish());
                    relationServices.close();
                    activityServices.close();
                    holder.checkBox.setClickable(false);
                }
            });
        }
        else {
            holder.checkBox.setChecked(true);
            holder.checkBox.setClickable(false);

        }
    }

    @Override
    public int getItemCount() {
        return this.exerciseList.size();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView ExerciseName, timeToFinishTheExercise, exerciseDescription;
        CheckBox checkBox;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            ExerciseName = itemView.findViewById(R.id.textViewExerciseName);
            timeToFinishTheExercise = itemView.findViewById(R.id.textViewExerciseTime);
            checkBox = itemView.findViewById(R.id.checkboxStatus);
            exerciseDescription = itemView.findViewById(R.id.exerciseDescription);

        }
    }
}
