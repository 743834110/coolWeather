package com.example.coolweather.android.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.coolweather.android.R;
import com.example.coolweather.android.util.SaveUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (SaveUtil.getDataInSharedPreferences("weather_id") != null){
            Intent intent = new Intent(this,WeatherActivity.class);
            this.startActivity(intent);
            this.finish();
        }
    }
}
