package com.example.mediccompanion.introScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mediccompanion.R;
import com.example.mediccompanion.activities.Home;

public class IntroScreen extends AppCompatActivity {
    //declaring view
    private ImageView intro_image;
    private TextView summary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);
        //referencing views
        intro_image = findViewById(R.id.intro_image);
        summary = findViewById(R.id.summary);
        //adding animation by first referencinh to animation class
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.launcher_screen);
        //adding animation to our image and summary text label
        intro_image.startAnimation(animation);
        summary.startAnimation(animation);
        final Intent intent = new Intent(IntroScreen.this, Home.class);
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();
    }
}
