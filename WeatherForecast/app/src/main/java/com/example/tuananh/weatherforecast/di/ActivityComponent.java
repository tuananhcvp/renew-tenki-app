package com.example.tuananh.weatherforecast.di;

import com.example.tuananh.weatherforecast.ForecastDetailActivity;
import com.example.tuananh.weatherforecast.MainActivity;
import com.example.tuananh.weatherforecast.CurrentLocationFragment;
import com.example.tuananh.weatherforecast.SelectedLocationWeatherActivity;
import com.example.tuananh.weatherforecast.WeatherMapsActivity;

import dagger.Subcomponent;

/**
 * ActivityComponent
 * Activity周りのComponent
 */
@ActivityScope
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {
    void inject(MainActivity activity);

    void inject(ForecastDetailActivity activity);

    void inject(SelectedLocationWeatherActivity activity);

    void inject(WeatherMapsActivity activity);

    void inject(CurrentLocationFragment fragment);
}

