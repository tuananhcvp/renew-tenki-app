package com.example.tuananh.weatherforecast;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.example.tuananh.weatherforecast.utils.application.BaseActivity;
import com.example.tuananh.weatherforecast.utils.LocationService;
import com.example.tuananh.weatherforecast.utils.SharedPreference;
import com.example.tuananh.weatherforecast.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by anh on 2018/04/16.
 */

public class SplashScreenActivity extends BaseActivity {
    private boolean _active = false;
    private int _splashTime = 2500;
    public static double latitude;
    public static double longitude;

    public static String[] country;
    public static String[] cityArr;
    public static List<String> japanCityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(this);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_splash_screen);

        initService();
        getAllCityData();
        getJapanCityData();

        int posLanguage = SharedPreference.getInstance(this).getInt("Language", 0);
        Log.e("LANGUAGE", "==> " + posLanguage);
        if (posLanguage == 0) {
            Utils.setLocaleLanguage(this, "en");
        } else if (posLanguage == 1) {
            Utils.setLocaleLanguage(this, "ja");
        }

        if (Utils.isNetworkConnected(this)) {
            splash();
        } else {
            Toasty.info(getApplicationContext(), getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        }

        final SwipeRefreshLayout swipe = (SwipeRefreshLayout)findViewById(R.id.swipeSplash);
        swipe.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        swipe.setOnRefreshListener(() -> {
            if (Utils.isNetworkConnected(SplashScreenActivity.this) && !_active) {
                new Handler().postDelayed(() -> {
                    Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                    swipe.setRefreshing(false);
                }, 3000);
            } else if (!Utils.isNetworkConnected(SplashScreenActivity.this)) {
                Toasty.info(getApplicationContext(), getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
                swipe.setRefreshing(false);
            }
        });

    }

    private void splash() {
        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    _active = true;
                    while (waited < _splashTime) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                    SplashScreenActivity.this.finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        splashThread.start();
    }

    public void initService() {
        Intent intent = new Intent(this, LocationService.class);
        startService(intent);
    }

    //Get cities in Japan from json file
    private void getJapanCityData() {
        japanCityList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset("japancity.json"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                String str = (String) object.get("slug");
                String city = str.substring(0, 1).toUpperCase() + str.substring(1);
                japanCityList.add(city);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collections.sort(japanCityList, String.CASE_INSENSITIVE_ORDER);
        Log.e("JAPAN CITY", "==> " + new Gson().toJson(japanCityList));
    }

    //Get cities in the world from json file
    private void getAllCityData() {
        ArrayList<String> city = new ArrayList<>();
        try {
            JSONObject jsonRoot = new JSONObject(loadJSONFromAsset("cities.json"));
            JSONArray listCountryArr = new JSONArray();
            listCountryArr = jsonRoot.names();
            country = new String[listCountryArr.length()];
            for (int i = 0; i < listCountryArr.length(); i++) {
                country[i] = listCountryArr.getString(i);
            }

            for (String c : country) {
                JSONArray jsonArray = jsonRoot.getJSONArray(c);
                for (int m = 0; m < jsonArray.length(); m++) {
                    city.add(jsonArray.getString(m));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cityArr = new String[city.size()];
        for (int j = 0; j < city.size(); j++) {
            cityArr[j] = city.get(j);
        }
    }

    private String loadJSONFromAsset(String file) {
        String json = null;
        try {
            InputStream is = this.getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
