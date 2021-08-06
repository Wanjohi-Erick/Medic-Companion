package com.example.mediccompanion;

import android.content.res.Resources;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class PI extends AppCompatActivity {
    Spinner genderspinner, allergiesspinner, languagespinner;
    String[] gender, allergies, languages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pi);
        genderspinner = findViewById(R.id.spinner2);
        allergiesspinner = findViewById(R.id.allergies);
        languagespinner = findViewById(R.id.language);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Resources resources = getResources();
        gender = resources.getStringArray(R.array.Gender);
        allergies = resources.getStringArray(R.array.allergies);
        languages = resources.getStringArray(R.array.languages);

        ArrayAdapter genderadapt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gender);
        ArrayAdapter allergyadapt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, allergies);
        ArrayAdapter langadapt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, languages);
        allergiesspinner.setAdapter(allergyadapt);
        genderspinner.setAdapter(genderadapt);
        languagespinner.setAdapter(langadapt);
    }

}
