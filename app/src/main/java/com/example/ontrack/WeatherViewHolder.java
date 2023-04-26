package com.example.ontrack;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class WeatherViewHolder extends RecyclerView.ViewHolder {

    public TextView day;
    public TextView temp_mm;
    public TextView description;
    public TextView pop;
    public TextView uvi;
    public TextView morn_temp;
    public TextView day_temp;
    public TextView eve_temp;
    public TextView night_temp;

    public ImageView image;

    WeatherViewHolder(View view) {
        super(view);

        day = view.findViewById(R.id.text_day);
        temp_mm = view.findViewById(R.id.text_temp_mm);
        description = view.findViewById(R.id.text_description7);
        pop = view.findViewById(R.id.text_pop);
        uvi = view.findViewById(R.id.text_uvi7);
        morn_temp = view.findViewById(R.id.text_morn_temp);
        day_temp = view.findViewById(R.id.text_day_temp);
        eve_temp = view.findViewById(R.id.text_eve_temp);
        night_temp = view.findViewById(R.id.text_night_temp);

        image = view.findViewById(R.id.image_weather_icon7);



    }

}
