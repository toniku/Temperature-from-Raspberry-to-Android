package com.example.temperaturemeasurement;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.temperaturetest.R;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Handler handler = null;
    Timer timer = null;
    TemperatureData temperatureData;
    String cDegrees = " C" + (char) 0x00B0;
    private int notificationId = 100;
    private TextView current_temperature, minimum_temperature, maximum_temperature, minWarningValue, maxWarningValue = null;
    private SeekBar seekBarMin, seekBarMax = null;
    // Update text field when user changes seekBar value
    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int barValue, boolean fromUser) {
            // updated continuously as the user slides the thumb
            if (seekBar.equals(seekBarMin)) {
                minWarningValue.setText("Set min temp warning: " + barValue);
            } else if (seekBar.equals(seekBarMax)) {
                maxWarningValue.setText("Set max temp warning: " + barValue);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };
    private boolean maxWarningActive = false;
    private boolean minWarningActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        timer = new Timer();
        temperatureData = new TemperatureData(this);
        current_temperature = findViewById(R.id.current_temp);
        minimum_temperature = findViewById(R.id.min_temp);
        maximum_temperature = findViewById(R.id.max_temp);
        minWarningValue = findViewById(R.id.min_temp_warning_text);
        maxWarningValue = findViewById(R.id.max_temp_warning_text);
        seekBarMin = findViewById(R.id.seekBar_min);
        seekBarMax = findViewById(R.id.seekBar_max);
        seekBarMin.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarMax.setOnSeekBarChangeListener(seekBarChangeListener);

        findViewById(R.id.button_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTemperatureValues();
                Toast.makeText(getApplicationContext(), "Current temperature: " + temperatureData.getCurrent_temp_String(), Toast.LENGTH_SHORT).show();
            }
        });

        startTimer();

        // Get the SeekBar value and set minimum temperature warning
        findViewById(R.id.set_min_temp_warning).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temperatureData.setMinSeekBar_Value(seekBarMin.getProgress());
                Toast.makeText(getApplicationContext(), "Minimum temperature warning set to: " + seekBarMin.getProgress() + cDegrees, Toast.LENGTH_SHORT).show();
                minWarningActive = true;
                broadcastActivator();
            }
        });

        // Get the SeekBar value and set maximum temperature warning
        findViewById(R.id.set_max_temp_warning).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temperatureData.setMaxSeekBar_Value(seekBarMax.getProgress());
                Toast.makeText(getApplicationContext(), "Maximum temperature warning set to: " + seekBarMax.getProgress() + cDegrees, Toast.LENGTH_SHORT).show();
                maxWarningActive = true;
                broadcastActivator();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTemperatureValues();
    }

    // Timer for updating temperature values every one minute
    private void startTimer() {
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        try {
                            setTemperatureValues();
                            Log.d("TEMPPI", "Timer-GetTemp");
                        } catch (Exception ignored) {
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 60000);
    }

    // Set current, minimum and maximum temperatures
    private void setTemperatureValues() {
        current_temperature.setText(temperatureData.getCurrent_temp_String() + cDegrees);
        String min_temp_string = Float.toString(temperatureData.getMin_temp());
        String max_temp_string = Float.toString(temperatureData.getMax_temp());
        minimum_temperature.setText(min_temp_string + cDegrees);
        maximum_temperature.setText(max_temp_string + cDegrees);
    }

    // Broadcast for showing the notification to user if the set min or max value is exceeded
    protected void broadcastActivator() {
        Intent intent = new Intent(getApplicationContext(), WarningBroadcastReceiver.class);
        // Pass temperature and seekbar values with intent to WarningBroadcastReceiver
        intent.putExtra("minSeekBar", temperatureData.getMinSeekBar_Value());
        intent.putExtra("maxSeekBar", temperatureData.getMaxSeekBar_Value());
        intent.putExtra("currentTemp", temperatureData.getTemp_value());
        intent.putExtra("minWarningActive", minWarningActive);
        intent.putExtra("maxWarningActive", maxWarningActive);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), notificationId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create AlarmManager which is sent with pending intent
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        // Create time when notification will fire
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 1000, alarmIntent);
    }
}