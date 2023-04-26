package com.example.ontrack;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HealthViewHolder extends RecyclerView.ViewHolder {

    public static final String TAG = "HealthViewHolder";

    public TextView activityDate;
    public TextView activityName;
    public TextView activityDescription;
    public TextView activityValue;

    public TextView waterDate;
    public TextView waterValue;
    public ImageView image;




    public HealthViewHolder(@NonNull View itemView) {
        super(itemView);

        //try{
            Log.d(TAG, "HealthViewHolder: begin try");
            activityDate = itemView.findViewById(R.id.ract_activityDate);
            activityName = itemView.findViewById(R.id.ract_Name);
            activityDescription = itemView.findViewById(R.id.ract_Description);
            activityValue = itemView.findViewById(R.id.ract_Value);

        //} catch (Exception e) {
            Log.d(TAG, "HealthViewHolder: ");
            waterDate = itemView.findViewById(R.id.rw_Date);
            waterValue = itemView.findViewById(R.id.rw_Value);
            image = itemView.findViewById(R.id.rw_Check);
            //e.printStackTrace();
       // }


    }
}
