package com.example.tuananh.weatherforecast.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.tuananh.weatherforecast.BR;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.inject.Inject;

/**
 * Created by anh on 2018/04/23.
 */

public class DetailForecastViewModel extends BaseObservable {
    private NumberFormat format = new DecimalFormat("#0.0");
    private String[] arrDailyTime = {"6AM", "9AM", "12AM", "3PM", "6PM", "9PM", "12PM", "3AM"};
    private String[] arrDailyTemp = new String[]{};
    private String[] urlDailyIcon = new String[]{};
    private String[] arrNextDaysDate = new String[]{};
    private String[] arrNextDays;
    private String[] arrNextDaysTemp = new String[]{};
    private String[] urlNextDaysIcon = new String[]{};

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

