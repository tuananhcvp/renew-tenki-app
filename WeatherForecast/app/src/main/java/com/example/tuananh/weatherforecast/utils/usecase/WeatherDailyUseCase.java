package com.example.tuananh.weatherforecast.utils.usecase;

import android.content.Context;

import com.example.tuananh.weatherforecast.model.daily.OpenWeatherDailyJSon;
import com.example.tuananh.weatherforecast.utils.SharedPreference;
import com.example.tuananh.weatherforecast.utils.api.WeatherRepository;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

/**
 * Created by anh on 2018/04/24.
 */

public class WeatherDailyUseCase extends BaseWeatherUseCase<OpenWeatherDailyJSon> {
    private WeatherRepository weatherRepository;
    private Context context;

    @Inject
    public WeatherDailyUseCase(@Named("executeScheduler") Scheduler threadExecutor,
                               @Named("postScheduler") Scheduler postExecutionThread,
                               WeatherRepository weatherRepository, Context context) {
        super(threadExecutor, postExecutionThread);
        this.weatherRepository = weatherRepository;
        this.context = context;
    }

    @Override
    public OpenWeatherDailyJSon background(RequestParameter parameter) throws IOException {
        int posLanguage = SharedPreference.getInstance(context).getInt("Language", 0);

        switch (parameter.type) {
            case RequestParameter.TYPE_LOCATION:
                if (posLanguage == 1) {
                    return weatherRepository.getDailyWeatherByLocation(parameter.lat, parameter.lon, "ja", parameter.appId);
                } else {
                    return weatherRepository.getDailyWeatherByLocation(parameter.lat, parameter.lon, parameter.appId);
                }

            case RequestParameter.TYPE_NAME:
                if (posLanguage == 1) {
                    return weatherRepository.getDailyWeatherByName(parameter.cityName, "ja", parameter.appId);
                } else {
                    return weatherRepository.getDailyWeatherByName(parameter.cityName, parameter.appId);
                }

            default:
                return null;
        }
    }
}
