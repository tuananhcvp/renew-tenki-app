package com.example.tuananh.weatherforecast.model.nextday;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anh on 2018/04/16.
 */

public class Coord {

    @SerializedName("lon")
    public double lon;

    @SerializedName("lat")
    public double lat;
}
