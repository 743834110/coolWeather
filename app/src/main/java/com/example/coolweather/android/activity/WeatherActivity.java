package com.example.coolweather.android.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.coolweather.android.R;
import com.example.coolweather.android.dto.gsonDto.DailyForecast;
import com.example.coolweather.android.dto.gsonDto.Suggestion;
import com.example.coolweather.android.dto.gsonDto.Weather;
import com.example.coolweather.android.json.ParseFactory;
import com.example.coolweather.android.json.ParseWithImageUrl;
import com.example.coolweather.android.json.ParseWithWeatherJSON;
import com.example.coolweather.android.util.HttpUtil;
import com.example.coolweather.android.util.SaveUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by angel beat on 2017/8/9.
 */

public class WeatherActivity extends AppCompatActivity {

    private Weather weather = null;
    private ScrollView scrollView = null;
    private TextView coutyNameText = null;
    private TextView timeText = null;
    private TextView temperatureText = null;
    private TextView weatherConditionText = null;
    private LinearLayout forecastItemContainer = null;
    private TextView AQIText = null;
    private TextView PM25Text = null;
    private LinearLayout adviceContainer = null;
    private ImageView imageView = null;
    private SharedPreferences sharedPreferences = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.acitivity_weather);
        this.weather = ParseFactory.newInstance(ParseWithWeatherJSON.class).execute(SaveUtil.getDataInSharedPreferences(this.getString(R.string.weather_id)),0);
        this.coutyNameText = (TextView)this.findViewById(R.id.weather_countyName);
        this.timeText = (TextView)this.findViewById(R.id.weather_time);
        this.temperatureText = (TextView)this.findViewById(R.id.weather_temperature);
        this.weatherConditionText = (TextView)this.findViewById(R.id.weather_condition);
        this.forecastItemContainer = (LinearLayout)this.findViewById(R.id.weather_forecast_container);
        this.AQIText = (TextView)this.findViewById(R.id.aqi);
        this.PM25Text = (TextView)this.findViewById(R.id.pm25);
        this.adviceContainer = (LinearLayout)this.findViewById(R.id.advice);
        this.imageView = (ImageView)this.findViewById(R.id.weather_imageView);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.initData();
        this.initImage();
    }

    public void initImage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String imageUrl;
                if (SaveUtil.getDataInSharedPreferences(getString(R.string.weather_image_id)) == null){
                    String url = getString(R.string.weather_image_url);
                    imageUrl = ParseFactory.newInstance(ParseWithImageUrl.class).execute(url,0);
                }
                else
                    imageUrl = SaveUtil.getDataInSharedPreferences(getString(R.string.weather_image_id));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(imageUrl).into(imageView);
                    }
                });

            }
        }).start();


    }
    public void initData(){
        this.coutyNameText.setText(this.weather.basic.cityName);
        this.timeText.setText(this.weather.basic.update.loc.split(" ")[1]);
        this.temperatureText.setText(this.weather.now.temperature+"℃");
        for (DailyForecast element:this.weather.daily_forecast) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, this.forecastItemContainer, false);
            TextView view1 =(TextView) view.findViewById(R.id.forecast_date);
            TextView view2 = (TextView)view.findViewById(R.id.forecast_weather_info);
            TextView view3 = (TextView)view.findViewById(R.id.forecast_max_temperature);
            TextView view4 = (TextView)view.findViewById(R.id.forecast_min_temperature);
            view1.setText(element.date);
            view2.setText(element.cond.txt_n);
            view3.setText(element.tmp.max);
            view4.setText(element.tmp.max);
            this.forecastItemContainer.addView(view);
        }
        this.AQIText.setText(this.weather.aqi == null?"暂无":this.weather.aqi.city.aqi);
        this.PM25Text.setText(this.weather.aqi == null?"暂无":this.weather.aqi.city.pm25);
        Field fields[] = Suggestion.class.getDeclaredFields();
        for (Field field:fields){
            if (!field.getName().equals("$change") && !field.getName().equalsIgnoreCase("serialVersionUID")) {
                field.setAccessible(true);
                TextView textView = this.getSuggestionTextView();
                try {
                    Suggestion.suggestion sub = (Suggestion.suggestion)field.get(this.weather.suggestion);
                    textView.setText(field.getName()+"："+sub.getTxt());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                this.adviceContainer.addView(textView);
            }
        }


    }

    public TextView getSuggestionTextView(){
        TextView textView = new TextView(this);
        textView.setTextColor(Color.parseColor("#ffffff"));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,15,15,15);
        textView.setLayoutParams(params);
        return textView;
    }

}
