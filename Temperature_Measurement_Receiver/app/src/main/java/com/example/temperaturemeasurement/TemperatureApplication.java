package com.example.temperaturemeasurement;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import com.example.temperaturetest.R;

public class TemperatureApplication extends Application {

    static final String CHANNEL_ID = "NOTIFICATION_CHANNEL_ID";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    // Create notificationChannel for showing the notification to the user
    private void createNotificationChannel() {
        Log.d("TEMPPI", "Temperature Application createNotificationChannel-METHOD");
        // Create the NotificationChannel, but only on API 26+ because the NotificationChannel class is new and not in the support library
        // All notifications must be assigned to a channel or it will not appear
        // By categorizing notifications into channels, users can disable specific notification channels for your app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
