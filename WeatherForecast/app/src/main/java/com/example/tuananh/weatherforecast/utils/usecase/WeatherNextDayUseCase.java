package com.example.tuananh.weatherforecast.utils.usecase;

import android.content.Context;

import com.example.tuananh.weatherforecast.model.nextday.OpenWeatherNextDaysJSon;
import com.example.tuananh.weatherforecast.utils.SharedPreference;
import com.example.tuananh.weatherforecast.utils.api.WeatherRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;

/**
 * Created by anh on 2018/05/16.
 */

public class WeatherNextDayUseCase extends UseCase {
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

    public void execute(RequestParameter parameter, UseCaseCallback callback) {
        int posLanguage = SharedPreference.getInstance(context).getInt("Language", 0);

        SingleOnSubscribe<OpenWeatherNextDaysJSon> emitter = e -> {
            switch (parameter.type) {
                case RequestParameter.TYPE_LOCATION:
                    if (posLanguage == 1) {
                        e.onSuccess(weatherRepository.getNextDayWeatherByLocation(parameter.lat, parameter.lon, 8, "ja", parameter.appId));
                    } else {
                        e.onSuccess(weatherRepository.getNextDayWeatherByLocation(parameter.lat, parameter.lon, 8, parameter.appId));
                    }
                    break;
                case RequestParameter.TYPE_NAME:
                    if (posLanguage == 1) {
                        e.onSuccess(weatherRepository.getNextDayWeatherByName(parameter.cityName, 8, "ja", parameter.appId));
                    } else {
                        e.onSuccess(weatherRepository.getNextDayWeatherByName(parameter.cityName, 8, parameter.appId));
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
        void onSuccess(OpenWeatherNextDaysJSon entity);

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
