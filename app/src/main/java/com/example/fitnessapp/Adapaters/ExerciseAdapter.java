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

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private List<Exercise> exerciseList;
    private Context context;

    public ExerciseAdapter(List<Exercise> exerciseList, Context context) {
        this.exerciseList = exerciseList;
        this.context = context;
    }

    @NonNull
    @Override
    public ExerciseAdapter.ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.exercice_items, parent, false);

        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapter.ExerciseViewHolder holder, int position) {
            Exercise exercise=exerciseList.get(position);
            holder.checkBox.setChecked(exercise.isState());
            holder.ExerciseName.setText(exercise.getExercise_name());
            holder.timeToFinishTheExercise.setText(String.valueOf(exercise.getTime_to_finish()));
    }

    @Override
    public int getItemCount() {
        return this.exerciseList.size();
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder{
        TextView ExerciseName,timeToFinishTheExercise;
        CheckBox checkBox;
        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
        ExerciseName=itemView.findViewById(R.id.textViewExerciseName);
        timeToFinishTheExercise=itemView.findViewById(R.id.textViewExerciseTime);
        checkBox=itemView.findViewById(R.id.checkboxStatus);

        }
    }
}
