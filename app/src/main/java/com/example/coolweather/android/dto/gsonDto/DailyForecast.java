package com.example.coolweather.android.dto.gsonDto;

/**
 * Created by angel beat on 2017/8/9.
 */

 public class DailyForecast {

    public Astro astro;
    public Condition cond;
    public String date;
    public String hum;
    public String pcpn;
    public String pop;
    public String pres;
    public Temperature tmp;
    public String uv;
    public String vis;
    public Wind wind;
    private class Astro{
        public String mr;
        public String ms;
        public String sr;
        public String ss;
    }
    public class Condition{
        public String code_d;
        public String code_n;
        public String text_d;
        public String txt_n;

    }
    public class Temperature{
        public String max;
        public String min;
    }
    static class Wind{
        String deg;
        String dir;
        String sc;
        String speed;
    }

}

