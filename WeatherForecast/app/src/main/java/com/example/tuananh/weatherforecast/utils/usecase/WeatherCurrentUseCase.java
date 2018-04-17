package com.example.tuananh.weatherforecast.utils.usecase;

import android.content.Context;

import com.example.tuananh.weatherforecast.model.current.OpenWeatherJSon;
import com.example.tuananh.weatherforecast.utils.SharedPreference;
import com.example.tuananh.weatherforecast.utils.api.WeatherRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;

/**
 * Created by anh on 2018/04/16.
 */

public class WeatherCurrentUseCase extends UseCase {
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

    public void excute(RequestParameter parameter, UseCaseCallback callback) {
        int posLanguage = SharedPreference.getInstance(context).getInt("Language", 0);

        SingleOnSubscribe<OpenWeatherJSon> emitter = e -> {
            switch (parameter.type) {
                case RequestParameter.TYPE_LOCATION:
                    if (posLanguage == 1) {
                        e.onSuccess(weatherRepository.getCurrentWeatherByLocation(parameter.lat, parameter.lon, "ja", parameter.appId));
                    } else {
                        e.onSuccess(weatherRepository.getCurrentWeatherByLocation(parameter.lat, parameter.lon, parameter.appId));
                    }
                    break;
                case RequestParameter.TYPE_NAME:
                    if (posLanguage == 1) {
                        e.onSuccess(weatherRepository.getCurrentWeatherByName(parameter.cityName, "ja", parameter.appId));
                    } else {
                        e.onSuccess(weatherRepository.getCurrentWeatherByName(parameter.cityName, parameter.appId));
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
        void onSuccess(OpenWeatherJSon entity);

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
