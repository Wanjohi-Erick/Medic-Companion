package com.example.mediccompanion.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediccompanion.Medications;
import com.example.mediccompanion.R;
import com.example.mediccompanion.adapters.DbAdapter;
import com.example.mediccompanion.adapters.HomeRecyclerAdapter;
import com.example.mediccompanion.services.ForegroundService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity {

    ProgressBar progressBar;
    TextView drugName;
    @SuppressLint("StaticFieldLeak")
    //public static TextView timeView;
    DbAdapter dbAdapter;
    private String amountFromDB, nameFromDb, amountPerDose, dosagesPerDay;
    int progress;
    String currentTime;
    String progressVal;
    Date date;
    int previousDay;
    String dateVal;

    RecyclerView recyclerView;
    HomeRecyclerAdapter homeRecyclerAdapter;
    List<Medications> medsToTake = new ArrayList<>();
    private static final String TAG = "Home";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        drugName = findViewById(R.id.drugName);
        progressBar = findViewById(R.id.progressBar);
        dbAdapter = new DbAdapter(this);
        currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        date = Calendar.getInstance().getTime();
        dateVal = date.toString();

        recyclerView = findViewById(R.id.medicationsToTakeRecycler);

        SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStartup = preferences.getBoolean("firstStart", true);

        if (firstStartup){
            showStartDialog();
        }else {
            getData();

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }

    }

    private void showStartDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Welcome to Medic Companion")
                .setMessage("Your ultimate companion for your medical needs")
                .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences preferences1 = getSharedPreferences("prefs1", MODE_PRIVATE);
                        boolean firstStartup1 = preferences1.getBoolean("firstStart1", true);

                        if (firstStartup1){
                            nextDialog();
                        }
                    }
                }).create().show();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    private void nextDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Welcome to Medic Companion")
                .setMessage("This is where all your recorded medications appear. \nClick on the medication you want to take.")
                .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), MedicationStock.class);
                        startActivity(intent);
                        finish();
                    }
                }).create().show();
        SharedPreferences prefs = getSharedPreferences("prefs1", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart1", false);
        editor.apply();
    }

    private void getDate() {
        dbAdapter.open();
        Cursor cursor = dbAdapter.getDate(1);
        if (cursor.moveToFirst()){
            getDateValue(cursor);
        }
    }

    private void getDateValue(Cursor cursor) {
        String dayy = cursor.getString(1);
        previousDay = Integer.parseInt(dayy);
        //Toast.makeText(this, "prev" + previousDay, Toast.LENGTH_SHORT).show();
    }

    private void setProgDialog() {
        int dosagesPerDayInt = Integer.parseInt(dosagesPerDay);
        progressBar.setMax(dosagesPerDayInt);
        int day = Calendar.getInstance().getTime().getDate();
        if (day > previousDay || day < previousDay && day == 1){
            progress = 0;
            progressBar.setProgress(progress);
            updateDateDb(day);
            updateProgressDb(progress);
        }else if (progressBar.getProgress() < progressBar.getMax()){
            progressBar.setProgress(progress);
        }
    }

    private void updateDateDb(int day) {
        dbAdapter.open();
        dbAdapter.updateDate(1, day);
        dbAdapter.close();
    }

    private void updateProgressDb(int progress) {
        dbAdapter.open();
        dbAdapter.updateProgress(1,progress);
        dbAdapter.close();
    }


    private void getDataFromProgress() {
        dbAdapter.open();
        Cursor cursor = dbAdapter.getProgress(1);
        if (cursor.moveToFirst()){
            readProgress(cursor);
        }
    }

    private void readProgress(Cursor cursor) {
        progressVal = cursor.getString(1);
        progress = Integer.parseInt(progressVal);
        //Toast.makeText(this, progress, Toast.LENGTH_SHORT).show();
    }

    private void progress() {
        if (progressBar.getProgress() < progressBar.getMax()) {
            progressBar.incrementProgressBy(1);
            int newAmount = Integer.parseInt(amountFromDB);
            int amountPerDosage = Integer.parseInt(amountPerDose);
            newAmount = newAmount - amountPerDosage;
            progress++;
            updateDb(newAmount, progress);


            Intent intent = new Intent(this, ForegroundService.class);
            ContextCompat.startForegroundService(this, intent);
        }else{
            Toast.makeText(this, "Max reached", Toast.LENGTH_SHORT).show();
        }
    }



    private void updateDb(int newAmount, int progress) {
        dbAdapter.open();
        dbAdapter.updateAmount(1, newAmount);
        dbAdapter.updateProgress(1, progress);
        dbAdapter.close();
    }

    private void getData() {
        dbAdapter.open();
        Cursor cursor = dbAdapter.getAllMedications();
        if (cursor.moveToFirst()){
            do {
                nameFromDb = cursor.getString(1);
                String medType = cursor.getString(2);
                amountFromDB = cursor.getString(3);
                amountPerDose = cursor.getString(4);
                dosagesPerDay = cursor.getString(5);
                Medications medications = new Medications(nameFromDb, medType, amountFromDB, amountPerDose, dosagesPerDay);
                medsToTake.add(medications);
                Log.i(TAG, "getData: "+ nameFromDb);

            } while (cursor.moveToNext());
            dbAdapter.close();
            homeRecyclerAdapter = new HomeRecyclerAdapter(medsToTake);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(homeRecyclerAdapter);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_history) {
            Intent intent = new Intent(this, History.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_settings){
            Intent i = new Intent(this, MedicationStock.class);
            startActivity(i);
            return true;
        }else if (id == R.id.action_about){
            Intent intent = new Intent(this, Help.class);
            startActivity(intent);
        }else if(id == R.id.action_help){
            Intent intent = new Intent(this, Help.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


}
