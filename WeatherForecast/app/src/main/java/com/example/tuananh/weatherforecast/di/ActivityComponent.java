package com.example.tuananh.weatherforecast.di;

import com.example.tuananh.weatherforecast.MainActivity;

import dagger.Subcomponent;

/**
 * ActivityComponent
 * Activity周りのComponent
 */
@ActivityScope
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {
    void inject(MainActivity mainActivity);

}

