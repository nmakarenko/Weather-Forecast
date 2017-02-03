package com.advertgidtest.weatherforecast;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface API {
    String BASE_URL = "http://api.openweathermap.org";
    String ICON_URL = "http://openweathermap.org/img/w/";
    String KEY_ID = "id";
    String KEY_APPID = "APPID";
    String APPID = "902c919f8d018976e4d71d17b59745f2";//"dace60038c51d0d623c823692a74732f";
    String KEY_CITY_0 = "703448";//"2643743";//"703448";
    String KEY_CITY_1 = "3675707";//"524901";//"3117735";//"5128581";
    String KEY_CITY_2 = "2643743";//"3675707";//"3067696";
    String KEY_CITY_3 = "2013159";//"2240449";//"2013159";//"1880252";

    @GET("/data/2.5/forecast/city.php")
    Call<String> getCityForecast(@QueryMap Map<String, String> parameters);

    @GET("/data/2.5/group.php")
    Call<String> getCitiesForecast(@QueryMap Map<String, String> parameters);
}
