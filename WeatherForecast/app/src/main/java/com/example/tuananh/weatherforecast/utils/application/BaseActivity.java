package com.example.tuananh.weatherforecast.utils.application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.tuananh.weatherforecast.di.ActivityComponent;
import com.example.tuananh.weatherforecast.di.ActivityModule;

/**
 * Created by anh on 2018/04/16.
 */

public abstract class BaseActivity extends AppCompatActivity {
    ActivityComponent activityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ActivityComponent getComponent() {
        if (activityComponent == null) {
            MyApplication application = (MyApplication) getApplication();

            activityComponent = application.getApplicationComponent().plus(new ActivityModule(this));

        }
        return activityComponent;
    }
}
