package com.example.tuananh.weatherforecast.model.daily;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by anh on 2018/04/16.
 */

public class ListItem {
    @SerializedName("dt")
    public long dt;

    @SerializedName("main")
    public Main main;

    @SerializedName("weather")
    public List<WeatherItem> weather;

    @SerializedName("clouds")
    public Clouds clouds;

    @SerializedName("wind")
    public Wind wind;

    @SerializedName("rain")
    public Rain rain;

    @SerializedName("sys")
    public SysList sys;

    @SerializedName("dt_txt")
    public String dtTxt;
}
