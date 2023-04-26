package com.example.ontrack;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Day {

    public static final String f = "°F";


    private String day_day_date;
    private String day_high_low;
    private String day_pop;
    private String day_temp_morn;
    private String day_temp_day;
    private String day_temp_eve;
    private String day_temp_night;
    private String day_description;
    private String day_icon;
    private String day_uvi;



    @RequiresApi(api = Build.VERSION_CODES.O)
    Day(int day_dt, double day, double min, double max, double night, double eve, double morn, String desc, String icon, int pop, double uvi, Weather weather) {
        super();
        LocalDateTime ldt = LocalDateTime.ofEpochSecond(day_dt + weather.getTimezone_offset(), 0, ZoneOffset.UTC);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE, m/dd", Locale.getDefault());
        day_day_date = ldt.format(dtf);
        this.day_high_low = (int) max + f + "/" + (int) min + f;
        this.day_temp_day = (int) day + f;
        this.day_temp_night = (int) night + f;
        this.day_temp_eve = (int) eve + f;
        this.day_temp_morn = (int) morn + f;

        this.day_pop = "(" + pop + "% precip.)";
        this.day_description = desc;
        this.day_icon = "_" + icon;
        this.day_uvi = "UV Index: " + uvi;
    }

    public String getDay_day_date() { return day_day_date; }
    public String getDay_high_low() { return day_high_low; }
    public String getDay_pop() { return day_pop; }
    public String getDay_temp_morn() { return day_temp_morn; }
    public String getDay_temp_day() { return day_temp_day; }
    public String getDay_temp_eve() { return day_temp_eve; }
    public String getDay_temp_night() { return day_temp_night; }
    public String getDay_Description() { return day_description; }
    public String getDay_Icon() { return day_icon; }
    public String getDay_uvi() { return day_uvi; }
}
