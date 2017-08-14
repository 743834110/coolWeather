package com.example.coolweather.android.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.example.coolweather.android.R;
import com.example.coolweather.android.dto.gsonDto.Weather;
import com.example.coolweather.android.json.ParseFactory;
import com.example.coolweather.android.json.ParseWithWeatherJSON;
import com.example.coolweather.android.util.HttpUtil;
import com.example.coolweather.android.util.SaveUtil;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by angel beat on 2017/8/14.
 */

public class AutoUpdateService extends IntentService {

    public AutoUpdateService() {
        super("AutoUpateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        this.updateWeatherData();
        this.updateWeatherImage();
        long time = SystemClock.elapsedRealtime() + 1000 * 60 * 60 *8;
        AlarmManager alarmManager = (AlarmManager)this.getSystemService(ALARM_SERVICE);
        //intent = new Intent(this,AutoUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this,0,intent,0);
//        alarmManager.cancel(pendingIntent);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,time,pendingIntent);
        Log.d("service","启动。。。。。。");
    }
    private void updateWeatherData(){

        String data = SaveUtil.getDataInSharedPreferences("weather_id");
        Weather weather = ParseFactory.newInstance(ParseWithWeatherJSON.class).execute(data,0);
        data = weather.basic.id;
        data = String.format(this.getString(R.string.weather_url),data);
        try {
            data = HttpUtil.requestUrl(data).body().string();
            SaveUtil.saveDataInSharedPreferences("weather_id",data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void updateWeatherImage(){

        try {
            String url = HttpUtil.requestUrl(this.getString(R.string.weather_image_url)).body().string();
            SaveUtil.saveDataInSharedPreferences(this.getString(R.string.weather_image_id),url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
