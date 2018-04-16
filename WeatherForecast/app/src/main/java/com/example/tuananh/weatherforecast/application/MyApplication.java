package com.example.tuananh.weatherforecast.application;

import android.app.Application;

import com.example.tuananh.weatherforecast.di.ActivityScope;
import com.example.tuananh.weatherforecast.di.ApplicationComponent;
import com.example.tuananh.weatherforecast.di.ApplicationModule;
import com.example.tuananh.weatherforecast.di.DaggerApplicationComponent;

/**
 * Created by anh on 2018/04/16.
 */

@ActivityScope
public class MyApplication extends Application {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
