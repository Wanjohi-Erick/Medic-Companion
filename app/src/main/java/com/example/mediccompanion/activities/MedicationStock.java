package com.example.mediccompanion.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.mediccompanion.Medications;
import com.example.mediccompanion.R;
import com.example.mediccompanion.adapters.DbAdapter;
import com.example.mediccompanion.adapters.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MedicationStock extends AppCompatActivity {
    RecyclerView meds;
    RecyclerViewAdapter recyclerViewAdapter;
    List<Medications> medicationsArrayList = new ArrayList<>();
    DbAdapter dbAdapter = new DbAdapter(this);

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_stock);
        Toolbar toolbar = findViewById(R.id.toolbar);
        meds = findViewById(R.id.meds);
        setSupportActionBar(toolbar);
        SharedPreferences preferences = getSharedPreferences("prefsStock", MODE_PRIVATE);
        boolean firstStartup = preferences.getBoolean("firstStartStock", true);

        if (firstStartup) {
            showStartUpDialog();
        }

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

            FloatingActionButton add = findViewById(R.id.add);
            FloatingActionButton find_store = findViewById(R.id.find);
            find_store.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent_to_map = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(intent_to_map);
                }
            });
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), MedicationEdit.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("status", "empty");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

            //creating the stock

            dbAdapter.open();
            Cursor cursor = dbAdapter.getAllMedications();
            if (cursor.moveToFirst()) {
                do {
                    String medName = cursor.getString(1);
                    String medType = cursor.getString(2);
                    String medQuantity = cursor.getString(3);
                    String medDosages = cursor.getString(5);
                    String medAmountPerDose = cursor.getString(4);
                    Medications medications = new Medications(medName, medType, medQuantity, medAmountPerDose, medDosages);
                    medicationsArrayList.add(medications);
                } while (cursor.moveToNext());
            }
            dbAdapter.close();
            recyclerViewAdapter = new RecyclerViewAdapter(medicationsArrayList);
            meds.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            meds.setHasFixedSize(true);
            meds.setAdapter(recyclerViewAdapter);
            meds.addItemDecoration(dividerItemDecoration);



    }

    private void showStartUpDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Welcome to Medic Companion")
                .setMessage("This is where you manage your medications. \n Click on the bottom-left button to view close pharmacies or hospitals." +
                        "\nClick on the bottom-right button to add a new medication to your list.")
                .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), MedicationEdit.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("status", "empty");
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                }).create().show();
        SharedPreferences prefs = getSharedPreferences("prefsStock", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStartStock", false);
        editor.apply();
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

        if (id == R.id.action_home) {
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_history) {
            Intent intent = new Intent(this, History.class);
            startActivity(intent);
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
