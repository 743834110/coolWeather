package com.example.coolweather.android.dto.gsonDto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by angel beat on 2017/8/9.
 */

public class Now {
    public HourlyForecast.Condition cond;
    public String fl;
    public String hum;
    public String pcpn;
    public String pres;
    @SerializedName("tmp")
    public String temperature;
    public String vis;
    public DailyForecast.Wind wind;
}
