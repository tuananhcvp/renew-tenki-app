package com.example.tuananh.weatherforecast.utils.usecase;

import android.content.Context;

import com.example.tuananh.weatherforecast.model.current.OpenWeatherJSon;
import com.example.tuananh.weatherforecast.utils.SharedPreference;
import com.example.tuananh.weatherforecast.utils.api.WeatherRepository;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

/**
 * Created by anh on 2018/04/16.
 */

public class WeatherCurrentUseCase extends BaseWeatherUseCase<OpenWeatherJSon> {
    private WeatherRepository weatherRepository;
    private Context context;

    @Inject
    public WeatherCurrentUseCase(WeatherRepository weatherRepository, Context context,
                                 @Named("executeScheduler") Scheduler threadExecutor,
                                 @Named("postScheduler") Scheduler postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.weatherRepository = weatherRepository;
        this.context = context;
    }

    @Override
    public OpenWeatherJSon background(RequestParameter parameter) throws IOException {
        int posLanguage = SharedPreference.getInstance(context).getInt("Language", 0);

        switch (parameter.type) {
            case RequestParameter.TYPE_LOCATION:
                if (posLanguage == 1) {
                    return weatherRepository.getCurrentWeatherByLocation(parameter.lat, parameter.lon, "ja", parameter.appId);
                } else {
                    return weatherRepository.getCurrentWeatherByLocation(parameter.lat, parameter.lon, parameter.appId);
                }

            case RequestParameter.TYPE_NAME:
                if (posLanguage == 1) {
                    return weatherRepository.getCurrentWeatherByName(parameter.cityName, "ja", parameter.appId);
                } else {
                    return weatherRepository.getCurrentWeatherByName(parameter.cityName, parameter.appId);
                }

            default:
                return null;
        }
    }

}
