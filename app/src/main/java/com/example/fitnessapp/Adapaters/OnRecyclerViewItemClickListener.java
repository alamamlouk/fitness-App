package com.example.fitnessapp.Adapaters;

import com.example.fitnessapp.Entity.Activity;

public interface OnRecyclerViewItemClickListener {
    void onRecyclerViewItemClicked(int position, Activity item);

    void onRecyclerViewItemClicked(long id);
}
