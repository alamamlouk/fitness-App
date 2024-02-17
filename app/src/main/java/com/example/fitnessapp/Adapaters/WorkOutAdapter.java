package com.example.fitnessapp.Adapaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.Entity.WorkOut;
import com.example.fitnessapp.R;

import java.util.ArrayList;

public class WorkOutAdapter extends RecyclerView.Adapter<WorkOutAdapter.WorkOutViewHolder> {

    ArrayList<WorkOut> workOuts;
    public Context context;

    public WorkOutAdapter(ArrayList<WorkOut> workOuts, Context context) {
        this.workOuts = workOuts;
        this.context = context;
    }

    @NonNull
    @Override
    public WorkOutAdapter.WorkOutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item,parent,false);
        return new WorkOutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkOutAdapter.WorkOutViewHolder holder, int position) {
        WorkOut workOut=workOuts.get(position);
        holder.workoutImage.setImageResource(workOut.getWorkoutImg());
        holder.workOutTime.setText(String.valueOf(workOut.getWorkOutTime())+" Minuit");
        holder.numberOfExercises.setText(String.valueOf(workOut.getNumberOfExercises())+" Exercises");
        holder.workOutName.setText(workOut.getWorkoutTile());
        holder.cardView.setCardBackgroundColor(getBackgroundColor(position));


    }
    private int getBackgroundColor(int position) {
        if (position % 2 == 0) {
            return ContextCompat.getColor(context, R.color.orange);
        }
        return ContextCompat.getColor(context, R.color.darkgreen);
    }

    @Override
    public int getItemCount() {
        return workOuts.size();
    }

    public class WorkOutViewHolder extends RecyclerView.ViewHolder{
        public ImageView workoutImage;
        public TextView workOutName,numberOfExercises,workOutTime;
        public CardView cardView;

        public WorkOutViewHolder(@NonNull View itemView) {
            super(itemView);
            this.workoutImage=itemView.findViewById(R.id.workOutImage);
            this.workOutName=itemView.findViewById(R.id.workOutName);
            this.numberOfExercises=itemView.findViewById(R.id.nbExercises);
            this.workOutTime=itemView.findViewById(R.id.timeToFinish);
            this.cardView=itemView.findViewById(R.id.MNCardView);
        }
    }
}
