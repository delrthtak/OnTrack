package com.example.ontrack;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HealthAdapter extends RecyclerView.Adapter<HealthViewHolder> {

    public static final String TAG = "HealthAdapter";

    private List<ActivityLog> activityLogs;
    private List<WaterLog> waterLogs;
    private HealthActivity healthActivity;
    private boolean activityActive;


    HealthAdapter(List<ActivityLog> activityLogs, HealthActivity healthActivity, boolean fine) {
        this.activityLogs = activityLogs;
        this.healthActivity = healthActivity;
        this.activityActive = true;

    }
    HealthAdapter(List<WaterLog> waterLogs, HealthActivity healthActivity) {
        this.waterLogs = waterLogs;
        this.healthActivity = healthActivity;
        this.activityActive = false;
    }


    @NonNull
    @Override
    public HealthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Start");
        View itemView;
        if (activityActive) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_activity, parent, false);

        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_water, parent, false);
        }
        itemView.setOnClickListener(this.healthActivity);
        Log.d(TAG, "onCreateViewHolder: End");
        return new HealthViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HealthViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Start");
        if (activityActive) {
            ActivityLog current = activityLogs.get(position);
            holder.activityName.setText(current.getActivityName());
            holder.activityDate.setText(current.getDate());
            holder.activityDescription.setText(current.getUserNotes());
            holder.activityValue.setText(current.getMinutes());
        } else {
            WaterLog current = waterLogs.get(position);
            Log.d(TAG, "onBindViewHolder: " + current.getDate());
            holder.waterDate.setText(current.getDate());
            holder.waterValue.setText(String.valueOf(current.getTotal()));
            if (current.isGoalMet()) {
                holder.image.setVisibility(View.VISIBLE);
            } else {
                holder.image.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (activityActive) {
            return activityLogs.size();
        } else {
            return waterLogs.size();
        }
    }
}
