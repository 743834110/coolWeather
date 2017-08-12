package com.example.coolweather.android.dto.gsonDto;

/**
 * Created by angel beat on 2017/8/9.
 */

public class HourlyForecast {
    Condition cond;
    String date;
    String hum;
    String pop;
    String pres;
    String temp;
    DailyForecast.Wind wind;
    static class Condition{
        String code;
        String txt;
    }

}
