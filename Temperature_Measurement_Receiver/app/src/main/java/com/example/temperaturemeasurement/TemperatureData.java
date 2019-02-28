package com.example.temperaturemeasurement;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TemperatureData {

    /***
     * Just a class for storing and retrieving data
     */


    private Context context;
    private String current_temp_String;
    private String url = "https://rjw3kli3nk.execute-api.us-west-2.amazonaws.com/Data-API/myid/1";
    private float temp_value;
    private float min_temp;
    private float max_temp;
    private int minSeekBar_Value, maxSeekBar_Value = 0;

    public TemperatureData(Context context) {
        this.context = context;
    }

    public String getCurrent_temp_String() {
        getTemperatureFromDb();
        return current_temp_String;
    }

    public void setCurrent_temp_String(String current_temp_String) {
        this.current_temp_String = current_temp_String;
    }

    public float getMin_temp() {
        getTemperatureFromDb();
        return min_temp;
    }

    public void setMin_temp(float min_temp) {
        this.min_temp = min_temp;
    }

    public float getMax_temp() {
        getTemperatureFromDb();
        return max_temp;
    }

    public void setMax_temp(float max_temp) {
        this.max_temp = max_temp;
    }

    public float getTemp_value() {
        return temp_value;
    }

    public void setTemp_value(float temp_value) {
        this.temp_value = temp_value;
    }

    public int getMinSeekBar_Value() {
        return minSeekBar_Value;
    }

    public void setMinSeekBar_Value(int minSeekBar_Value) {
        this.minSeekBar_Value = minSeekBar_Value;
    }

    public int getMaxSeekBar_Value() {
        return maxSeekBar_Value;
    }

    public void setMaxSeekBar_Value(int maxSeekBar_Value) {
        this.maxSeekBar_Value = maxSeekBar_Value;
    }

    public void getTemperatureFromDb() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();
                current_temp_String = myResponse.substring(37, 41);
                temp_value = Float.valueOf(current_temp_String);
                setMinTemperatureValue();
                setMaxTemperatureValue();
            }
        });
    }

    private void setMinTemperatureValue() {
        if (min_temp == 0) {
            min_temp = temp_value + 1.00f;
        }
        if (temp_value < min_temp) {
            setMin_temp(temp_value);
            String minTempString = Float.toString(min_temp);
            Log.d("TEMPPI", "TemperatureData - setMaxTemperatureValue-Method, Min temp value: " + minTempString);
            Log.d("TEMPPI", "setMin temperature");
        }
    }

    private void setMaxTemperatureValue() {
        if (temp_value > max_temp) {
            setMax_temp(temp_value);
            String maxTempString = Float.toString(max_temp);
            Log.d("TEMPPI", "TemperatureData - setMaxTemperatureValue-Method, Max temp value: " + maxTempString);
            Log.d("TEMPPI", "setMax temperature");

        }
    }
}