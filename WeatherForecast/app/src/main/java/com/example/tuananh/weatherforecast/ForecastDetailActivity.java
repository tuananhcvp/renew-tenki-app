package com.example.tuananh.weatherforecast;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tuananh.weatherforecast.adapter.HorizontalListViewDailyAdapter;
import com.example.tuananh.weatherforecast.adapter.NextDaysWeatherAdapter;
import com.example.tuananh.weatherforecast.databinding.ActivityForecastDetailBinding;
import com.example.tuananh.weatherforecast.model.NextDaysItem;
import com.example.tuananh.weatherforecast.model.daily.OpenWeatherDailyJSon;
import com.example.tuananh.weatherforecast.model.nextday.ListItem;
import com.example.tuananh.weatherforecast.model.nextday.OpenWeatherNextDaysJSon;
import com.example.tuananh.weatherforecast.utils.SharedPreference;
import com.example.tuananh.weatherforecast.utils.application.BaseActivity;
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

        getComponent().inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_forecast_detail);
        binding.setViewModel(viewModel);

        init();
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

        }

        adapter.setCallback(position -> {
            String date = dayOfWeek[position] + "<" + nextDates[position] + ">";
            showDailyWeatherDialog(nextDaysEntity, date, position);
        });
    }

    private void loadWeather(int type) {
        String appId = getResources().getString(R.string.appid_weather);

        switch (type) {
            case TYPE_CURRENT_ADDRESS:
                WeatherDailyUseCase.RequestParameter parameter = new WeatherDailyUseCase.RequestParameter();
                parameter.type = WeatherDailyUseCase.RequestParameter.TYPE_LOCATION;
                parameter.appId = appId;
                parameter.lat = SplashScreenActivity.latitude;
                parameter.lon = SplashScreenActivity.longitude;

                dailyUseCase.execute(parameter, new WeatherDailyUseCase.UseCaseCallback() {
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

                WeatherNextDayUseCase.RequestParameter requestParameter = new WeatherNextDayUseCase.RequestParameter();
                requestParameter.type = WeatherDailyUseCase.RequestParameter.TYPE_LOCATION;
                requestParameter.appId = appId;
                requestParameter.lat = SplashScreenActivity.latitude;
                requestParameter.lon = SplashScreenActivity.longitude;

                nextDayUseCase.execute(requestParameter, new WeatherNextDayUseCase.UseCaseCallback() {
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

                break;

            case TYPE_SELECTED_ADDRESS:
                break;

            default:
                break;
        }
    }

    // Get 7days of week from now Ex: Tuesday, Wednesday...
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

    // Get next 7 dates from now Ex: 1-2-2018,2-2-2018...
    private void getNextDates() {
        for (int i = 1; i < 8; i++) {
            Calendar calendar = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            calendar.add(Calendar.DAY_OF_YEAR, i);
            Date date = calendar.getTime();
            nextDates[i - 1] = dateFormat.format(date);
        }
    }

    private void showDailyWeatherDialog(OpenWeatherNextDaysJSon nextDaysJSon, String date, int i) {
        View v = this.getLayoutInflater().inflate(R.layout.next_days_weather_info, null);

        TextView tvDate = v.findViewById(R.id.tvDate);
        ImageView imgIconState = v.findViewById(R.id.imgIconState);
        TextView tvTemp = v.findViewById(R.id.tvTemp);
        TextView tvState = v.findViewById(R.id.tvState);
        TextView tvMaxMinTemp = v.findViewById(R.id.tvMaxMinTemp);
        TextView tvMorTemp = v.findViewById(R.id.tvMorTemp);
        TextView tvEveTemp = v.findViewById(R.id.tvEveTemp);
        TextView tvNightTemp = v.findViewById(R.id.tvNightTemp);
        TextView tvWind = v.findViewById(R.id.tvWind);
        TextView tvHum = v.findViewById(R.id.tvHum);
        TextView tvPress = v.findViewById(R.id.tvPress);

        ListItem item = nextDaysJSon.list.get(i + 1);
        String tempDay = format.format(item.temp.day - 273.15) + "°C";
        String state = item.weather.get(0).description;
        String tempMax = format.format(item.temp.max - 273.15) + "°C";
        String tempMin = format.format(item.temp.min - 273.15) + "°C";
        String tempMorn = format.format(item.temp.morning - 273.15) + "°C";
        String tempEve = format.format(item.temp.evening - 273.15) + "°C";
        String tempNight = format.format(item.temp.night - 273.15) + "°C";
        String wind = item.speed + "m/s";
        String press = item.pressure + "hpa";
        String hum = item.humidity + "%";
        String urlIcon = item.weather.get(0).icon;

        tvDate.setText(date);
        Glide.with(this).load(getString(R.string.base_icon_url) + urlIcon + ".png").into(imgIconState);
        tvTemp.setText(tempDay);
        tvState.setText(state);
        tvMaxMinTemp.setText(tempMax + "/" + tempMin);
        tvMorTemp.setText(getResources().getString(R.string.txt_morning) + ": " + tempMorn);
        tvEveTemp.setText(getResources().getString(R.string.txt_evening) + ": " + tempEve);
        tvNightTemp.setText(getResources().getString(R.string.txt_night) + ": " + tempNight);
        tvWind.setText(getResources().getString(R.string.txt_wind) + ": " + wind);
        tvHum.setText(getResources().getString(R.string.txt_humidity) + ": " + hum);
        tvPress.setText(getResources().getString(R.string.txt_pressure) + ": " + press);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setView(v);
        builder.setCancelable(true);

        final AlertDialog dialog = builder.create();
        v.setOnClickListener(v1 -> dialog.dismiss());

//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        float dp = 320f;
//        float fpixels = metrics.density * dp;
//        int pixels = (int) (fpixels + 0.5f);

        dialog.show();
//        dialog.getWindow().setLayout(pixels, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }
}
