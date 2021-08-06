package com.example.mediccompanion.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.example.mediccompanion.PI;
import com.example.mediccompanion.R;
import com.example.mediccompanion.adapters.DbAdapter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

public class MedicationView extends AppCompatActivity {
    TextView drugNameView;
    TextView typeView;
    TextView quantityView;
    TextView drugAmountView;
    TextView numberOfDosagesView;
    DbAdapter dbAdapter;
    String name, type, quantity, amount, dosages;
    int rowId;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_view);
        dbAdapter = new DbAdapter(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        drugNameView = findViewById(R.id.drugNameView);
        typeView = findViewById(R.id.typeView);
        quantityView = findViewById(R.id.quantityView);
        drugAmountView = findViewById(R.id.drugAmountView);
        numberOfDosagesView = findViewById(R.id.number_of_dosagesView);

        setSupportActionBar(toolbar);
        SharedPreferences preferencesMed = getSharedPreferences("prefsMed", MODE_PRIVATE);
        boolean firstStartupMed = preferencesMed.getBoolean("firstStartMed", true);

        if (firstStartupMed){
            showStartDialog();
        }else {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

            getData();
        }
    }

    private void showStartDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Welcome to Medic Companion")
                .setMessage("View the details of your medication here")
                .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
        SharedPreferences prefsMed = getSharedPreferences("prefsMed", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsMed.edit();
        editor.putBoolean("firstStartMed", false);
        editor.apply();
    }
    private void getData() {
        Bundle bundle = getIntent().getExtras();
        rowId = bundle.getInt("rowId");
        name = bundle.getString("drugName");
        type = bundle.getString("type");
        quantity = bundle.getString("quantity");
        amount = bundle.getString("amount");
        dosages = bundle.getString("dosages");
        drugNameView.setText(name);
        typeView.setText(type);
        quantityView.setText(quantity);
        drugAmountView.setText(amount);
        numberOfDosagesView.setText(dosages);

    }

    public void editDetails(View view) {
        Intent intent = new Intent(MedicationView.this, MedicationEdit.class);
        Bundle bundle = new Bundle();
        bundle.putString("status", "occupied");
        bundle.putInt("rowId", rowId);
        bundle.putString("name", name);
        bundle.putString("quantity", quantity);
        bundle.putString("amount", amount);
        bundle.putString("dosages", dosages);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            finish();
            return true;
        }else if (id == R.id.action_settings){
            Intent i = new Intent(this, MedicationStock.class);
            startActivity(i);
            finish();
            return true;
        }else if (id == R.id.action_about){
            Intent intent = new Intent(this, Help.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.action_help){
            Intent intent = new Intent(this, Help.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
