package com.venkat.mroads.weatherapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.androidfung.weather.OpenWeatherMapApiHelper;
import com.androidfung.weather.model.WeatherRecord;
import com.androidfung.weather.model.WeatherResponseModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by venkat on 5/8/17.
 */

public class DisplayWeather extends Activity {
    public String latitude,longitude;
    public ImageView mCityImage;
    public TextView mPresentTemperature, mPresentHumidity,mMaxTemperature,mMinTemperature,mPressure,mCurrentTime;
    public OpenWeatherMapApiHelper helper;
    private int mInterval = 20000;
    private Handler mHandler;
    private Calendar c;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_weather);

        mCityImage = (ImageView) findViewById(R.id.weather_image);
        mPresentTemperature = (TextView) findViewById(R.id.present_temperature);
        mPresentHumidity = (TextView) findViewById(R.id.present_humidity);
        mMaxTemperature = (TextView) findViewById(R.id.max_temp_text);
        mMinTemperature = (TextView) findViewById(R.id.min_temp_text);
        mPressure = (TextView) findViewById(R.id.present_pressure);
        mCurrentTime = (TextView) findViewById(R.id.present_time);
        c = Calendar.getInstance();

        Bundle bundle = getIntent().getExtras();
        latitude = bundle.getString("Latitude");
        longitude = bundle.getString("Longitude");
        System.out.println(latitude+" "+longitude+" Image path = "+Integer.parseInt(bundle.getString("ImagePath")));
        mCityImage.setImageResource(Integer.parseInt(bundle.getString("ImagePath")));

        helper = new OpenWeatherMapApiHelper(getApplicationContext());

        mHandler = new Handler();
        startRepeatingTask();
    }

    private void updateStatus() {
        helper.getWeather(Double.parseDouble(latitude), Double.parseDouble(longitude), new Response.Listener<WeatherResponseModel>() {
            @Override
            public void onResponse(WeatherResponseModel response) {
                WeatherRecord record = response.getWeatherRecord();
                mPresentTemperature.setText("Temperature : "+Float.toString(record.getTemperature())+"\u2103");
                mMaxTemperature.setText("Max Temp : "+record.getMaximunTemperature()+"\u2103");
                mMinTemperature.setText("Min Temp : "+record.getMinimumTemperature()+"\u2103");
                mPresentHumidity.setText("Humidity : "+record.getHumidity()+" %");
                mPressure.setText("Pressure : "+record.getPressure()+" hpa");
                DateFormat dateFormatter = new SimpleDateFormat("hh:mm:ss");
                dateFormatter.setLenient(false);
                Date today = new Date();
                String s = dateFormatter.format(today);
                mCurrentTime.setText("Last Update : "+s+"");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Failed to get Weather Data",Toast.LENGTH_LONG).show();
            }
        });
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                updateStatus();
            } finally {
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopRepeatingTask();
        finish();
    }

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }
}
