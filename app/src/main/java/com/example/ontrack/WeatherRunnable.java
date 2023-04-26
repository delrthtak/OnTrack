package com.example.ontrack;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class WeatherRunnable implements Runnable {

    public static final String TAG = "WeatherRunnable";
    private MainActivity mainActivity;
    private WeatherActivity weatherActivity;
    private double lat = 41.8675766;
    private double lon = -87.616232;
    private Boolean imperial = true;
    private Boolean weatherRun;
    private static final String APIKey = "368097b0a836a67d8b3a79cfd0c553df";
    //https://api.openweathermap.org/data/2.5/onecall?lat=41.8675766&lon=-87.616232&appid=368097b0a836a67d8b3a79cfd0c553df&units=imperial&lang=en&exclude=minutely
    private static String DATA_URL;

    WeatherRunnable(MainActivity ma, double lat, double lon, boolean imperial){
        this.mainActivity = ma;
        this.lat = lat;
        this.lon = lon;
        this.imperial = imperial;
    }
    WeatherRunnable(MainActivity ma) {
        this.weatherRun = false;
        this.mainActivity = ma;
    }

    public WeatherRunnable(WeatherActivity weatherActivity) {
        this.weatherRun = true;
        this.weatherActivity = weatherActivity;
    }

    public void setURL(double lat, double lon, boolean imperial) {
        String unit = "imperial";
        if (imperial == false) unit = "metric";
        DATA_URL = "https://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon=" + lon + "&appid=" + APIKey + "&units=" + unit + "&lang=en&exclude=minutely,hourly,alerts";
    }

    @Override
    public void run() {
        setURL(lat, lon, imperial);
        Uri dataUri = Uri.parse(DATA_URL);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "run: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            Log.d(TAG, "run: trying");

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "run: HTTP ResponseCode NOT OK: " + conn.getResponseCode());
                handleResults(null);
                return;
            }

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "run: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "run: ", e);
            handleResults(null);
            return;
        }

        handleResults(sb.toString());

    }

    private void handleResults(String s) {

        if (!weatherRun) {
            if (s == null) {
                Log.d(TAG, "handleResults: Failure in data download");
                mainActivity.runOnUiThread(mainActivity::setNoConnection);
                return;
            }
            ArrayList<Day> dayList = parseJSON(s);
            mainActivity.runOnUiThread(() -> {
                if (dayList != null)
                    //Toast.makeText(mainActivity, "Loaded " + hourList.size() + " countries.", Toast.LENGTH_LONG).show();
                    //mainActivity.updateData(hourList);
                    mainActivity.doWeather();
                mainActivity.setDays(dayList);
                Log.d(TAG, "handleResults: updated?");
            });
        } else {
            if (s == null) {
                Log.d(TAG, "handleResults: Failure in data download");
                weatherActivity.runOnUiThread(mainActivity::setNoConnection);
                return;
            }
            ArrayList<Day> dayList = parseJSON(s);
            weatherActivity.runOnUiThread(() -> {
                if (dayList != null)
                    //Toast.makeText(mainActivity, "Loaded " + hourList.size() + " countries.", Toast.LENGTH_LONG).show();
                    //mainActivity.updateData(hourList);
                    weatherActivity.doWeather();
                weatherActivity.setDays(dayList);
                weatherActivity.setRec();
                Log.d(TAG, "handleResults: updated?");
            });
        }
    }

    private ArrayList<Day> parseJSON(String s) {

        Log.d(TAG, "parseJSON: beginning");
        int dt;
        double day;
        double min;
        double max;
        double night;
        double eve;
        double morn;
        String desc;
        String icon;
        int pop;
        double uvi;

        Weather weather = new Weather();
        ArrayList<Day> DayList = new ArrayList<>();
        try {
            Log.d(TAG, "parseJSON: try");
            weather.setFar(imperial);

            JSONObject jObjMain = new JSONObject(s);

            weather.setTimezone(jObjMain.getString("timezone"));
            weather.setTimezone_offset(jObjMain.getInt("timezone_offset"));
            weather.setLat(lat);
            weather.setLon(lon);

            Log.d(TAG, "parseJSON: main_current" + lat);

            //main_current
            JSONObject current = jObjMain.getJSONObject("current");
            weather.setDt(current.getInt("dt"));
            weather.setSunrise(current.getInt("sunrise"));
            weather.setSunset(current.getInt("sunset"));
            weather.setTemp(current.getDouble("temp"));
            weather.setFeels_like(current.getDouble("feels_like"));
            weather.setPressure(current.getInt("pressure"));
            weather.setHumidity(current.getInt("humidity"));
            weather.setUvi(current.getDouble("uvi"));
            weather.setClouds(current.getInt("clouds"));
            weather.setVisibility(current.getInt("visibility"));
            weather.setWind_speed(current.getDouble("wind_speed"));
            weather.setWind_degree(current.getInt("wind_deg"));
            try { weather.setWind_gust(current.getDouble("wind_gust"));
            } catch(Exception e) { weather.setWind_gust(0.0); }
            try {
                JSONObject rain = current.getJSONObject("rain");
                weather.setRain(rain.getDouble("1h"));
            } catch(Exception e) { weather.setRain(0.0); }
            try {
                JSONObject snow = current.getJSONObject("snow");
                weather.setSnow(snow.getDouble("1h"));
            } catch(Exception e) { weather.setRain(0.0); }


            Log.d(TAG, "parseJSON: main_current_weather");
            //main_current_weather
            JSONArray current_weather = current.getJSONArray("weather");
            JSONObject c_w_first = (JSONObject) current_weather.get(0);
            weather.setMain(c_w_first.getString("main"));
            weather.setDescription(c_w_first.getString("description"));
            weather.setIcon(c_w_first.getString("icon"));

            //main_daily
            JSONArray main_daily = jObjMain.getJSONArray("daily");
            JSONObject daily = main_daily.getJSONObject(0);
            JSONObject daily_temp = daily.getJSONObject("temp");

            double dday = daily_temp.getDouble("day");
            double dnight = daily_temp.getDouble("night");
            double deve = daily_temp.getDouble("eve");
            double dmorn = daily_temp.getDouble("morn");
            weather.setTemps(dday,dnight,deve,dmorn);
            weather.setLat(lat);
            weather.setLon(lon);
            if (weatherRun) {
                weatherActivity.setWeather(weather);
            } else {
                mainActivity.setWeather(weather);
            }
            Log.d(TAG, "parseJSON: main_daily");
            //main_hourly
            JSONArray main_hourly = jObjMain.getJSONArray("daily");

            for (int i = 0; i < main_hourly.length(); i++) {
                Log.d(TAG, "parseJSON: day" + i);
                JSONObject j_hour = (JSONObject) main_hourly.get(i);

                dt = j_hour.getInt("dt");

                JSONObject j_h_weather = j_hour.getJSONObject("temp");

                day = j_h_weather.getDouble("day");
                min = j_h_weather.getDouble("min");
                max = j_h_weather.getDouble("max");
                night = j_h_weather.getDouble("night");
                eve = j_h_weather.getDouble("eve");
                morn = j_h_weather.getDouble("morn");

                JSONArray j_h_w = j_hour.getJSONArray("weather");
                JSONObject h_w = j_h_w.getJSONObject(0);

                desc = h_w.getString("description");
                icon = h_w.getString("icon");

                pop = j_hour.getInt("pop");
                uvi = j_hour.getDouble("uvi");

                DayList.add(new Day(dt, day,min,max,night,eve,morn,desc,icon,pop,uvi,weather));

            }
            //Collections.sort(hourList);
            return DayList;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
