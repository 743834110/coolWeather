package com.example.coolweather.android.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.coolweather.android.CoolWeatherApplication;
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

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{

    public Weather weather = null;
    public ScrollView scrollView = null;
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
    private Button home = null;
    public SwipeRefreshLayout refreshLayout = null;
    public DrawerLayout drawerLayout = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.acitivity_weather);
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
        this.home = (Button)this.findViewById(R.id._home);
        this.home.setOnClickListener(this);
        this.scrollView = (ScrollView)this.findViewById(R.id.srcollView);
        this.refreshLayout = (SwipeRefreshLayout)this.findViewById(R.id.refreshLayout);
        this.refreshLayout.setOnRefreshListener(this);
        this.drawerLayout = (DrawerLayout)this.findViewById(R.id.drawerLayout);
        this.initData();
        this.initImage(false);
    }

    public void initImage(final boolean isRefresh){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String imageUrl;
                if (SaveUtil.getDataInSharedPreferences(getString(R.string.weather_image_id)) == null ||isRefresh){
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

        this.weather = ParseFactory.newInstance(ParseWithWeatherJSON.class).execute(SaveUtil.getDataInSharedPreferences(this.getString(R.string.weather_id)),0);
        this.coutyNameText.setText(this.weather.basic.cityName);
        this.timeText.setText(this.weather.basic.update.loc.split(" ")[1]);
        this.temperatureText.setText(this.weather.now.temperature+"℃");
        this.forecastItemContainer.removeAllViews();
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
        this.adviceContainer.removeAllViews();
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

    @Override
    public void onClick(View v) {
        this.drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onRefresh() {
        this.scrollView.setVisibility(View.INVISIBLE);
        new RefreshWorker(this).execute();
    }
}






class RefreshWorker extends AsyncTask<Void,Void,Integer>{
    private WeatherActivity activity = null;
    private String data;
    RefreshWorker(WeatherActivity _activity){
        this.activity = _activity;
    }
    RefreshWorker(){}

    @Override
    protected Integer doInBackground(Void... params) {

        String url = String.format(activity.getString(R.string.weather_url),activity.weather.basic.id);
        try {
            data = HttpUtil.requestUrl(url).body().string();
            return 1;
        } catch (IOException e) {
            return 0;
        }
    }

    @Override
    protected void onPreExecute() {
        activity.scrollView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer){
            case 1:
                SaveUtil.saveDataInSharedPreferences(activity.getString(R.string.weather_id),data);
                activity.weather = ParseFactory.newInstance(ParseWithWeatherJSON.class).execute(this.data,0);
                activity.initData();
                activity.initImage(true);
                break;
            case 0:
                Toast.makeText(activity, "电波无法到达", Toast.LENGTH_SHORT).show();
                break;
        }
        activity.scrollView.setVisibility(View.VISIBLE);
        activity.refreshLayout.setRefreshing(false);
    }
}