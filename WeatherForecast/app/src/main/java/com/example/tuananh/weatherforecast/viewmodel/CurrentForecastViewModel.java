package com.example.tuananh.weatherforecast.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.example.tuananh.weatherforecast.BR;
import com.example.tuananh.weatherforecast.R;
import com.example.tuananh.weatherforecast.model.current.OpenWeatherJSon;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import javax.inject.Inject;

/**
 * Created by anh on 2018/04/17.
 */

public class CurrentForecastViewModel extends BaseObservable {
    NumberFormat format = new DecimalFormat("#0.0");

    public String addressName;
    public String imageIconUrl;
    public String mainTemp;
    public String mainState;
    public String maxMinTemp;
    public String wind;
    public String press;
    public String humidity;
    public String state;
    public String sunriseTime;
    public String sunsetTime;

    public Context context;

    @Inject
    public CurrentForecastViewModel(Context context) {
        this.context = context;
    }

    public void setModel(OpenWeatherJSon openWeather) {
        addressName = openWeather.name;
        imageIconUrl = getIconSkyUrl(openWeather);
        Log.e("TEST_TA","=> " + imageIconUrl);
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
}

