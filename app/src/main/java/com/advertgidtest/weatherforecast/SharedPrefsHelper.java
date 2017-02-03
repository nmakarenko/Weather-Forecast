package com.advertgidtest.weatherforecast;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHelper {
    private static final String SP_NAME = "FORECAST_SP";
    private static final String KEY_LAST_UPLOAD_DATE = "LAST_UPLOAD_DATE";

    private static volatile SharedPrefsHelper sInstance;
    private SharedPreferences mSharedPreferences;

    public static SharedPrefsHelper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (SharedPrefsHelper.class) {
                if (sInstance == null) {
                    sInstance = new SharedPrefsHelper(context);
                }
            }
        }

        return sInstance;
    }

    private SharedPrefsHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public void setCityForecastData(String cityId, String data) {
        if (mSharedPreferences != null) {
            mSharedPreferences.edit().putString(cityId, data).commit();
        }
    }

    public String getCityForecastData(String cityId) {
        return mSharedPreferences != null
                ? mSharedPreferences.getString(cityId, "")
                : "";
    }

    public void setLastUploadDate(String date) {
        if (mSharedPreferences != null) {
            mSharedPreferences.edit().putString(KEY_LAST_UPLOAD_DATE, date).commit();
        }
    }

    public String getLastUploadDate() {
        return mSharedPreferences != null
                ? mSharedPreferences.getString(KEY_LAST_UPLOAD_DATE, "")
                : "";
    }
}
