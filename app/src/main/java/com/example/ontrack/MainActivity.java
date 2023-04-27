package com.example.ontrack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, List<Assignment> {
    public static final String TAG = "Main Activity";

    private TextView tvCityState;
    private TextView tvTemp;
    private TextView tvDescription;
    private ImageView ivWeather;

    public double lat;
    public double lon;

    public Weather weather;
    public List<Day> days = new ArrayList<>();

    private Assignment ass;
    private RecyclerView recView;
    public static List<Assignment> items = new ArrayList<>();

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutMan;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCityState = findViewById(R.id.text_CityState);
        tvTemp = findViewById(R.id.text_Temp);
        tvDescription = findViewById(R.id.text_WeatherDescription);
        ivWeather = findViewById(R.id.img_Image);




        if (hasNetworkConnection()) {
            doDownload();

        } else {
            setNoConnection();
        }

        recView = findViewById(R.id.rec_Up);

        recView.setHasFixedSize(true);

        layoutMan = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recView.setLayoutManager(layoutMan);

        mAdapter = new MainAssAdapter(this, items);
        recView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recView.getContext(), 1);

        recView.addItemDecoration(dividerItemDecoration);

        loadFile();
    }

    public void setNoConnection() {
        tvCityState.setText("No Service");
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

    private void loadFile() {
        Log.d(TAG, "loadFile: Loading JSON file");
        try {
            //Toast.makeText(this, getString(R.string.loading), Toast.LENGTH_SHORT).show();
            InputStream is = getApplicationContext().
                    openFileInput(getString(R.string.file_name));

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            writeJsonStream(items);
        } catch (FileNotFoundException e) {
            try {
                InputStream inputStream = getApplicationContext().openFileInput(String.valueOf(R.string.file_name));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
    }

    public void writeJsonStream(List<Assignment> its) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(openFileOutput(String.valueOf(R.string.file_name), Context.MODE_PRIVATE)));
        writer.setIndent("  ");
        writeAssArray(writer, its);
        writer.close();
    }

    public void writeAssArray(JsonWriter writer, List<Assignment> its) throws IOException {
        writer.beginArray();
        for (Assignment it : its) {
            writeNote(writer, it);
        }
        writer.endArray();
    }

    public void writeNote(JsonWriter writer, Assignment its) throws IOException {
        writer.beginObject();
        writer.name("Name").value(its.getName());
        if (its.getNotes().toString().length() > 80) {
            String word = its.getNotes().toString().substring(0, 80) + "...";
            writer.name("Notes").value(word);
        } else {
            writer.name("Notes").value(its.getNotes());
        }
        writer.name("Due").value(its.getDueDate());
        writer.name("Priority").value(its.getPriority());
        writer.endObject();
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
        Log.d(TAG, "setWeather: hahahahhahaha");
    }

    public Weather getWeather(){
        return weather;
    }
    public void setDays(List<Day> days) {
        this.days = days;
    }
    public List<Day> getDays(){
        return this.days;
    }
    public void doWeather() {
        String temp = getLocationName(this.weather.getLat(), this.weather.getLon());
        tvCityState.setText(temp);
        tvTemp.setText(this.weather.getTemp());
        tvDescription.setText(this.weather.getDescription());
        int iconResId = getResources().getIdentifier(this.weather.getIcon(), "mipmap", getPackageName());
        ivWeather.setImageResource(iconResId);
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
    private double[] getLatLon(String userProvidedLocation) {
        Geocoder geocoder = new Geocoder(this); // Here, “this” is an Activity
        try {
            List<Address> address =
                    geocoder.getFromLocationName(userProvidedLocation, 1);
            if (address == null || address.isEmpty()) {
                // Nothing returned!
                return null;
            }
            this.lat = address.get(0).getLatitude();
            this.lon = address.get(0).getLongitude();

            return new double[] {lat, lon};
        } catch (IOException e) {
            // Failure to get an Address object
            return null;
        }
    }

    public static List<Assignment> getList() {
        return items;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_home:
                //Intent intent = new Intent(this, CreateActivity.class);
                //startActivity(intent);
                return true;
            case R.id.menu_assignments:
                Intent aintent = new Intent(this, AssignmentActivity.class);
                startActivity(aintent);
                return true;
            case R.id.menu_weather:
                Intent ain = new Intent(this, WeatherActivity.class);
                startActivity(ain);
                return true;
            case R.id.menu_health:
                Intent intent = new Intent(this, HealthActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_settings:
                //Intent aintent = new Intent(this, InfoActivity.class);
                //startActivity(aintent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {

    }

    public void updateList(List<Assignment> items) {
        this.items = items;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(@Nullable Object o) {
        return false;
    }

    @NonNull
    @Override
    public Iterator<Assignment> iterator() {
        return null;
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] ts) {
        return null;
    }

    @Override
    public boolean add(Assignment assignment) {
        return false;
    }

    @Override
    public boolean remove(@Nullable Object o) {
        return false;
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return false;
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends Assignment> collection) {
        return false;
    }

    @Override
    public boolean addAll(int i, @NonNull Collection<? extends Assignment> collection) {
        return false;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        return false;
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Assignment get(int i) {
        return null;
    }

    @Override
    public Assignment set(int i, Assignment assignment) {
        return null;
    }

    @Override
    public void add(int i, Assignment assignment) {

    }

    @Override
    public Assignment remove(int i) {
        return null;
    }

    @Override
    public int indexOf(@Nullable Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(@Nullable Object o) {
        return 0;
    }

    @NonNull
    @Override
    public ListIterator<Assignment> listIterator() {
        return null;
    }

    @NonNull
    @Override
    public ListIterator<Assignment> listIterator(int i) {
        return null;
    }

    @NonNull
    @Override
    public List<Assignment> subList(int i, int i1) {
        return null;
    }
}