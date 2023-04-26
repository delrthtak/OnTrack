package com.example.ontrack;

import android.os.Bundle;
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

import java.util.ArrayList;
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
    private List<ActivityLog> activityLogs;
    List<String> spinnerList = new ArrayList<>();

    private boolean showActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        waterLogs = new ArrayList<>();
        activityLogs = new ArrayList<>();
        testRecycler();
        setViews();
        setRec();
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

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
