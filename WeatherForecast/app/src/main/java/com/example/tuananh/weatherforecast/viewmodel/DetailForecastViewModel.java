package com.example.tuananh.weatherforecast.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.example.tuananh.weatherforecast.BR;
import com.example.tuananh.weatherforecast.R;
import com.example.tuananh.weatherforecast.SplashScreenActivity;
import com.example.tuananh.weatherforecast.adapter.HorizontalListViewDailyAdapter;
import com.example.tuananh.weatherforecast.adapter.NextDaysWeatherAdapter;
import com.example.tuananh.weatherforecast.databinding.ActivityForecastDetailBinding;
import com.example.tuananh.weatherforecast.model.NextDaysItem;
import com.example.tuananh.weatherforecast.model.daily.OpenWeatherDailyJSon;
import com.example.tuananh.weatherforecast.model.nextday.OpenWeatherNextDaysJSon;
import com.example.tuananh.weatherforecast.utils.SharedPreference;
import com.example.tuananh.weatherforecast.utils.Utils;
import com.example.tuananh.weatherforecast.utils.usecase.BaseWeatherUseCase;
import com.example.tuananh.weatherforecast.utils.usecase.WeatherDailyUseCase;
import com.example.tuananh.weatherforecast.utils.usecase.WeatherNextDayUseCase;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by anh on 2018/04/23.
 */

public class DetailForecastViewModel extends BaseObservable {
    public final int TYPE_CURRENT_ADDRESS = 100;
    public final int TYPE_SELECTED_ADDRESS = 101;

    public Context context;
    private String address;

    private NumberFormat format = new DecimalFormat("#0.0");
    private String[] dailyTime = {"6AM", "9AM", "12AM", "3PM", "6PM", "9PM", "12PM", "3AM"};
    private String[] dailyTemp = new String[8];
    private String[] dailyIcon = new String[8];

    private String[] nextDates = new String[7];
    private String[] dayOfWeek = new String[7];

    private WeatherDailyUseCase dailyUseCase;
    private WeatherNextDayUseCase nextDayUseCase;
    private NextDaysWeatherAdapter adapter;

    private Activity activity;
    private ActivityForecastDetailBinding binding;
    private OpenWeatherNextDaysJSon nextDaysEntity;
    private List<NextDaysItem> itemList = new ArrayList<>();


    @Inject
    public DetailForecastViewModel(Context context, WeatherDailyUseCase dailyUseCase,
                                   WeatherNextDayUseCase nextDayUseCase, NextDaysWeatherAdapter adapter) {
        this.context = context;
        this.dailyUseCase = dailyUseCase;
        this.nextDayUseCase = nextDayUseCase;
        this.adapter = adapter;
    }

    public void init(Activity activity, ActivityForecastDetailBinding binding,
                     String address, int type) {
        this.activity = activity;
        this.binding = binding;
        this.address = address;

        initView();
        loadWeather(type);

        adapter.setCallback(position -> {
            String date = dayOfWeek[position] + "<" + nextDates[position] + ">";
            Utils.showDailyWeatherDialog(activity, nextDaysEntity, date, position);
        });

        notifyPropertyChanged(BR.address);
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);

        getDayOfWeek();
        getNextDates();
    }

    /**
     * Load weather information
     */
    private void loadWeather(int type) {
        String appId = context.getResources().getString(R.string.appid_weather);

        switch (type) {
            case TYPE_CURRENT_ADDRESS:
                WeatherDailyUseCase.RequestParameter dailyParameter = new WeatherDailyUseCase.RequestParameter();
                dailyParameter.type = WeatherDailyUseCase.RequestParameter.TYPE_LOCATION;
                dailyParameter.appId = appId;
                dailyParameter.lat = SplashScreenActivity.latitude;
                dailyParameter.lon = SplashScreenActivity.longitude;

                loadDailyWeather(dailyParameter);

                WeatherNextDayUseCase.RequestParameter nextDaysParameter = new WeatherNextDayUseCase.RequestParameter();
                nextDaysParameter.type = WeatherDailyUseCase.RequestParameter.TYPE_LOCATION;
                nextDaysParameter.appId = appId;
                nextDaysParameter.lat = SplashScreenActivity.latitude;
                nextDaysParameter.lon = SplashScreenActivity.longitude;

                loadNextDaysWeather(nextDaysParameter);

                break;

            case TYPE_SELECTED_ADDRESS:
                WeatherDailyUseCase.RequestParameter dailyParameter2 = new WeatherDailyUseCase.RequestParameter();
                dailyParameter2.type = WeatherDailyUseCase.RequestParameter.TYPE_NAME;
                dailyParameter2.appId = appId;
                dailyParameter2.cityName = address;

                loadDailyWeather(dailyParameter2);

                WeatherNextDayUseCase.RequestParameter nextDaysParameter2 = new WeatherNextDayUseCase.RequestParameter();
                nextDaysParameter2.type = WeatherDailyUseCase.RequestParameter.TYPE_NAME;
                nextDaysParameter2.appId = appId;
                nextDaysParameter2.cityName = address;

                loadNextDaysWeather(nextDaysParameter2);

                break;

            default:
                break;
        }
    }

    /**
     * Load daily weather information
     */
    private void loadDailyWeather(WeatherDailyUseCase.RequestParameter parameter) {
        dailyUseCase.execute(parameter, new BaseWeatherUseCase.UseCaseCallback<OpenWeatherDailyJSon>() {
            @Override
            public void onSuccess(OpenWeatherDailyJSon entity) {
                Log.e("TEST_TA", "DAILY WEATHER ==> " + new Gson().toJson(entity));

                for (int i = 0;i < 8;i++) {
                    String temp = format.format(entity.list.get(i).main.temp-273.15)+"°C";
                    dailyTemp[i] = temp;
                    dailyIcon[i] = entity.list.get(i).weather.get(0).icon;
                }

                HorizontalListViewDailyAdapter dailyAdapter = new HorizontalListViewDailyAdapter(activity, dailyTime, dailyTemp, dailyIcon);
                binding.listViewDaily.setAdapter(dailyAdapter);
            }

            @Override
            public void onError(Throwable t) {
                // TODO onError
            }
        });
    }

    /**
     * Load next days weather information
     */
    private void loadNextDaysWeather(WeatherNextDayUseCase.RequestParameter parameter) {
        nextDayUseCase.execute(parameter, new BaseWeatherUseCase.UseCaseCallback<OpenWeatherNextDaysJSon>() {
            @Override
            public void onSuccess(OpenWeatherNextDaysJSon entity) {
                Log.e("TEST_TA", "NEXTDAYS WEATHER ==> " + new Gson().toJson(entity));

                nextDaysEntity = entity;

                for (int i = 0;i < 7;i++) {
                    String tempMax = format.format(entity.list.get(i+1).temp.max-273.15) + "°C";
                    String tempMin = format.format(entity.list.get(i+1).temp.min-273.15) + "°C";
                    String icon = entity.list.get(i+1).weather.get(0).icon;

                    NextDaysItem item = new NextDaysItem();
                    item.date = nextDates[i];
                    item.dayOfWeek = dayOfWeek[i];
                    item.maxMinTemp = tempMax + "/" + tempMin;
                    item.imageIconUrl = context.getResources().getString(R.string.base_icon_url) + icon + ".png";

                    itemList.add(item);
                }

                adapter.addAll(itemList);
            }

            @Override
            public void onError(Throwable t) {
                // TODO onError
            }
        });
    }

    /**
     * Get 7days of week from now Ex: Tuesday, Wednesday...
     */
    private void getDayOfWeek() {
        int posLanguage = SharedPreference.getInstance(context).getInt("Language", 0);

        SimpleDateFormat sdf;
        if (posLanguage == 1) {
            sdf = new SimpleDateFormat("EEEE", Locale.JAPAN);
        } else {
            sdf = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        }

        for (int i = 0; i < 7; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, i + 1);
            dayOfWeek[i] = sdf.format(calendar.getTime());
        }
    }

    /**
     * Get next 7 dates from now Ex: 1-2-2018,2-2-2018...
     */
    private void getNextDates() {
        for (int i = 1; i < 8; i++) {
            Calendar calendar = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            calendar.add(Calendar.DAY_OF_YEAR, i);
            Date date = calendar.getTime();
            nextDates[i - 1] = dateFormat.format(date);
        }
    }
}

