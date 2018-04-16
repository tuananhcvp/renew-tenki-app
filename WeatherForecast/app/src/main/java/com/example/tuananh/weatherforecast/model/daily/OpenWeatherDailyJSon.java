package com.example.tuananh.weatherforecast.model.daily;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by anh on 2018/04/16.
 */

public class OpenWeatherDailyJSon {
    @SerializedName("city")
    public City city;

    @SerializedName("cod")
    public String cod;

    @SerializedName("message")
    public double message;

    @SerializedName("cnt")
    public int cnt;

    @SerializedName("list")
    public List<ListItem> list;
}
