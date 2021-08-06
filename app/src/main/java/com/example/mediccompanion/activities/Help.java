package com.example.mediccompanion.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.mediccompanion.PI;
import com.example.mediccompanion.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class Help extends AppCompatActivity {
    String url = "http://www.mediccompanion.wordpress.com/about";
    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences preferences = getSharedPreferences("prefsHelp", MODE_PRIVATE);
        boolean firstStartup = preferences.getBoolean("firstStartHelp", true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (firstStartup){
            showStartDialog();
        }
        progressBar = findViewById(R.id.progressBar2);
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView webView, int progress){
                if (progress < 100 && progressBar.getVisibility() == ProgressBar.GONE){
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                }
                progressBar.setProgress(progress);
                if (progress == 100){
                    progressBar.setVisibility(ProgressBar.GONE);
                }
            }
        });
    }
    private void showStartDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Welcome to Medic Companion")
                .setMessage("Click on the help button on the menu for help")
                .setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
        SharedPreferences prefs = getSharedPreferences("prefsHelp", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStartHelp", false);
        editor.apply();
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
        }else if (id == R.id.action_home){
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}