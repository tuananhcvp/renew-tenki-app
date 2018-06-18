package com.example.tuananh.weatherforecast.utils.usecase;

import java.io.IOException;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;

/**
 * Created by anh on 2018/06/06.
 */

public abstract class BaseWeatherUseCase<T> extends UseCase {

    public BaseWeatherUseCase(Scheduler threadExecutor, Scheduler postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    public void execute(RequestParameter parameter, UseCaseCallback callback) {
        SingleOnSubscribe<T> emitter = e -> {
            e.onSuccess(background(parameter));
        };

        Single.create(emitter)
                .subscribeOn(threadExecutor)
                .observeOn(postExecutionThread)
                .subscribe(
                        callback::onSuccess,
                        callback::onError);

    }

    public abstract T background(RequestParameter parameter) throws IOException;

    public interface UseCaseCallback<T> {
        void onSuccess(T success);

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
