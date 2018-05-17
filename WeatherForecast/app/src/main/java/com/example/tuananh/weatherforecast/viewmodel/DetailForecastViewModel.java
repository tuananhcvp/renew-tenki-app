package com.example.tuananh.weatherforecast.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.tuananh.weatherforecast.BR;

import javax.inject.Inject;

/**
 * Created by anh on 2018/04/23.
 */

public class DetailForecastViewModel extends BaseObservable {
    public Context context;
    private String address;

    @Inject
    public DetailForecastViewModel(Context context) {
        this.context = context;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    @Bindable
    public String getAddress() {
        return address;
    }
}

