package com.advertgidtest.weatherforecast;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    LinearLayout llReload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        llReload = (LinearLayout) toolbar.findViewById(R.id.llReload);
        llReload.setOnClickListener(this);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Calendar today = Calendar.getInstance();
        Calendar dateLastUpdate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        try {
            dateLastUpdate.setTime(sdf.parse(SharedPrefsHelper.getInstance(getApplicationContext()).getLastUploadDate()));
            if (hoursBetween(today, dateLastUpdate) > 1) {
                loadData();
            }
        } catch (ParseException e) {
            loadData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llReload:
                loadData();
                break;
        }
    }

    void loadData() {
        Calendar today = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String date = sdf.format(today.getTime());
        SharedPrefsHelper.getInstance(getApplicationContext()).setLastUploadDate(date);

        loadForecast(API.KEY_CITY_0);
        loadForecast(API.KEY_CITY_1);
        loadForecast(API.KEY_CITY_2);
        loadForecast(API.KEY_CITY_3);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Bundle args = new Bundle();
            FragmentCity fragm = FragmentCity.newInstance();
            switch (position) {
                case 0:
                    args.putString("city", API.KEY_CITY_0);
                    break;
                case 1:
                    args.putString("city", API.KEY_CITY_1);
                    break;
                case 2:
                    args.putString("city", API.KEY_CITY_2);
                    break;
                case 3:
                    args.putString("city", API.KEY_CITY_3);
                    break;

                default:
                    args.putString("city", API.KEY_CITY_0);
                    break;
            }
            fragm.setArguments(args);
            return fragm;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    void loadForecast(final String cityId) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(new ToStringConverterFactory())
                .build();

        API service = retrofit.create(API.class);

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(API.KEY_ID, cityId);
        parameters.put(API.KEY_APPID, API.APPID);
        Call<String> call = service.getCityForecast(parameters);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("TAG_RESPONSE", response.body());
                try {
                    JSONObject jObjResponse = new JSONObject(response.body());
                    if (!jObjResponse.has("city") || !jObjResponse.has("list")) {
                        Toast.makeText(getApplicationContext(), "cityId = " + cityId + ": " + jObjResponse.optString("message"), Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "cityId = " + cityId + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPrefsHelper.getInstance(getApplicationContext()).setCityForecastData(cityId, response.body());
                refreshFragment(0, cityId, response.body());
                refreshFragment(1, cityId, response.body());
                refreshFragment(2, cityId, response.body());
                refreshFragment(3, cityId, response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "cityId = " + cityId + ": " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void refreshFragment(int fragmentId, String cityId, String response) {
        String cityIdFragment = getCityIdFragment(fragmentId);
        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + fragmentId);
        if (page != null && cityId.equals(cityIdFragment)) {
            ((FragmentCity) page).getForecast(response);
        }
    }

    int hoursBetween(Calendar cal1, Calendar cal2) {
        long result = Math.abs((cal2.getTimeInMillis() / (60 * 60 * 1000)) - (cal1.getTimeInMillis() / (60 * 60 * 1000)));
        return (int) result;
    }

    String getCityIdFragment(int fragmentId) {
        switch (fragmentId) {
            case 0:
                return API.KEY_CITY_0;
            case 1:
                return API.KEY_CITY_1;
            case 2:
                return API.KEY_CITY_2;
            case 3:
                return API.KEY_CITY_3;

            default:
                return API.KEY_CITY_0;
        }
    }
}
