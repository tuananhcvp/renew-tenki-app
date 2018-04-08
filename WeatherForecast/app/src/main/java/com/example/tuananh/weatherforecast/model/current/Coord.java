package com.example.tuananh.weatherforecast.model.current;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TuanAnh on 4/8/2018.
 */

public class Coord {
    @SerializedName("lon")
    public double lon;

    @SerializedName("lat")
    public double lat;
}
