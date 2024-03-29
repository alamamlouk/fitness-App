package com.example.fitnessapp.Adapaters;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.Entity.Exercise;
import com.example.fitnessapp.R;
import com.example.fitnessapp.Services.ActivityServices;
import com.example.fitnessapp.Services.RelationServices;
import com.example.fitnessapp.Stats;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapter.ExerciseViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);
        relationServices.open();
        activityServices.open();
        holder.ExerciseName.setText(exercise.getExercise_name());
        holder.timeToFinishTheExercise.setText(String.valueOf(exercise.getTime_to_finish())+" Minutes ");
        holder.exerciseDescription.setText(exercise.getExercise_description());
        String imageName=exercise.getExercise_path_image();
        int resourceId = holder.itemView.getContext().getResources().getIdentifier(
                imageName, "drawable", holder.itemView.getContext().getPackageName());
        if (resourceId == 0) {
            resourceId = holder.itemView.getContext().getResources().getIdentifier(
                    "arm_day", "drawable", holder.itemView.getContext().getPackageName());
        }

        Drawable drawable = holder.itemView.getContext().getResources().getDrawable(resourceId);
        holder.imageView.setImageDrawable(drawable);
        if(relationServices.getExerciseProgress(activityId,exercise.getId())==0){
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    relationServices.updateExerciseProgress(activityId, exercise.getId(), 1);
                    relationServices.setTimeFinishedIn(activityId, exercise.getId());
                    activityServices.updateTimeExercised(activityId,exercise.getTime_to_finish());

                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    ComponentName componentName = new ComponentName(context, Stats.class);
                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
                    Intent intent = new Intent(context, Stats.class);
                    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                    context.sendBroadcast(intent);

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
        ImageView imageView;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            ExerciseName = itemView.findViewById(R.id.textViewExerciseName);
            timeToFinishTheExercise = itemView.findViewById(R.id.textViewExerciseTime);
            checkBox = itemView.findViewById(R.id.checkboxStatus);
            exerciseDescription = itemView.findViewById(R.id.exerciseDescription);
            imageView=itemView.findViewById(R.id.exerciseImage);

        }
    }
}
