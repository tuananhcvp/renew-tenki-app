package com.example.tuananh.weatherforecast.model.current;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TuanAnh on 4/8/2018.
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
