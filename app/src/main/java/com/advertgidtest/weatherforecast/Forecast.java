package com.advertgidtest.weatherforecast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Forecast {
    Calendar date;
    String description;
    double temperature;
    double pressure;
    double wind;
    double windDegrees;
    int humidity;
    String iconId;

    public Forecast(JSONObject jsonResponse) {
        try {
            JSONObject mainJObj = jsonResponse.optJSONObject("main");
            this.temperature = mainJObj.optDouble("temp") - 273.15;
            this.pressure = mainJObj.optDouble("pressure");
            this.humidity = mainJObj.optInt("humidity");

            JSONArray weatherJArr = jsonResponse.optJSONArray("weather");
            JSONObject weatherJObj = weatherJArr.optJSONObject(0);
            this.description = weatherJObj.optString("main");
            this.iconId = weatherJObj.optString("icon");

            JSONObject windJObj = jsonResponse.optJSONObject("wind");
            this.wind = windJObj.optDouble("speed");
            this.windDegrees = windJObj.optDouble("deg");

            this.date = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            this.date.setTime(sdf.parse(jsonResponse.optString("dt_txt")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
