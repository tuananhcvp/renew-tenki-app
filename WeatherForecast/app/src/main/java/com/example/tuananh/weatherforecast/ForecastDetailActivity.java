package com.example.tuananh.weatherforecast;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;

import com.example.tuananh.weatherforecast.adapter.HorizontalListViewDailyAdapter;
import com.example.tuananh.weatherforecast.adapter.NextDaysWeatherAdapter;
import com.example.tuananh.weatherforecast.databinding.ActivityForecastDetailBinding;
import com.example.tuananh.weatherforecast.model.NextDaysItem;
import com.example.tuananh.weatherforecast.model.daily.OpenWeatherDailyJSon;
import com.example.tuananh.weatherforecast.model.nextday.OpenWeatherNextDaysJSon;
import com.example.tuananh.weatherforecast.utils.SharedPreference;
import com.example.tuananh.weatherforecast.utils.Utils;
import com.example.tuananh.weatherforecast.utils.application.BaseActivity;
import com.example.tuananh.weatherforecast.utils.usecase.BaseWeatherUseCase;
import com.example.tuananh.weatherforecast.utils.usecase.WeatherDailyUseCase;
import com.example.tuananh.weatherforecast.utils.usecase.WeatherNextDayUseCase;
import com.example.tuananh.weatherforecast.viewmodel.DetailForecastViewModel;
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

public class ForecastDetailActivity extends BaseActivity {
    private final int TYPE_CURRENT_ADDRESS = 100;
    private final int TYPE_SELECTED_ADDRESS = 101;

    private NumberFormat format = new DecimalFormat("#0.0");
    private String[] dailyTime = {"6AM", "9AM", "12AM", "3PM", "6PM", "9PM", "12PM", "3AM"};
    private String[] dailyTemp = new String[8];
    private String[] dailyIcon = new String[8];

    private String[] nextDates = new String[7];
    private String[] dayOfWeek = new String[7];

    private List<NextDaysItem> itemList = new ArrayList<>();
    private String selectedAddress;
    private String currentAddress;

    private OpenWeatherNextDaysJSon nextDaysEntity;

    @Inject
    WeatherDailyUseCase dailyUseCase;
    @Inject
    WeatherNextDayUseCase nextDayUseCase;
    @Inject
    DetailForecastViewModel viewModel;
    @Inject
    NextDaysWeatherAdapter adapter;

    ActivityForecastDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setActionbarTitle(getResources().getString(R.string.title_forecast_detail), this, getSupportActionBar());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getComponent().inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_forecast_detail);
        binding.setViewModel(viewModel);

        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {
        getNextDates();
        getDayOfWeek();

        currentAddress = getIntent().getStringExtra("CurrentAddressName");
        selectedAddress = getIntent().getStringExtra("SelectedAddress");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);

        if (currentAddress != null) {
            viewModel.setAddress(currentAddress);
            loadWeather(TYPE_CURRENT_ADDRESS);
        } else if (selectedAddress != null) {
            viewModel.setAddress(selectedAddress);
            loadWeather(TYPE_SELECTED_ADDRESS);
        }

        adapter.setCallback(position -> {
            String date = dayOfWeek[position] + "<" + nextDates[position] + ">";
            Utils.showDailyWeatherDialog(this, nextDaysEntity, date, position);
        });
    }

    /**
     * Load weather information
     */
    private void loadWeather(int type) {
        String appId = getResources().getString(R.string.appid_weather);

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
                dailyParameter2.cityName = selectedAddress;

                loadDailyWeather(dailyParameter2);

                WeatherNextDayUseCase.RequestParameter nextDaysParameter2 = new WeatherNextDayUseCase.RequestParameter();
                nextDaysParameter2.type = WeatherDailyUseCase.RequestParameter.TYPE_NAME;
                nextDaysParameter2.appId = appId;
                nextDaysParameter2.cityName = selectedAddress;

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

                HorizontalListViewDailyAdapter dailyAdapter = new HorizontalListViewDailyAdapter(ForecastDetailActivity.this, dailyTime, dailyTemp, dailyIcon);
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
                    item.imageIconUrl = getResources().getString(R.string.base_icon_url) + icon + ".png";

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
        int posLanguage = SharedPreference.getInstance(this).getInt("Language", 0);

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
