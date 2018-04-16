package com.example.tuananh.weatherforecast.model.daily;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anh on 2018/04/16.
 */

public class City {
    @SerializedName("id")
    public long id;

    @SerializedName("name")
    public String name;

    @SerializedName("coord")
    public Coord coord;

    @SerializedName("country")
    public String country;

    @SerializedName("population")
    public int population;

    @SerializedName("sys")
    public SysCity sys;
}
