package com.example.tuananh.weatherforecast.model.nextday;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anh on 2018/04/16.
 */

public class Temp {
    @SerializedName("day")
    public double day;

    @SerializedName("min")
    public double min;

    @SerializedName("max")
    public double max;

    @SerializedName("night")
    public double night;

    @SerializedName("eve")
    public double eve;

    @SerializedName("morn")
    public double morn;
}
