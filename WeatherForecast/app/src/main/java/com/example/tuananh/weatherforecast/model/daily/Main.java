package com.example.tuananh.weatherforecast.model.daily;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anh on 2018/04/16.
 */

public class Main {
    @SerializedName("temp")
    public double temp;

    @SerializedName("pressure")
    public double pressure;

    @SerializedName("humidity")
    public double humidity;

    @SerializedName("temp_min")
    public double tempMin;

    @SerializedName("temp_max")
    public double tempMax;

    @SerializedName("sea_level")
    public double seaLevel;

    @SerializedName("grnd_level")
    public double grndLevel;

    @SerializedName("temp_kf")
    public double tempKf;
}
