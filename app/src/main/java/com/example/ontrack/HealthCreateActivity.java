package com.example.ontrack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class HealthCreateActivity extends AppCompatActivity  {

    public static final String TAG = "HealthCreateActivity";

    private TextView name;
    private TextView date;
    private TextView time;
    private TextView notes;
    private TextView title;
    private Button submit;

    private List<ActivityLog> activityLogs;
    private List<Health> healthList;

    private Boolean edit = false;
    private Integer pos = 0;
    private Boolean save = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);

        activityLogs = HealthActivity.getActivityLogs();
        healthList = HealthActivity.getHealthList();

        submit = findViewById(R.id.button_AddAssignment);

        name = findViewById(R.id.aa_Name);
        date = findViewById(R.id.aa_Date);
        time = findViewById(R.id.aa_Time);
        notes = findViewById(R.id.aa_ActivityNotes);
        submit = findViewById(R.id.aa_AddAssignment);
        title = findViewById(R.id.aa_NewActivity);

        //This is for editting Assignments
        if (getIntent().hasExtra("Name")) {
            String n = getIntent().getStringExtra("Name");
            String d = getIntent().getStringExtra("Date");
            String c = getIntent().getStringExtra("time");
            String a = getIntent().getStringExtra("Notes");
            int r = getIntent().getIntExtra("Priority", 0);
            name.setText(n);
            date.setText(d);
            time.setText(c);
            notes.setText(a);

            edit = true;
            title.setText(R.string.edit_activity);
            submit.setText(R.string.edit_activity);
            pos = getIntent().getIntExtra("Pos", 0);
        } else {
            edit = false;
        }
        if (!edit) {
            title.setText(R.string.new_activity);
            submit.setText(R.string.add_activity);
        }
    }

    private void saveActivity() {
        Log.d(TAG, "saveActivity: Method Starting");
        if (name.getText().toString().trim().isEmpty() ) {
            Toast.makeText(this, "Cannot save activity without a name", Toast.LENGTH_LONG).show();
            save = false;
            return;
        } else {
            Log.d(TAG, "saveActivity: new assignment");
            ActivityLog bruh = new ActivityLog();
            try {
                bruh.setActivityName(name.getText().toString());
                bruh.setDate(date.getText().toString());
                bruh.setMinutes(time.getText().toString());
                bruh.setUserNotes(notes.getText().toString());
                if (edit) {
                    int boi = pos;
                    activityLogs.remove(boi);
                    //something probably needs to be done here
                }
                activityLogs.add(0, bruh);
                for (Health health : healthList) {
                    if (findHealthForActivity(health, date.getText().toString())) {
                        health.addActivityLog(bruh);
                        Log.d(TAG, "saveActivity: added to " + date.getText().toString());
                        break;
                    }
                }
                Log.d(TAG, "saveActivity: add assignment to list");
                writeJsonStream(healthList);
                Log.d(TAG, "saveActivity: write assignment list");
            } catch (FileNotFoundException e) {
                Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
                try {
                    InputStream inputStream = getApplicationContext().openFileInput(String.valueOf(R.string.file_name));
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "saveActivity: Method Starting");
        Intent aintent = new Intent(this, AssignmentActivity.class);
        startActivity(aintent);
    }

    private boolean findHealthForActivity(Health health, String toString) {
        return toString.equals(health.getHealthDate());
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

    public void onClick(View view) {
        saveActivity();
    }

}
