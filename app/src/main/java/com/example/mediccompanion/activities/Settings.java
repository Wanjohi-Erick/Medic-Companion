package com.example.mediccompanion.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mediccompanion.PI;
import com.example.mediccompanion.R;
import com.example.mediccompanion.ReminderSchedule;


public class Settings extends AppCompatActivity {
    ActionBar actionBar;
    Toolbar toolbar;
    ListView settings;

    String[] settingsprefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        toolbar = findViewById(R.id.toolbar);
        settings = findViewById(R.id.settings_preferences);

        settingsEdit();

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    //creating the settings preferences list
    private void settingsEdit() {
        Resources resources = getResources();
        settingsprefs = resources.getStringArray(R.array.set_list);

        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item, settingsprefs);
        settings.setAdapter(adapter);

        //modifying the settings preference list
        settings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0){
                    Intent intent = new Intent(Settings.this, MedicationStock.class);
                    startActivity(intent);
                }else if (i == 1){
                    Intent intent = new Intent(Settings.this, ReminderSchedule.class);
                    startActivity(intent);
                }else if (i == 2) {

                }else if (i == 3) {

                }else if (i == 4) {

                }else if (i == 5) {
                    Intent intent = new Intent(getApplicationContext(), PI.class);
                    startActivity(intent);
                }else if (i == 6) {
                    Intent to_map_intent = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(to_map_intent);
                }else if (i == 7) {

                }else if (i == 8) {

                }else if (i == 9) {

                }else if (i == 10) {

                }else if (i == 11) {
                    triggerRebirth(getApplicationContext());
                }
            }
        });

    }

    public static void triggerRebirth(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        assert intent != null;
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
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
        } else if (id == R.id.action_settings) {

        } else if (id == R.id.action_about) {
            Intent intent = new Intent(this, PI.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.action_help) {
            Intent intent = new Intent(this, Help.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
