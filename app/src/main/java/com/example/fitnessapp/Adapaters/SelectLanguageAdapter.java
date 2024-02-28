package com.example.fitnessapp.Adapaters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fitnessapp.R;

public class SelectLanguageAdapter extends BaseAdapter {
    Context context;
    int[] flags;
    String[] countryNames;
    LayoutInflater inflater;

    public SelectLanguageAdapter(Context context, int[] flags, String[] countryNames) {
        this.context = context;
        this.flags = flags;
        this.countryNames = countryNames;
        this.inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return flags.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.language_spinner, null);
        ImageView icon = (ImageView) convertView.findViewById(R.id.flagAdapter);
        TextView names = (TextView) convertView.findViewById(R.id.textAdapter);
        icon.setImageResource(flags[position]);
        names.setText(countryNames[position]);
        return convertView;
    }
}
