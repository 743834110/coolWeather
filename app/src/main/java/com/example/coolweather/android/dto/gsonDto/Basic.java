package com.example.coolweather.android.dto.gsonDto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by angel beat on 2017/8/9.
 */

public class Basic {
    
    @SerializedName("city")
    public String cityName;
    public String cnty;
    public String id;
    @SerializedName("lat")
    public String latitude;
    @SerializedName("lon")
    public String longtitude;
    public Update update;

    public class Update{
        public String loc;
        public String utc;
    }

}
