package com.example.tuananh.weatherforecast.model.current;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by TuanAnh on 4/8/2018.
 */

public class OpenWeatherJSon {
    @SerializedName("coord")
    public Coord coord;

    @SerializedName("weather")
    public List<WeatherItem> weather;

    @SerializedName("base")
    public String base;

    @SerializedName("main")
    public Main main;

    @SerializedName("wind")
    public Wind wind;

    @SerializedName("clouds")
    public Clouds clouds;

    @SerializedName("dt")
    public long dt;

    @SerializedName("sys")
    public Sys sys;

    @SerializedName("id")
    public long id;

    @SerializedName("name")
    public String name;

    @SerializedName("cod")
    public int cod;
}
