package com.example.tuananh.weatherforecast.model.current;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TuanAnh on 4/8/2018.
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
}
