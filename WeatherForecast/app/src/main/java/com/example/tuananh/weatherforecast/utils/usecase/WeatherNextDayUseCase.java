package com.example.tuananh.weatherforecast.utils.usecase;

import android.content.Context;

import com.example.tuananh.weatherforecast.model.nextday.OpenWeatherNextDaysJSon;
import com.example.tuananh.weatherforecast.utils.SharedPreference;
import com.example.tuananh.weatherforecast.utils.api.WeatherRepository;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

/**
 * Created by anh on 2018/05/16.
 */

public class WeatherNextDayUseCase extends BaseWeatherUseCase<OpenWeatherNextDaysJSon> {
    private WeatherRepository weatherRepository;
    private Context context;

    @Inject
    public WeatherNextDayUseCase(@Named("executeScheduler") Scheduler threadExecutor,
                               @Named("postScheduler") Scheduler postExecutionThread,
                               WeatherRepository weatherRepository, Context context) {
        super(threadExecutor, postExecutionThread);
        this.weatherRepository = weatherRepository;
        this.context = context;
    }

    @Override
    public OpenWeatherNextDaysJSon background(RequestParameter parameter) throws IOException {
        int posLanguage = SharedPreference.getInstance(context).getInt("Language", 0);

        switch (parameter.type) {
            case RequestParameter.TYPE_LOCATION:
                if (posLanguage == 1) {
                    return weatherRepository.getNextDayWeatherByLocation(parameter.lat, parameter.lon, 8, "ja", parameter.appId);
                } else {
                    return weatherRepository.getNextDayWeatherByLocation(parameter.lat, parameter.lon, 8, parameter.appId);
                }

            case RequestParameter.TYPE_NAME:
                if (posLanguage == 1) {
                    return weatherRepository.getNextDayWeatherByName(parameter.cityName, 8, "ja", parameter.appId);
                } else {
                    return weatherRepository.getNextDayWeatherByName(parameter.cityName, 8, parameter.appId);
                }

            default:
                return null;
        }
    }
}
