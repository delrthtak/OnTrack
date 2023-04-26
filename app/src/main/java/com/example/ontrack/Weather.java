package com.example.ontrack;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Weather {
    public static final String f = "°F";
    public static final String c = "°C";
    //public static final String[] week = {"Sun", "Mon", "Tue","Wed","Thu", "Fri", "Sat"};
    private boolean far;

    private String location;
    private double lat;
    private double lon;
    private String timezone;
    private int timezone_offset;

    //current
    private int dt;
    private String date_time;
    private String weather_day;
    private String sunrise;
    private String sunset;
    private String temp;
    private String feels_like;
    private int pressure;
    private String humidity;
    private String uvi;
    private int clouds;
    private String visibility;
    private double wind_speed;
    private int wind_degree;
    private double wind_gust;
    private double rain;
    private double snow;

    //weather
    private String main;
    private String description;
    private String icon;

    //daily
    private String temp_morn;
    private String temp_day;
    private String temp_eve;
    private String temp_night;

    Weather() { super(); }

    public void setFar(Boolean far) { this.far = far; }

    public void setLocation(String location) { this.location = location; }

    public void setLat(double lat) { this.lat = lat; }
    public void setLon(double lon) { this.lon = lon; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    public void setTimezone_offset(int offset) { this.timezone_offset = offset; }

    //setCurrent
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDt(int dt) {
        this.dt = dt;
        LocalDateTime ldt = LocalDateTime.ofEpochSecond(dt + timezone_offset, 0, ZoneOffset.UTC);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE", Locale.getDefault());
        this.weather_day = ldt.format(dtf);
        dtf = DateTimeFormatter.ofPattern("EEE MMM dd h:mm a, yyyy", Locale.getDefault());
        this.date_time = ldt.format(dtf);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setSunrise(int sunrise) {
        LocalDateTime ldt = LocalDateTime.ofEpochSecond(sunrise + timezone_offset, 0, ZoneOffset.UTC);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault());
        this.sunrise = "Sunrise: " + ldt.format(dtf);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setSunset(int sunset) {
        LocalDateTime ldt = LocalDateTime.ofEpochSecond(sunset + timezone_offset, 0, ZoneOffset.UTC);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault());
        this.sunset = "Sunset: " + ldt.format(dtf);
    }
    public void setTemp(double temp) {
        if (this.far) {
            this.temp = (int) temp + f;
        } else {
            this.temp = (int) temp + c;
        }
    }
    public void setFeels_like(double feels_like) {
        if (far) { this.feels_like = "Feels Like " + (int) feels_like + f; }
        else { this.feels_like = "Feels Like " + (int) feels_like + c; }
    }
    public void setPressure(int pressure) { this.pressure = pressure; }
    public void setHumidity(int humidity) { this.humidity = "Humidity: " + humidity + "%"; }
    public void setUvi(double uvi) { this.uvi = "UV Index: " + uvi; }
    public void setClouds(int clouds) { this.clouds = clouds; }
    public void setVisibility(int visibility) { this.visibility = "Visibility: " + visibility + "mi"; }
    public void setWind_speed(double wind_speed) { this.wind_speed = wind_speed; }
    public void setWind_degree(int wind_degree) { this.wind_degree = wind_degree; }
    public void setWind_gust(double wind_gust) { this.wind_gust = wind_gust; }

    public void setRain(double rain) {
        this.rain = rain;
    }

    public void setSnow(double snow) {
        this.snow = snow;
    }

    //setWeather
    public void setMain(String main) { this.main = main; }
    public void setDescription(String description) {
        String comp = "";
        if (this.main.contains("rain") || this.main.contains("Rain")) {
            comp = "(" + this.rain + " " + this.main + ")";
        }
        if (this.main.contains("snow") || this.main.contains("Snow")) {
            comp = "(" + this.snow + " " + this.main + ")";
        }
        if (this.main.contains("cloud") || this.main.contains("Cloud")) {
            comp = "(" + this.clouds + "% " + this.main + ")";
        }
        this.description = description + " " + comp;
    }
    public void setIcon(String icon) { this.icon = "_" + icon; }

    public void setTemps(double day, double night, double eve, double morn) {
        if (far) {
            this.temp_day = (int) day + f;
            this.temp_night = (int) night + f;
            this.temp_eve = (int) eve + f;
            this.temp_morn = (int) morn + f;
        } else {
            this.temp_day = (int) day + c;
            this.temp_night = (int) night + c;
            this.temp_eve = (int) eve + c;
            this.temp_morn = (int) morn + c;
        }
    }

    //Get ####################################################################

    public boolean getFar() { return far; }
    public String getDate_time() { return date_time; }

    public double getLat() { return lat; }
    public double getLon() { return lon; }
    public String getTimezone() { return timezone; }
    public int getTimezone_offset() { return timezone_offset; }
    public String getLocation() { return location; }

    //getCurrent
    public String getWeather_day() { return weather_day; }
    public int getDt() { return dt; }
    public String getSunrise() { return sunrise; }
    public String getSunset() { return sunset; }
    public String getTemp() { return temp; }
    public String getFeels_like() { return feels_like; }
    public int getPressure() { return pressure; }
    public String getHumidity() { return humidity; }
    public String getUvi() { return uvi; }
    public int getClouds() { return clouds; }
    public String getVisibility() { return visibility; }
    public double getWind_speed() { return wind_speed; }
    public int getWind_degree() { return wind_degree; }
    public double getWind_gust() { return wind_gust; }

    public String getWind() {
        String dir;
        if (wind_degree >= 337.5 || wind_degree < 22.5)
            dir = "N";
        else if (wind_degree >= 22.5 && wind_degree < 67.5)
            dir = "NE";
        else if (wind_degree >= 67.5 && wind_degree < 112.5)
            dir = "E";
        else if (wind_degree >= 112.5 && wind_degree < 157.5)
            dir = "SE";
        else if (wind_degree >= 157.5 && wind_degree < 202.5)
            dir = "S";
        else if (wind_degree >= 202.5 && wind_degree < 247.5)
            dir = "SW";
        else if (wind_degree >= 247.5 && wind_degree < 292.5)
            dir = "W";
        else if (wind_degree >= 292.5 && wind_degree < 337.5)
            dir = "NW";
        else
            dir = "X";
        return "Winds: " + dir + " at " + wind_speed + " mph";
    }

    //getWeather
    public String getMain() { return main; }
    public String getDescription() { return description; }
    public String getIcon() { return icon; }

    public String getTemp_morn() { return temp_morn; }
    public String getTemp_day() { return temp_day; }
    public String getTemp_eve() { return temp_eve; }
    public String getTemp_night() { return temp_night; }



}
