package com.example.tuananh.weatherforecast.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Handler;
import android.util.Log;

import com.example.tuananh.weatherforecast.BR;
import com.example.tuananh.weatherforecast.ForecastDetailActivity;
import com.example.tuananh.weatherforecast.R;
import com.example.tuananh.weatherforecast.SelectedLocationWeatherActivity;
import com.example.tuananh.weatherforecast.SplashScreenActivity;
import com.example.tuananh.weatherforecast.model.current.OpenWeatherJSon;
import com.example.tuananh.weatherforecast.utils.SharedPreference;
import com.example.tuananh.weatherforecast.utils.Utils;
import com.example.tuananh.weatherforecast.utils.usecase.BaseWeatherUseCase;
import com.example.tuananh.weatherforecast.utils.usecase.WeatherCurrentUseCase;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import javax.inject.Inject;

/**
 * Created by anh on 2018/04/17.
 */

public class CurrentForecastViewModel extends BaseObservable {
    public final int TYPE_LOCATION = 1;
    public final int TYPE_NAME = 2;

    NumberFormat format = new DecimalFormat("#0.0");

    private String addressName;
    private String imageIconUrl;
    private String mainTemp;
    private String mainState;
    private String maxMinTemp;
    private String wind;
    private String press;
    private String humidity;
    private String state;
    private String sunriseTime;
    private String sunsetTime;
    private String address = "";
    private int type;

    private Context context;
    private WeatherCurrentUseCase useCase;

    @Inject
    public CurrentForecastViewModel(Context context, WeatherCurrentUseCase useCase) {
        this.context = context;
        this.useCase = useCase;
    }

    public void init(int type, String address) {
        this.type = type;
        this.address = address;

        switch (type) {
            case TYPE_LOCATION:
                // Load weather info
                if (SplashScreenActivity.latitude == 0 && SplashScreenActivity.longitude == 0) {
                    final Handler handler = new Handler();
                    handler.postDelayed(()
                            -> loadWeather(SharedPreference.getInstance(context).getDouble("latitude", 0), SharedPreference.getInstance(context).getDouble("longitude", 0)), 1000);
                } else {
                    loadWeather(SplashScreenActivity.latitude, SplashScreenActivity.longitude);
                }

            case TYPE_NAME:
                loadCurrentWeatherByCityName(address);
        }
    }

    public void setModel(OpenWeatherJSon openWeather) {
        addressName = openWeather.name;
        imageIconUrl = getIconSkyUrl(openWeather);
        mainTemp = getStringTemp(openWeather.main.temp);
        mainState = openWeather.weather.get(0).main;
        maxMinTemp = getStringTemp(openWeather.main.tempMax) + "/" + getStringTemp(openWeather.main.tempMin);
        wind = openWeather.wind.speed + "m/s";
        press = openWeather.main.pressure + "hpa";
        humidity = openWeather.main.humidity + "%";
        state = openWeather.weather.get(0).description;
        sunriseTime = getStringTime(openWeather.sys.sunrise);
        sunsetTime = getStringTime(openWeather.sys.sunset);

        notifyPropertyChanged(BR.addressName);
        notifyPropertyChanged(BR.mainTemp);
        notifyPropertyChanged(BR.mainState);
        notifyPropertyChanged(BR.maxMinTemp);
        notifyPropertyChanged(BR.wind);
        notifyPropertyChanged(BR.press);
        notifyPropertyChanged(BR.humidity);
        notifyPropertyChanged(BR.state);
        notifyPropertyChanged(BR.sunriseTime);
        notifyPropertyChanged(BR.sunsetTime);
        notifyChange();
    }

    @Bindable
    public String getAddressName() {
        return addressName;
    }

    public String imageIconUrl() {
        return imageIconUrl;
    }

    @Bindable
    public String getMainTemp() {
        return mainTemp;
    }

    @Bindable
    public String getMainState() {
        return mainState;
    }

    @Bindable
    public String getMaxMinTemp() {
        return maxMinTemp;
    }

    @Bindable
    public String getWind() {
        return wind;
    }

    @Bindable
    public String getPress() {
        return press;
    }

    @Bindable
    public String getHumidity() {
        return humidity;
    }

    @Bindable
    public String getState() {
        return state;
    }

    @Bindable
    public String getSunriseTime() {
        return sunriseTime;
    }

    @Bindable
    public String getSunsetTime() {
        return sunsetTime;
    }

    public void onClickDetail() {
        if (!address.equalsIgnoreCase("")) {
            Intent intent = new Intent(context, ForecastDetailActivity.class);

            if (type == TYPE_LOCATION) {
                intent.putExtra("CurrentAddressName", address);
            } else if (type == TYPE_NAME) {
                intent.putExtra("SelectedAddress", address);
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Utils.showToastNotify(context, context.getString(R.string.check_data_not_found));
        }
    }

    private String getIconSkyUrl(OpenWeatherJSon openWeather) {
        return context.getString(R.string.base_icon_url) + openWeather.weather.get(0).icon + ".png";
    }

    private String getStringTemp(double temp) {
        return format.format(temp - 273.15) + "°C";
    }

    private String getStringTime(long time) {
        Date timeSunrise = new Date(time * 1000);
        return timeSunrise.getHours() + ":" + timeSunrise.getMinutes();
    }

    /**
     * Load current weather information by coordinate
     */
    private void loadWeather(double lat, double lon) {
        WeatherCurrentUseCase.RequestParameter parameter = new WeatherCurrentUseCase.RequestParameter();
        parameter.type = WeatherCurrentUseCase.RequestParameter.TYPE_LOCATION;
        parameter.lat = lat;
        parameter.lon = lon;
        parameter.appId = context.getResources().getString(R.string.appid_weather);

        useCase.execute(parameter, new BaseWeatherUseCase.UseCaseCallback<OpenWeatherJSon>() {
            @Override
            public void onSuccess(OpenWeatherJSon entity) {
                setModel(entity);
                address = entity.name;
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }
        });

    }

    /**
     * Load weather information by city name
     */
    private void loadCurrentWeatherByCityName(String city) {
        WeatherCurrentUseCase.RequestParameter parameter = new WeatherCurrentUseCase.RequestParameter();
        parameter.type = WeatherCurrentUseCase.RequestParameter.TYPE_NAME;
        parameter.appId = context.getResources().getString(R.string.appid_weather);;
        parameter.cityName = city;

        useCase.execute(parameter, new BaseWeatherUseCase.UseCaseCallback<OpenWeatherJSon>() {
            @Override
            public void onSuccess(OpenWeatherJSon entity) {
                setModel(entity);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }
        });

    }
}

