package com.venkat.mroads.weatherapplication;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.androidfung.weather.OpenWeatherMapApiHelper;
import com.androidfung.weather.model.WeatherRecord;
import com.androidfung.weather.model.WeatherResponseModel;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<CityInfo> data;
    public OpenWeatherMapApiHelper helper;
    public double[] mPresentTemperatureList = new double[CityData.mCityNames.length+1];
    public TextView mHottest,mCoolest;
    private Handler mHandler;
    private int mInterval = 22000,i,j;
    public float max=0,min=5500;

    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println(this.getClass().getName());
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        helper = new OpenWeatherMapApiHelper(getApplicationContext());

        mHottest = (TextView) findViewById(R.id.hottest);
        mCoolest = (TextView) findViewById(R.id.coolest);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        data = new ArrayList<CityInfo>();
        for (int i = 0; i < CityData.mCityNames.length; i++) {
            data.add(new CityInfo(
                    CityData.mCityNames[i],
                    CityData.latitude[i],
                    CityData.longitude[i],
                    CityData.mCityImages[i]
            ));
        }

        adapter = new CustomAdapter(data);
        recyclerView.setAdapter(adapter);
        mHandler = new Handler();
        startRepeatingTask();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopRepeatingTask();
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void updateStatus() {
        helper.getWeather(CityData.latitude[i], CityData.longitude[i], new Response.Listener<WeatherResponseModel>() {
            @Override
            public void onResponse(WeatherResponseModel response) {
                WeatherRecord record = response.getWeatherRecord();
                float temp=record.getTemperature();
                if(record.getTemperature()<min)
                    min=record.getTemperature();
                if(record.getTemperature()>max)
                    max=record.getTemperature();
                mHottest.setText("Hottest : " + Float.toString(max)+"\u2103");
                mCoolest.setText("Coolest : " +Float.toString(min)+"\u2103");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Failed to get Weather Data", Toast.LENGTH_LONG).show();
            }
        });
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                for(i=0;i<CityData.mCityNames.length;i++) {
                    updateStatus();
                }
            } finally {
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        System.out.println("Stopped the process");
        mHandler.removeCallbacks(mStatusChecker);
    }
}
