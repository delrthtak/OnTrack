package com.example.ontrack;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class CreateActivity extends AppCompatActivity {

    public static final String TAG = "CreateActivity";

    private TextView name;
    private TextView date;
    private TextView className;
    private TextView notes;
    private TextView title;

    private RadioGroup radio;
    private RadioButton low;
    private RadioButton medium;
    private RadioButton high;

    private List<Assignment> assignmentList = MainActivity.getList();

    private Boolean edit = false;
    private Integer pos = 0;
    private Boolean save = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        Button submit = findViewById(R.id.button_AddAssignment);

        name = findViewById(R.id.edit_Name);
        date = findViewById(R.id.edit_Date);
        className = findViewById(R.id.edit_Class);
        notes = findViewById(R.id.edit_Notes);
        title = findViewById(R.id.text_NewAssignment);

        radio = findViewById(R.id.rad_priority);
        low = findViewById(R.id.rad_Low);
        medium = findViewById(R.id.rad_Medium);
        high = findViewById(R.id.rad_High);
        notes.setMovementMethod(new ScrollingMovementMethod());

        //This is for editting Assignments
        if (getIntent().hasExtra("Name")) {
            String n = getIntent().getStringExtra("Name");
            String d = getIntent().getStringExtra("Date");
            String c = getIntent().getStringExtra("Class");
            String a = getIntent().getStringExtra("Notes");
            int r = getIntent().getIntExtra("Priority", 0);
            name.setText(n);
            date.setText(d);
            className.setText(c);
            notes.setText(a);
            //set priority
            if (r == 1) { low.setChecked(true);
            } else if (r == 2) { medium.setChecked(true);
            } else if (r == 3) { high.setChecked(true); }
            edit = true;
            title.setText(R.string.editAssignment);
            submit.setText(R.string.editAssignment);
            pos = getIntent().getIntExtra("Pos", 0);
        } else {
            edit = false;
        }
        if (!edit) {
            title.setText(R.string.new_assignment);
            submit.setText(R.string.add_assignment);
        }
    }



    @Override
    protected void onPause() {
        if (save)
            saveProduct();
        super.onPause();
    }

    public void onClick(View view) {
        saveProduct();
    }


    private void saveProduct() {
        Log.d(TAG, "saveProduct: Method Starting");
        if (name.getText().toString().trim().isEmpty() ) {
            Toast.makeText(this, "Cannot save assignment without a name", Toast.LENGTH_LONG).show();
            save = false;
            return;
        } else {
            Log.d(TAG, "saveProduct: new assignment");
            Assignment bruh = new Assignment();
            try {
                bruh.setName(name.getText().toString());
                bruh.setDueDate(date.getText().toString());
                bruh.setClassName(className.getText().toString());
                bruh.setNotes(notes.getText().toString());
                if (low.isChecked()) {
                    bruh.setPriority(1);
                } else if (medium.isChecked()) {
                    bruh.setPriority(2);
                } else if (high.isChecked()) {
                    bruh.setPriority(3);
                } else {
                    bruh.setPriority(0);
                }
                if (edit) {
                    int boi = pos;
                    assignmentList.remove(boi);
                }
                assignmentList.add(0, bruh);
                Log.d(TAG, "saveProduct: add assignment to list");
                writeJsonStream(assignmentList);
                Log.d(TAG, "saveProduct: write assignment list");
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
        Log.d(TAG, "onSaveProduct: Method Starting");
        Intent aintent = new Intent(this, AssignmentActivity.class);
        startActivity(aintent);
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning");
        builder.setMessage("Your assignment has not been saved, would you like to save?");

        builder.setNegativeButton("No", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CreateActivity.super.onBackPressed();
            }
        });
        builder.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save = true;//saveProduct();//save function
                CreateActivity.super.onBackPressed();
            }
        });
        builder.create();
        builder.show();
    }


    public void writeJsonStream(List<Assignment> its) throws IOException {
        Log.d(TAG, "writeJsonStream: method starting");
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(openFileOutput(String.valueOf(R.string.file_name), Context.MODE_PRIVATE)));
        //JsonWriter writer = new JsonWriter(new OutputStreamWriter(openFileOutput(String.valueOf(R.string.file_name), Context.MODE_PRIVATE)));
        writer.setIndent("  ");
        writeNotesArray(writer, its);
        writer.close();
        Log.d(TAG, "writeJsonStream: method complete");
    }

    public void writeNotesArray(JsonWriter writer, List<Assignment> its) throws IOException {
        writer.beginArray();
        for (Assignment it : its) {
            writeNote(writer, it);
        }
        writer.endArray();
    }

    public void writeNote(JsonWriter writer, Assignment its) throws IOException {
        writer.beginObject();
        writer.name("Name").value(its.getName());
        writer.name("Date").value(its.getDueDate());
        writer.name("Class").value(its.getClassName());
        writer.name("Notes").value(its.getNotes());
        writer.name("Priority").value(its.getPriority());
        writer.endObject();
    }



}
