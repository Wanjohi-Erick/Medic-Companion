package com.example.mediccompanion.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.mediccompanion.R;
import com.example.mediccompanion.adapters.DbAdapter;
import com.example.mediccompanion.services.ForegroundService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MedicationTake extends AppCompatActivity {

    ProgressBar progressBar;
    TextView drugName;
    @SuppressLint("StaticFieldLeak")
    //public static TextView timeView;
            DbAdapter dbAdapter;
    private String amountFromDB,type, nameFromDb, amountPerDose, dosagesPerDay;
    int progress;
    String currentTime;
    String progressVal;
    Date date;
    int previousDay;
    String dateVal;
    Bundle bundle;
    int rowId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_take);
        drugName = findViewById(R.id.drugName);
        progressBar = findViewById(R.id.progressBar);
        dbAdapter = new DbAdapter(this);
        currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        date = Calendar.getInstance().getTime();
        dateVal = date.toString();
        bundle = getIntent().getExtras();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            getDate();
            getData();
            getDataFromProgress();
            setProgDialog();
            drugName.setText(nameFromDb);
            //timeView = findViewById(R.id.timeView);
            //timeView.setText("--:--");
            FloatingActionButton fab = findViewById(R.id.fab);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progress(view);
                }
            });

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
        dbAdapter.updateDate(rowId + 1, day);
        dbAdapter.close();
    }

    private void updateProgressDb(int progress) {
        dbAdapter.open();
        dbAdapter.updateProgress(rowId + 1,progress);
        dbAdapter.close();
    }


    private void getDataFromProgress() {
        dbAdapter.open();
        Cursor cursor = dbAdapter.getProgress(rowId +1);
        if (cursor.moveToFirst()){
            progressVal = cursor.getString(1);
            progress = Integer.parseInt(progressVal);
        }else {
            dbAdapter.open();
            dbAdapter.insertProgress(0);
            int day = Calendar.getInstance().getTime().getDate();
            dbAdapter.insertDate(day);
            dbAdapter.close();
        }
    }

    private void progress(View view) {
        if (progressBar.getProgress() < progressBar.getMax()) {
            progressBar.incrementProgressBy(1);
            int newAmount = Integer.parseInt(amountFromDB);
            int amountPerDosage = Integer.parseInt(amountPerDose);
            newAmount = newAmount - amountPerDosage;
            progress++;
            updateDb(newAmount, progress);

            Snackbar.make(view, "Dosage confirmed", Snackbar.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ForegroundService.class);
            ContextCompat.startForegroundService(this, intent);
        }else{
            //Toast.makeText(this, "Max reached", Toast.LENGTH_SHORT).show();
            Snackbar.make(view, "Today's maximum dosages reached", Snackbar.LENGTH_SHORT).show();
        }
    }



    private void updateDb(int newAmount, int progress) {
        dbAdapter.open();
        dbAdapter.updateAmount(rowId + 1, newAmount);
        dbAdapter.updateProgress(rowId +1, progress);
        dbAdapter.close();
    }

    private void getData() {
        amountFromDB = bundle.getString("quantity");
        nameFromDb = bundle.getString("drugName");
        type = bundle.getString("type");
        amountPerDose = bundle.getString("amount");
        dosagesPerDay = bundle.getString("dosages");
        rowId = bundle.getInt("drugId");
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
        if (id == R.id.action_home) {
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
            finish();
        }
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
