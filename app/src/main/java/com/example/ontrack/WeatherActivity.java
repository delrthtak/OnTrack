package com.example.ontrack;

import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "WeatherActivity";

    private TextView tvCity;
    private TextView tvTime;
    private TextView tvTemp;
    private TextView tvFeels;
    private TextView tvDescription;
    private TextView tv6am;
    private TextView tv1pm;
    private TextView tv5pm;
    private TextView tv11pm;
    private TextView tvHumidity;
    private TextView tvUVI;
    private TextView tvSunrise;
    private TextView tvSunset;
    private ImageView ivImage;
    private RecyclerView recView;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private Weather weather;
    private List<Day> days;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        setViews();

        if (hasNetworkConnection()) {
            doDownload();
        } else {
            setNoConnection();
        }

        //setRec();
    }

    public void setViews() {
        Log.d(TAG, "setViews: Start");
        tvCity = findViewById(R.id.aw_CityState);
        tvTime = findViewById(R.id.aw_Time);
        tvTemp = findViewById(R.id.aw_Temp);
        tvFeels = findViewById(R.id.aw_Feels);
        tvDescription = findViewById(R.id.aw_Description);
        tv6am = findViewById(R.id.aw_6amTemp);
        tv1pm = findViewById(R.id.aw_1pmTemp);
        tv5pm = findViewById(R.id.aw_5pmTemp);
        tv11pm = findViewById(R.id.aw_11pmTemp);
        tvHumidity = findViewById(R.id.aw_Humidity);
        tvUVI = findViewById(R.id.aw_UVI);
        tvSunrise = findViewById(R.id.aw_Sunrise);
        tvSunset = findViewById(R.id.aw_Sunset);
        ivImage = findViewById(R.id.aw_Image);
        recView = findViewById(R.id.aw_Rec);
        Log.d(TAG, "setViews: Finish");
    }
    public void setRec() {
        Log.d(TAG, "setRec: Start");
        recView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recView.setLayoutManager(layoutManager);
        mAdapter = new WeatherAdapter(days, this);
        recView.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recView.getContext(), 0);
        recView.addItemDecoration(dividerItemDecoration);
        Log.d(TAG, "setRec: Finish");
    }

    public void setNoConnection() {
        //tvCityState.setText("No Service");
        tvTemp.setText("XX");
        tvDescription.setText("No Description");
    }
    private void doDownload() {
        WeatherRunnable task = new WeatherRunnable(this);
        new Thread(task).start();
    }
    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }
    public void setWeather(Weather weather) { this.weather = weather;  }

    public void doWeather() {
        String temp = getLocationName(this.weather.getLat(), this.weather.getLon());
        tvCity.setText(temp);
        tvTime.setText(this.weather.getDate_time());
        tvTemp.setText(this.weather.getTemp());
        tvFeels.setText(this.weather.getFeels_like());
        tvDescription.setText(this.weather.getDescription());
        tv6am.setText(this.weather.getTemp_morn());
        tv1pm.setText(this.weather.getTemp_day());
        tv5pm.setText(this.weather.getTemp_eve());
        tv11pm.setText(this.weather.getTemp_night());
        tvHumidity.setText(this.weather.getHumidity());
        tvUVI.setText(this.weather.getUvi());
        tvSunrise.setText(this.weather.getSunrise());
        tvSunset.setText(this.weather.getSunset());
        int iconResId = getResources().getIdentifier(this.weather.getIcon(), "mipmap", getPackageName());
        ivImage.setImageResource(iconResId);
    }
    private String getLocationName(double lat, double lon) {
        Geocoder geocoder = new Geocoder(this); // Here, “this” is an Activity
        try {
            List<Address> address = geocoder.getFromLocation(lat, lon, 1);
            if (address == null || address.isEmpty()) {
                // Nothing returned!
                Log.d(TAG, "getLocationName: nothing returned");
                return null;
            }
            String country = address.get(0).getCountryCode();
            String p1 = "";
            String p2 = "";
            if (country.equals("US")) {
                p1 = address.get(0).getLocality();
                p2 = address.get(0).getAdminArea();
            } else {
                p1 = address.get(0).getLocality();
                if (p1 == null)
                    p1 = address.get(0).getSubAdminArea();
                p2 = address.get(0).getCountryName();
            }
            Log.d(TAG, "getLocationName: about to return " + p1);
            return p1 + ", " + p2;
        } catch (IOException e) {
            // Failure to get an Address object
            return null;
        }
    }

    @Override
    public void onClick(View view) {

    }



}
