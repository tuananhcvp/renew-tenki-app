package com.example.tuananh.weatherforecast.model.daily;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anh on 2018/04/16.
 */

public class WeatherItem {
    @SerializedName("id")
    public long id;

    @SerializedName("main")
    public String main;

    @SerializedName("description")
    public String description;

    @SerializedName("icon")
    public String icon;
}
