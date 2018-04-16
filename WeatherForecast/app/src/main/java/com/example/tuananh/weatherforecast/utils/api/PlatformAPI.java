package com.example.tuananh.weatherforecast.utils.api;

import com.example.tuananh.weatherforecast.model.current.OpenWeatherJSon;
import com.example.tuananh.weatherforecast.model.daily.OpenWeatherDailyJSon;
import com.example.tuananh.weatherforecast.model.nextday.OpenWeatherNextDaysJSon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by anh on 2018/04/16.
 */

public interface PlatformAPI {
    @GET("data/2.5/weather")
    Call<OpenWeatherJSon> loadCurrentWeatherByLocation(@Query("lat") Double lat, @Query("lon") Double lon, @Query("appid") String appid);

    //When language = ja
    @GET("data/2.5/weather")
    Call<OpenWeatherJSon> loadCurrentWeatherByLocation(@Query("lat") Double lat, @Query("lon") Double lon, @Query("lang") String lang, @Query("appid") String appid);

    @GET("data/2.5/weather")
    Call<OpenWeatherJSon> loadCurrentWeatherByName(@Query("q") String name, @Query("appid") String appid);

    //When language = ja
    @GET("data/2.5/weather")
    Call<OpenWeatherJSon> loadCurrentWeatherByName(@Query("q") String name, @Query("lang") String lang, @Query("appid") String appid);

    @GET("data/2.5/forecast")
    Call<OpenWeatherDailyJSon> loadDailyWeatherByLocation(@Query("lat") Double lat, @Query("lon") Double lon, @Query("appid") String appid);

    //When language = ja
    @GET("data/2.5/forecast")
    Call<OpenWeatherDailyJSon> loadDailyWeatherByLocation(@Query("lat") Double lat, @Query("lon") Double lon, @Query("lang") String lang, @Query("appid") String appid);

    @GET("data/2.5/forecast")
    Call<OpenWeatherDailyJSon> loadDailyWeatherByName(@Query("q") String name, @Query("appid") String appid);

    //When language = ja
    @GET("data/2.5/forecast")
    Call<OpenWeatherDailyJSon> loadDailyWeatherByName(@Query("q") String name, @Query("lang") String lang, @Query("appid") String appid);

    @GET("data/2.5/forecast/daily")
    Call<OpenWeatherNextDaysJSon> loadNextDayWeatherByLocation(@Query("lat") Double lat, @Query("lon") Double lon, @Query("cnt") int cnt, @Query("appid") String appid);

    //When language = ja
    @GET("data/2.5/forecast/daily")
    Call<OpenWeatherNextDaysJSon> loadNextDayWeatherByLocation(@Query("lat") Double lat, @Query("lon") Double lon, @Query("cnt") int cnt, @Query("lang") String lang, @Query("appid") String appid);

    @GET("data/2.5/forecast/daily")
    Call<OpenWeatherNextDaysJSon> loadNextDayWeatherByName(@Query("q") String name, @Query("cnt") int cnt, @Query("appid") String appid);

    //When language = ja
    @GET("data/2.5/forecast/daily")
    Call<OpenWeatherNextDaysJSon> loadNextDayWeatherByName(@Query("q") String name, @Query("cnt") int cnt, @Query("lang") String lang, @Query("appid") String appid);
}
