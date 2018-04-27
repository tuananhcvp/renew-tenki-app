package com.example.tuananh.weatherforecast.utils.usecase;

import android.content.Context;

import com.example.tuananh.weatherforecast.model.daily.OpenWeatherDailyJSon;
import com.example.tuananh.weatherforecast.utils.SharedPreference;
import com.example.tuananh.weatherforecast.utils.api.WeatherRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;

/**
 * Created by anh on 2018/04/24.
 */

public class WeatherDailyUseCase extends UseCase {
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

    public void execute(WeatherDailyUseCase.RequestParameter parameter, UseCaseCallback callback) {
        int posLanguage = SharedPreference.getInstance(context).getInt("Language", 0);

        SingleOnSubscribe<OpenWeatherDailyJSon> emitter = e -> {
            switch (parameter.type) {
                case WeatherDailyUseCase.RequestParameter.TYPE_LOCATION:
                    if (posLanguage == 1) {
                        e.onSuccess(weatherRepository.getDailyWeatherByLocation(parameter.lat, parameter.lon, "ja", parameter.appId));
                    } else {
                        e.onSuccess(weatherRepository.getDailyWeatherByLocation(parameter.lat, parameter.lon, parameter.appId));
                    }
                    break;
                case WeatherDailyUseCase.RequestParameter.TYPE_NAME:
                    if (posLanguage == 1) {
                        e.onSuccess(weatherRepository.getDailyWeatherByName(parameter.cityName, "ja", parameter.appId));
                    } else {
                        e.onSuccess(weatherRepository.getDailyWeatherByName(parameter.cityName, parameter.appId));
                    }
                    break;
                default:
                    break;
            }
        };

        Single.create(emitter)
                .subscribeOn(threadExecutor)
                .observeOn(postExecutionThread)
                .subscribe(
                        callback::onSuccess,
                        callback::onError);

    }

    public interface UseCaseCallback {
        void onSuccess(OpenWeatherDailyJSon entity);

        void onError(Throwable t);
    }

    public static class RequestParameter {
        public static final String TYPE_NAME = "TYPE_NAME";
        public static final String TYPE_LOCATION = "TYPE_LOCATION";

        public String type;
        public String appId;
        public double lat;
        public double lon;
        public String cityName;
    }
}
