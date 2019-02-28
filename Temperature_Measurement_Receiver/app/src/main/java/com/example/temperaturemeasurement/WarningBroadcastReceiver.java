package com.example.temperaturemeasurement;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class WarningBroadcastReceiver extends BroadcastReceiver {

    int notificationId = 100;
    float currentTemp;
    int minSeekBar, maxSeekBar;
    Boolean minWarningActive, maxWarningActive;

    @Override
    public void onReceive(Context context, Intent intent) {

        // Create an intent which is passed back to MainActivity class when user taps the notification
        Intent mainIntent = new Intent(context, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationId, mainIntent, 0);

        // Update temperature values before checking is the user set limit exceeded
        currentTemp = intent.getFloatExtra("currentTemp", 20);
        minSeekBar = intent.getIntExtra("minSeekBar", 10);
        maxSeekBar = intent.getIntExtra("maxSeekBar", 30);
        minWarningActive = intent.getBooleanExtra("minWarningActive", false);
        maxWarningActive = intent.getBooleanExtra("maxWarningActive", false);

        // Build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, TemperatureApplication.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentText(intent.getStringExtra("notificationText"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // On Android 7.1 and lower determines how intrusive the notification is
                .setContentIntent(pendingIntent) // When notification is tapped, call MainActivity.
                .setAutoCancel(true); // Notification is automatically removed when tapped
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // Print log information messages
        Log.d("TEMPPI", "WarningBroadcastReceiver - OnReceive-Method, Current temp value:" + Float.toString(currentTemp));
        Log.d("TEMPPI", "WarningBroadcastReceiver - OnReceive-Method, Min seek-bar value: " + Integer.toString(minSeekBar));
        Log.d("TEMPPI", "WarningBroadcastReceiver - OnReceive-Method, Max seek-bar value: " + Integer.toString(maxSeekBar));
        Log.d("TEMPPI", "Temperature min warning active: " + minWarningActive);
        Log.d("TEMPPI", "Temperature max warning active: " + maxWarningActive);

        // Show notification if user set temperature min or max is exceeded
        if (currentTemp < minSeekBar && minWarningActive) {
            builder.setContentTitle("Temperature too low!");
            notificationManager.notify(notificationId, builder.build());
            Log.d("TEMPPI", "WarningBroadcastReceiver - Temperature too low");
        } else if (currentTemp > maxSeekBar && maxWarningActive) {
            builder.setContentTitle("Temperature too high!");
            notificationManager.notify(notificationId, builder.build());
            Log.d("TEMPPI", "WarningBroadcastReceiver - Temperature too high");
        }
    }
}
