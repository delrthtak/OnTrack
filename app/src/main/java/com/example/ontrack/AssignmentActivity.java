package com.example.ontrack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AssignmentActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    public static final String TAG = "AssignmentActivity";

    private Button bSearchDue;
    private Button bCreateNew;
    private Button bSearchKey;

    private TextView tSearchKey;
    private TextView tSearchDue;

    private RadioGroup gDate;
    private RadioButton rEarly;
    private RadioButton rLate;
    private RadioGroup gPriority;
    private RadioButton rHigh;
    private RadioButton rLow;

    private RecyclerView rvUpcoming;

    public static List<Assignment> items = new ArrayList<>();

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutMan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Starting");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments);
        
        assignVariables();
        Log.d(TAG, "onCreate: assignVariables complete");
        loadFile();
        Log.d(TAG, "onCreate: loadFile complete");

    }

    private void assignVariables() {
        Log.d(TAG, "assignVariables: Method Starting");
        bSearchDue = findViewById(R.id.button_SearchDue);
        bCreateNew = findViewById(R.id.button_CreateNew);
        bSearchKey = findViewById(R.id.button_SearchKey);

        tSearchKey = findViewById(R.id.text_SearchKey);
        tSearchDue = findViewById(R.id.text_SearchDue);

        gDate = findViewById(R.id.rad_Date);
        rEarly = findViewById(R.id.rad_Earliest);
        rLate = findViewById(R.id.rad_Latest);
        gPriority = findViewById(R.id.rad_Priority);
        rHigh = findViewById(R.id.rad_Highest);
        rLow = findViewById(R.id.rad_Lowest);

        rvUpcoming = findViewById(R.id.rec_Filtered);

        Log.d(TAG, "assignVariables: findViewById complete");

        rvUpcoming.setHasFixedSize(true);

        layoutMan = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvUpcoming.setLayoutManager(layoutMan);

        mAdapter = new MainAssAdapter(this, items);
        rvUpcoming.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvUpcoming.getContext(), 1);

        rvUpcoming.addItemDecoration(dividerItemDecoration);
        Log.d(TAG, "assignVariables: Method complete");
    }
    private void loadFile() {
        Log.d(TAG, "loadFile: Loading JSON file");
        try {
            Log.d(TAG, "loadFile: start of try");
            //Toast.makeText(this, getString(R.string.loading), Toast.LENGTH_SHORT).show();
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            Log.d(TAG, "loadFile: found json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            writeJsonStream(items);
            Log.d(TAG, "loadFile: end of Try");
        } catch (FileNotFoundException e) {
            Log.d(TAG, "loadFile: try incomplete");
            try {
                InputStream inputStream = getApplicationContext().openFileInput(String.valueOf(R.string.file_name));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
        Log.d(TAG, "loadFile: method complete");
    }

    public void writeJsonStream(List<Assignment> its) throws IOException {
        Log.d(TAG, "writeJsonStream: Method Starting");
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(openFileOutput(String.valueOf(R.string.file_name), Context.MODE_PRIVATE)));
        writer.setIndent("  ");
        writeAssArray(writer, its);
        writer.close();
        Log.d(TAG, "writeJsonStream: Method Complete");
    }

    public void writeAssArray(JsonWriter writer, List<Assignment> its) throws IOException {
        Log.d(TAG, "writeAssArray: Method Starting");
        writer.beginArray();
        for (Assignment it : its) {
            writeNote(writer, it);
        }
        writer.endArray();
        Log.d(TAG, "writeAssArray: Method Complete");
    }

    public void writeNote(JsonWriter writer, Assignment its) throws IOException {
        Log.d(TAG, "writeNote: Method Starting");
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
        Log.d(TAG, "writeNote: Method Complete");
    }

    public void onPressed(View view) {
        Log.d(TAG, "onPressed: Method Starting");
        Intent aintent = new Intent(this, CreateActivity.class);
        startActivity(aintent);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }
}
