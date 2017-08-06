package com.example.coolweather.android;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;


/**
 * Created by angel beat on 2017/8/6.
 */

public class CoolWeatherApplication extends Application{

    public static Context context = null;
    @Override
    public void onCreate() {
       context = this.getApplicationContext();
        LitePal.initialize(context);
    }
}
