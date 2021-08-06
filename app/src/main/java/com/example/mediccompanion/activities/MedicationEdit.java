package com.example.mediccompanion.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mediccompanion.PI;
import com.example.mediccompanion.R;
import com.example.mediccompanion.Validation.FieldsValidation;
import com.example.mediccompanion.adapters.DbAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;


public class MedicationEdit extends AppCompatActivity {
    Spinner type;
    EditText name;
    EditText quantity;
    EditText amount_per_dosage;
    EditText number_of_dosages;
    String typeOfDrug, status;
    TextView title;
    DbAdapter dbAdapter;
    int rowId;
    Bundle bundle;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);


        dbAdapter = new DbAdapter(this);

        type = findViewById(R.id.type);
        name = findViewById(R.id.drug_name);
        quantity = findViewById(R.id.quantity);
        amount_per_dosage = findViewById(R.id.drug_amount);
        number_of_dosages = findViewById(R.id.number_of_dosages);
        title = findViewById(R.id.textView3);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStartup = preferences.getBoolean("firstStart", true);

        if (firstStartup){
            showStartDialog();
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        String[] medtype;

        bundle = getIntent().getExtras();
        status = bundle.getString("status");
        if (status.equalsIgnoreCase("occupied")) {
            rowId = bundle.getInt("rowId");
            name.setText(bundle.getString("name"));
            quantity.setText(bundle.getString("quantity"));
            amount_per_dosage.setText(bundle.getString("amount"));
            number_of_dosages.setText(bundle.getString("dosages"));
            title.setText("Medication Edit");
        }else {
            name.setText("");
            quantity.setText("");
            amount_per_dosage.setText("");
            number_of_dosages.setText("");
        }


        Resources resources = getResources();
        medtype = resources.getStringArray(R.array.medication_types);
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item, medtype);
        type.setAdapter(adapter);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                typeOfDrug = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void showStartDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Welcome to Medic Companion")
                .setMessage("This is where you are required to edit the details of your new medication.")
                .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences preferences1 = getSharedPreferences("preferences", MODE_PRIVATE);
                        boolean firstStartup1 = preferences1.getBoolean("first", true);

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
                .setMessage("Enjoy using medic companion and keep watch for updates.")
                .setPositiveButton("FINISH", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
        SharedPreferences prefs = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("first", false);
        editor.apply();
    }

    public void onSave(View view) {
        String nameValue, quantityStr, amountStr, dosagesStr;
        nameValue = name.getText().toString();
        quantityStr = quantity.getText().toString();
        amountStr = amount_per_dosage.getText().toString();
        dosagesStr = number_of_dosages.getText().toString();

        FieldsValidation fieldsValidation = new FieldsValidation(name, quantity, amount_per_dosage, number_of_dosages);
        if (fieldsValidation.invalidRegistrationInputFields(nameValue, quantityStr, amountStr, dosagesStr))return;
        if (type.getSelectedItemPosition() == 0) {
            Snackbar.make(type, "Please select the type of drug", Snackbar.LENGTH_LONG).show();
            return;
        }
        int quantityValue = Integer.parseInt(quantityStr);
        int amountValue = Integer.parseInt(amountStr);
        int numberOfDosages = Integer.parseInt(dosagesStr);


        String status = bundle.getString("status");


        dbAdapter.open();
        if (status.equalsIgnoreCase("empty")){
            dbAdapter.insertData(nameValue,typeOfDrug, quantityValue, amountValue, numberOfDosages);
        }else {
            Cursor cursor = dbAdapter.getData(rowId + 1);
            if (cursor.moveToFirst()) {
                dbAdapter.update(rowId + 1, nameValue, typeOfDrug, quantityValue, amountValue, numberOfDosages);
            } else {
                dbAdapter.insertData(nameValue, typeOfDrug, quantityValue, amountValue, numberOfDosages);
            }
        }
        dbAdapter.close();

        name.setText("");
        quantity.setText("");
        amount_per_dosage.setText("");
        number_of_dosages.setText("");

        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
        finish();
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
