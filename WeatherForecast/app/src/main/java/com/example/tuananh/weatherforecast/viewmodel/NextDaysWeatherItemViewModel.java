package com.example.tuananh.weatherforecast.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;
import com.example.tuananh.weatherforecast.model.NextDaysItem;
import com.example.tuananh.weatherforecast.model.nextday.OpenWeatherNextDaysJSon;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by anh on 2018/05/16.
 */

public class NextDaysWeatherItemViewModel extends BaseObservable {

    private NextDaysItem entity;
    private String dayOfWeek;
    private String date;
    private String maxMinTemp;
    private String imageIconUrl;

    private int position;

    private CallbackListener listener;

    @Inject
    public NextDaysWeatherItemViewModel() {
    }

    public void setItem(NextDaysItem model, int position) {
        this.entity = model;
        this.position = position;

        this.dayOfWeek = model.dayOfWeek;
        this.date = model.date;
        this.maxMinTemp = model.maxMinTemp;
        this.imageIconUrl = model.imageIconUrl;

        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public String getDayOfWeek() {
        return dayOfWeek;
    }

    @Bindable
    public String getDate() {
        return date;
    }

    @Bindable
    public String getMaxMinTemp() {
        return maxMinTemp;
    }

    @Bindable
    public String getImageIconUrl() {
        return imageIconUrl;
    }

    public void onClickItem() {
        if (listener != null) {
            listener.onShowDailyWeatherInfo(position);
        }
    }

    public void setCallback(CallbackListener listener) {
        this.listener = listener;
    }

    public interface CallbackListener {
        void onShowDailyWeatherInfo(int position);
    }
}
