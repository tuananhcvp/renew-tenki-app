package com.example.tuananh.weatherforecast.model.nextday;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by anh on 2018/04/16.
 */

public class ListItem {
    @SerializedName("dt")
    public long dt;

    @SerializedName("temp")
    public Temp temp;

    @SerializedName("pressure")
    public double pressure;

    @SerializedName("humidity")
    public double humidity;

    @SerializedName("weather")
    public List<WeatherItem> weather;

    @SerializedName("speed")
    public double speed;

    @SerializedName("deg")
    public double deg;

    @SerializedName("clouds")
    public int clouds;

}
