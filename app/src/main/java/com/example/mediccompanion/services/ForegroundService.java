package com.example.mediccompanion.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.mediccompanion.activities.Home;
import com.example.mediccompanion.R;

import java.util.Locale;

import static com.example.mediccompanion.App.CHANNEL_ID;
//import static com.example.mediccompanion.activities.Home.timeView;

public class ForegroundService extends Service {
    int i;
    public static final long timeIntervalSeconds = (16/3)*60*60;
    int hoursLeftInCounter, minutesLeftInCounter, secondsLeftInCounter;
    @Override
    public void onCreate() {
        super.onCreate();
        updateCountDownTimerText();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (i = 0; i < timeIntervalSeconds; i++){
                    try {
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Intent notificationIntent = new Intent(getApplicationContext(), Home.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
                Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setContentTitle("Dosage Reminder")
                        .setContentText("Take dosage now")
                        .setSmallIcon(R.drawable.messages)
                        .setContentIntent(pendingIntent)
                        .build();
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, notification);
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent;
        notificationIntent = new Intent(this, Home.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Dosage Taken")
                .setContentText("Next dosage coming up")
                .setSmallIcon(R.drawable.messages)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        return START_NOT_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void updateCountDownTimerText() {
        hoursLeftInCounter = (int) timeIntervalSeconds / 3600;
        minutesLeftInCounter = (int) (timeIntervalSeconds % 3600);
        secondsLeftInCounter = minutesLeftInCounter ;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d:%02d", hoursLeftInCounter, minutesLeftInCounter, secondsLeftInCounter);
        //timeView.setText(timeLeftFormatted);
    }
}
