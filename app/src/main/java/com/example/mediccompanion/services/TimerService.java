package com.example.mediccompanion.services;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;


import androidx.core.app.NotificationCompat;

import com.example.mediccompanion.activities.Home;
import com.example.mediccompanion.R;

import java.util.Locale;

public class TimerService extends Service {
    long timeLeftInMillis = (14/3) * 3600000;
    int hoursLeftInCounter, minutesLeftInCounter, secondsLeftInCounter;
    private final static String TAG = "BroadcastService";
    String message = "Take dosage now";
    public final String CHANNEL_ID = "Personal notifications";
    public final int NOTIFICATION_ID = 1;

    public static final String COUNTDOWN_BR = "com.example.mediccompanion.activities.Home";
    Intent bi = new Intent(COUNTDOWN_BR);

    CountDownTimer cdt = null;

    @Override
    public void onCreate() {
        super.onCreate();


        Log.i(TAG, "Starting timer...");

        cdt = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);
                bi.putExtra("countdown", millisUntilFinished);
                sendBroadcast(bi);
                timeLeftInMillis = millisUntilFinished;
                updateCountDownTimerText();
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "Timer finished");
                notifyMethod();
            }
        };

        cdt.start();
    }
    private void updateCountDownTimerText() {
        hoursLeftInCounter = (int) timeLeftInMillis / 3600000;
        minutesLeftInCounter = (int) (timeLeftInMillis / 60000) - (hoursLeftInCounter * 60);
        secondsLeftInCounter = (int) (timeLeftInMillis / 1000) - (minutesLeftInCounter * 60);

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d:%02d", hoursLeftInCounter, minutesLeftInCounter, secondsLeftInCounter);
        bi.putExtra("time", timeLeftFormatted);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void notifyMethod() {
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(TimerService.this, CHANNEL_ID)
                .setSmallIcon(R.drawable.messages)
                .setContentTitle("Dosage reminder")
                .setContentText(message)
                .setAutoCancel(true);

        Intent intent = new Intent(TimerService.this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message", message);
        PendingIntent pendingIntent = PendingIntent.getActivity(TimerService.this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            CharSequence name = "Personal notifications";
            String description = "Include all personal notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);


        }
    }

}
