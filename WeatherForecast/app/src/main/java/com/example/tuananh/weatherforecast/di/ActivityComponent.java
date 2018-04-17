package com.example.tuananh.weatherforecast.di;

import com.example.tuananh.weatherforecast.MainActivity;
import com.example.tuananh.weatherforecast.fragment.CurrentLocationFragment;

import dagger.Subcomponent;

/**
 * ActivityComponent
 * Activity周りのComponent
 */
@ActivityScope
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {
    void inject(MainActivity mainActivity);

    void inject(CurrentLocationFragment fragment);
}

