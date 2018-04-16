package com.example.tuananh.weatherforecast.di;

import android.app.Application;
import android.content.Context;

import com.example.tuananh.weatherforecast.utils.api.PlatformAPI;
import com.example.tuananh.weatherforecast.utils.api.WeatherRepository;
import com.example.tuananh.weatherforecast.utils.api.WeatherRepositoryInterface;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anh on 2018/04/16.
 */

@Module
public class ApplicationModule {
    public static final String BASE_URL = "http://api.openweathermap.org/";

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @ApplicationScope
    public Context provideApplicationContext() {
        return application.getApplicationContext();
    }

    @Provides
    @ApplicationScope
    public PlatformAPI providePlatformAPI() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit.create(PlatformAPI.class);
    }

    @Provides
    @ApplicationScope
    public WeatherRepositoryInterface provideWeatherRepositoryInterface(PlatformAPI platformAPI) {
        return new WeatherRepository(platformAPI);
    }

    @Provides
    @Named("executeScheduler")
    public Scheduler provideExecutionScheduler() {
        return Schedulers.io();
    }

    @Provides
    @Named("postScheduler")
    public Scheduler providePostScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
