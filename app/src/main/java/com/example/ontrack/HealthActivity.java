package com.example.ontrack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HealthActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    public static final String TAG = "HealthActivity";

    private TextView tvWater;
    private TextView tvActivity;
    private TextView tvMinutes;
    private TextView tvStart;
    private TextView tvEnd;
    private TextView tvKeyword;
    private Spinner spHistory;
    private Button addWater;
    private Button addActivity;
    private Button clearButton;
    private Button searchButton;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<WaterLog> waterLogs;
    public static List<ActivityLog> activityLogs;
    public static List<Health> healthList;
    List<String> spinnerList = new ArrayList<>();

    private boolean showActivity;
    private int waterGoal;
    private int waterTotal;
    private int activityGoal;
    private int activityTotal;

    public static List<ActivityLog> getActivityLogs() {
        return activityLogs;
    }
    public static List<Health> getHealthList() {
        return healthList;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        waterLogs = new ArrayList<>();
        activityLogs = new ArrayList<>();
        healthList = new ArrayList<>();

        //testRecycler();
        //testGoals();

        setViews();
        setRec();
        try {
            loadFile();
            fillLogs();
            mAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillLogs() {
        for (Health health : healthList) {
            waterLogs.add(new WaterLog(health.getHealthDate(), health.getWater(), health.isDwgb()));
            activityLogs.addAll(health.getDailyActivityLog());
        }
    }

    private void loadFile() throws IOException {
        //Try check to see if fileName currently exists
        //if not then we create the file name with one new entry

        try {
            /*Log.d(TAG, "loadFile: Start");
            File file = new File(getApplicationContext().getFilesDir(), String.valueOf(R.string.health_json));
            Log.d(TAG, "loadFile: 1");
            FileReader fileReader = new FileReader(file);
            Log.d(TAG, "loadFile: 2");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Log.d(TAG, "loadFile: 3");
            StringBuilder stringBuilder = new StringBuilder();
            Log.d(TAG, "loadFile: 4");
            String line = bufferedReader.readLine();
            Log.d(TAG, "loadFile: 5");
            while (line != null){
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            Log.d(TAG, "loadFile: " + line);
*//*
            InputStream is = getApplicationContext().
                    openFileInput(getString(R.string.health_json));

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            Log.d(TAG, "loadFile: " + line);
            is.close(); */

            File file = new File(getApplicationContext().getFilesDir(), String.valueOf(R.string.health_json));
            StringBuilder sb = new StringBuilder();
            InputStream in = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + System.lineSeparator());
            }
            Log.d(TAG, "loadFile: " + sb.toString());
            in.close();
            fillRecycler();
            writeJsonStream(healthList);
        } catch (FileNotFoundException e) {
            FileOutputStream fos = this.openFileOutput(String.valueOf(R.string.health_json), Context.MODE_PRIVATE);
            Health newHealth = new Health();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yy");
            String currentDate = simpleDateFormat.format(new Date());
            Log.d(TAG, "loadFile: " + currentDate);
            newHealth.setHealthDate(currentDate);
            newHealth.setDwGoal(5);
            newHealth.setDwgb(false);
            newHealth.setDaGoal(30);
            newHealth.setDagb(false);
            newHealth.setWater(0);
            List<ActivityLog> tempLog = new ArrayList<>();
            newHealth.setDailyActivityLog(tempLog);
            //e.printStackTrace();
        }

    }

    public void writeJsonStream(List<Health> healthList) throws IOException {
        Log.d(TAG, "writeJsonStream: Start");
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(openFileOutput(String.valueOf(R.string.file_name), Context.MODE_PRIVATE)));
        writer.setIndent("  ");
        writeHealthArray(writer, healthList);
        writer.close();
        Log.d(TAG, "writeJsonStream: End");
    }

    public void writeHealthArray(JsonWriter writer, List<Health> healthList) throws IOException {
        writer.beginArray();
        for (Health health : healthList) {
            writeHealth(writer, health);
        }
        writer.endArray();
    }

    private void writeHealth(JsonWriter writer, Health health) throws IOException {
        writer.beginObject();

        writer.name("date").value(health.getHealthDate());
        writer.name("dwgoal").value(health.getDwGoal());
        writer.name("dwgb").value(health.isDwgb());
        writer.name("dagoal").value(health.getDaGoal());
        writer.name("dagb").value(health.isDagb());
        writer.name("water").value(health.getWater());
        for (ActivityLog activityLog : health.getDailyActivityLog()) {
            writer.beginObject();
            writer.name("type").value(health.getHealthDate());
            writer.name("tod").value(health.getDwGoal());
            writer.name("time").value(health.isDwgb());
            writer.name("notes").value(health.getDaGoal());
            writer.endObject();
        }
        writer.endObject();
    }


    private void fillRecycler() {
    }

    private void testRecycler() {
        activityLogs.add(new ActivityLog("March 31st", "Running", "Lots of hills", 61));
        activityLogs.add(new ActivityLog("April 3rd", "Hike", "Took my dog for a walk", 476));
        activityLogs.add(new ActivityLog("April 14th", "Walking", "Had nothing better to do", 13));
        activityLogs.add(new ActivityLog("April 16th", "Weights", "Went to the gym with a friend", 48));
        activityLogs.add(new ActivityLog("April 20th", "Running", "Lots of hills", 90));
        waterLogs.add(new WaterLog("April 3rd", 8, true));
        waterLogs.add(new WaterLog("April 14th", 7, true));
        waterLogs.add(new WaterLog("April 16th", 9, true));
        waterLogs.add(new WaterLog("April 20th", 2, false));
    }
    private void testGoals() {
        waterGoal = 8;
        waterTotal = 2;
        activityGoal = 30;
        activityTotal = 15;
        //will have to check the list if there is an entry for today:
                //if there is an entry: add up the totals for display
                //if there is NOT an entry: make a new one set everything to 0
    }

    private void setViews() {
        tvWater = findViewById(R.id.ah_WaterGoal);
        tvActivity = findViewById(R.id.ah_ActvivtyGoal);
        tvMinutes = findViewById(R.id.ah_Minutes);
        tvStart = findViewById(R.id.ah_Start);
        tvEnd = findViewById(R.id.ah_End);
        tvKeyword = findViewById(R.id.ah_Keyword);

        spHistory = findViewById(R.id.ah_Spinner);
        spinnerList.add("Activity");
        spinnerList.add("Water");
        showActivity = true;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, spinnerList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spHistory.setAdapter(arrayAdapter);
        spHistory.setOnItemSelectedListener(this);


        addWater = findViewById(R.id.ah_AddWater);
        addActivity = findViewById(R.id.ah_AddActivity);
        clearButton = findViewById(R.id.ah_Clear);
        searchButton = findViewById(R.id.ah_Search);

        recyclerView = findViewById(R.id.ah_Rec);
        //set spinner to default to activity


    }

    public void setRec() {
        Log.d(TAG, "setRec: Start");
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        if (showActivity) {
            mAdapter = new HealthAdapter(activityLogs, this, true);
        } else {
            mAdapter = new HealthAdapter(waterLogs, this);
        }
        recyclerView.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), 0);
        recyclerView.addItemDecoration(dividerItemDecoration);
        Log.d(TAG, "setRec: Finish");
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String value = (String) adapterView.getItemAtPosition(i);
        Log.d(TAG, "onItemSelected: " + value);
        if (value.equalsIgnoreCase("Activity")) {
            Log.d(TAG, "onItemSelected: act");
            showActivity = true;
            setRec();
        } else if (value.equalsIgnoreCase("Water")) {
            Log.d(TAG, "onItemSelected: water");
            showActivity = false;
            //updatelist
            setRec();
        }
    }

    public void addActivity(View view) {
        Intent aintent = new Intent(this, HealthCreateActivity.class);
        startActivity(aintent);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
