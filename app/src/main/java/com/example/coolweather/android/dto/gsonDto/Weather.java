package com.example.coolweather.android.dto.gsonDto;

import java.util.List;

/**
 * Created by angel beat on 2017/8/9.
 */

public class Weather {
    public AQI aqi;
    public Basic basic;
    public List<DailyForecast> daily_forecast;
    public List<HourlyForecast> hourly_forecast;
    public Now now;
    public String status;
    public Suggestion suggestion;
}
