package com.example.tuananh.weatherforecast.model.current;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TuanAnh on 4/8/2018.
 */

public class Sys {
    @SerializedName("message")
    public double message;

    @SerializedName("country")
    public String country;

    @SerializedName("sunrise")
    public long sunrise;

    @SerializedName("sunset")
    public long sunset;
}
