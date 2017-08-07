package com.venkat.mroads.weatherapplication;

/**
 * Created by venkat on 4/8/17.
 */

public class CityInfo {
    String mCityName;
    double latitude;
    double longitude;
    int mImagePath;

    public CityInfo(String mCityName,double latitude,double longitude,int mImagePath) {
        this.mCityName=mCityName;
        this.latitude=latitude;
        this.longitude=longitude;
        this.mImagePath = mImagePath;
    }

    public String getCityName() {
        return mCityName;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public int getImagePath() {
        return mImagePath;
    }
}
