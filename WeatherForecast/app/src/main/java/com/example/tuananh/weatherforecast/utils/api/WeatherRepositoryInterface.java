package com.example.tuananh.weatherforecast.utils.api;

import com.example.tuananh.weatherforecast.model.current.OpenWeatherJSon;
import com.example.tuananh.weatherforecast.model.daily.OpenWeatherDailyJSon;
import com.example.tuananh.weatherforecast.model.nextday.OpenWeatherNextDaysJSon;

import java.io.IOException;

/**
 * Created by anh on 2018/04/16.
 */

public interface WeatherRepositoryInterface {
    OpenWeatherJSon getCurrentWeatherByLocation(Double lat, Double lon, String appId) throws IOException;

    OpenWeatherJSon getCurrentWeatherByLocation(Double lat, Double lon, String lang, String appId) throws IOException;

    OpenWeatherJSon getCurrentWeatherByName(String name, String appId) throws IOException;

    OpenWeatherJSon getCurrentWeatherByName(String name, String lang, String appId) throws IOException;

    OpenWeatherDailyJSon getDailyWeatherByLocation(Double lat, Double lon, String appId) throws IOException;

    OpenWeatherDailyJSon getDailyWeatherByLocation(Double lat, Double lon, String lang, String appId) throws IOException;

    OpenWeatherDailyJSon getDailyWeatherByName(String name, String appId) throws IOException;

    OpenWeatherDailyJSon getDailyWeatherByName(String name, String lang, String appId) throws IOException;

    OpenWeatherNextDaysJSon getNextDayWeatherByLocation(Double lat, Double lon, int cnt, String appId) throws IOException;

    OpenWeatherNextDaysJSon getNextDayWeatherByLocation(Double lat, Double lon, int cnt, String lang, String appId) throws IOException;

    OpenWeatherNextDaysJSon getNextDayWeatherByName(String name, int cnt, String appId) throws IOException;

    OpenWeatherNextDaysJSon getNextDayWeatherByName(String name, int cnt, String lang, String appId) throws IOException;
}
