package com.example.tuananh.weatherforecast.di;

import dagger.Component;

/**
 * Created by anh on 2018/04/16.
 */

@ApplicationScope
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    ActivityComponent plus(ActivityModule module);
}
