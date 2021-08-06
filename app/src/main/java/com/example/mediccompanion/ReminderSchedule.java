package com.example.mediccompanion;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import com.example.mediccompanion.adapters.DbAdapter;
import com.example.mediccompanion.dialogs.TimePickerDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReminderSchedule extends AppCompatActivity {

    private TextView wake, sleep;
    private String wakeTime, sleepTime, wakeTimeDB, sleepTimeDB;
    private int hour, minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_schedule);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*getTime();



         */
    }

    private void getTime() {
        DbAdapter dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        Cursor cursor = dbAdapter.getTime(1);
        if (cursor.moveToFirst()){
            retrieve(cursor);
        }
    }

    private void retrieve(Cursor cursor) {
        wakeTimeDB = cursor.getString(1);
        sleepTimeDB = cursor.getString(2);
        wake.setText(wakeTimeDB);
        sleep.setText(sleepTimeDB);
    }

    public void saveSleepTime(View view) {
        wakeTime = wake.getText().toString();
        sleepTime = sleep.getText().toString();

        DbAdapter dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        dbAdapter.updateTime(1, wakeTime, sleepTime);
        dbAdapter.close();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void openTimePicker(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.time_picker);
        TimePicker timePicker = dialog.findViewById(R.id.timePicker);
        Button cancel = dialog.findViewById(R.id.cancel);
        Button save = dialog.findViewById(R.id.save);
        wake = findViewById(R.id.wakeEdit);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                hour = i;
                minute = i1;
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = String.format(Locale.getDefault(),"%02d:%02d", hour, minute);
                wake.setText(time);
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void openTimePicker1(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.time_picker);
        TimePicker timePicker = dialog.findViewById(R.id.timePicker);
        Button cancel = dialog.findViewById(R.id.cancel);
        Button save = dialog.findViewById(R.id.save);
        sleep = findViewById(R.id.sleepEdit);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                hour = i;
                minute = i1;
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = String.format(Locale.getDefault(),"%02d:%02d", hour, minute);
                sleep.setText(time);
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
