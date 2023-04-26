package com.example.ontrack;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherViewHolder> {

    public static final String TAG = "WeatherAdapter";
    private final List<Day> dayList;
    private final WeatherActivity weatherActivity;

    WeatherAdapter(List<Day> dayList, WeatherActivity weatherActivity) {
        this.dayList = dayList;
        this.weatherActivity = weatherActivity;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Start");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_weather, parent, false);
        itemView.setOnClickListener(this.weatherActivity);
        return new WeatherViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        Day current = dayList.get(position);

        holder.day.setText(current.getDay_day_date());
        holder.temp_mm.setText(current.getDay_high_low());
        holder.description.setText(current.getDay_Description());
        holder.pop.setText(current.getDay_pop());
        holder.uvi.setText(current.getDay_uvi());
        holder.morn_temp.setText(current.getDay_temp_morn());
        holder.day_temp.setText(current.getDay_temp_day());
        holder.eve_temp.setText(current.getDay_temp_eve());
        holder.night_temp.setText(current.getDay_temp_night());
        int iconResId = weatherActivity.getResources().getIdentifier(current.getDay_Icon(), "mipmap", weatherActivity.getPackageName());
        holder.image.setImageResource(iconResId);
    }

    @Override
    public int getItemCount() {
        return this.dayList.size();
    }
}
