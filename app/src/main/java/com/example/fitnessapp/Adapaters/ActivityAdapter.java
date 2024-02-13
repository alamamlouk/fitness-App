package com.example.fitnessapp.Adapaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.Entity.Activity;
import com.example.fitnessapp.R;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {
    private List<Activity> activities;
    private Context context;
    private OnRecyclerViewItemClickListener listener;


    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }
    public ActivityAdapter(List<Activity> activities, Context context ) {
        this.activities = activities;
        this.context = context;


    }

    @NonNull
    @Override
    public ActivityAdapter.ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityAdapter.ActivityViewHolder holder, int position) {

        Activity activity = activities.get(position);
        holder.textViewName.setText(activity.getActivity_name());
        holder.textViewTime.setText(String.valueOf(activity.getTime_to_finish()));
        holder.textViewState.setText(activity.getState());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onRecyclerViewItemClicked(activity.getId());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return activities.size();
    }



    public class ActivityViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewTime;
        TextView textViewState;
        TextView textViewNumberOfExercises;
        ImageView ImageViewOfActivity;
        ProgressBar ProgressBarActivity;
        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewState = itemView.findViewById(R.id.textViewState);
            textViewNumberOfExercises=itemView.findViewById(R.id.numberOfExercises);
            ImageViewOfActivity=itemView.findViewById(R.id.activityImage);
            ProgressBarActivity=itemView.findViewById(R.id.progressBar1);
        }
    }

}
