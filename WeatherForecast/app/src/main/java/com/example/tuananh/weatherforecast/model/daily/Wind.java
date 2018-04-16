package com.example.tuananh.weatherforecast.model.daily;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anh on 2018/04/16.
 */

public class Wind {
    @SerializedName("speed")
    public double speed;

    @SerializedName("deg")
    public double deg;
}
