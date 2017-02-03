package com.advertgidtest.weatherforecast;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FragmentCity extends Fragment {

    List<Forecast> forecastList;

    RecyclerView rvForecast;
    ForecastRVAdapter adapter;

    TextView tvHeader;

    public static FragmentCity newInstance() {
        FragmentCity fragment = new FragmentCity();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_city, container, false);

        tvHeader = (TextView) rootView.findViewById(R.id.tvHeader);

        forecastList = new ArrayList<>();

        rvForecast = (RecyclerView) rootView.findViewById(R.id.rvForecast);
        adapter = new ForecastRVAdapter(getActivity(), forecastList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        rvForecast.setAdapter(adapter);
        rvForecast.setLayoutManager(layoutManager);

        String forecast = SharedPrefsHelper.getInstance(getActivity()).getCityForecastData(getArguments().getString("city"));
        getForecast(forecast);

        return rootView;
    }

    public static int daysBetween(Calendar day1, Calendar day2) {
        int tempDifference = 0;
        int difference = 0;
        Calendar dayOne = (Calendar) day1.clone(),
                dayTwo = (Calendar) day2.clone();

        while (dayOne.get(Calendar.YEAR) != dayTwo.get(Calendar.YEAR)) {
            tempDifference = 365 * (dayTwo.get(Calendar.YEAR) - dayOne.get(Calendar.YEAR));
            difference += tempDifference;

            dayOne.add(Calendar.DAY_OF_YEAR, tempDifference);
        }

        if (dayOne.get(Calendar.DAY_OF_YEAR) != dayTwo.get(Calendar.DAY_OF_YEAR)) {
            tempDifference = dayTwo.get(Calendar.DAY_OF_YEAR) - dayOne.get(Calendar.DAY_OF_YEAR);
            difference += tempDifference;

            dayOne.add(Calendar.DAY_OF_YEAR, tempDifference);
        }

        return difference;
    }

    void getForecast(String data) {
        try {
            JSONObject jObjResponse = new JSONObject(data);
            JSONObject jObjCity = jObjResponse.optJSONObject("city");
            String cityName = jObjCity.optString("name");
            Calendar today = Calendar.getInstance();
            Calendar tomorrow = Calendar.getInstance();
            tomorrow.add(Calendar.DAY_OF_YEAR, 1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            SimpleDateFormat sdfHearerDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            tvHeader.setText(String.format(Locale.US, "%s, %s", cityName, sdfHearerDate.format(tomorrow.getTime())));
            JSONArray jArrList = jObjResponse.optJSONArray("list");
            for (int i = 0; i < jArrList.length(); i++) {

                Calendar date = Calendar.getInstance();
                date.setTime(sdf.parse(jArrList.optJSONObject(i).optString("dt_txt")));

                if (daysBetween(today, date) == 1) {
                    forecastList.add(new Forecast(jArrList.optJSONObject(i)));
                }
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
