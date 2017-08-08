package com.example.coolweather.android.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.coolweather.android.R;
import com.example.coolweather.android.json.ParseJSONFactory;
import com.example.coolweather.android.json.ParseWithCityJSON;
import com.example.coolweather.android.json.ParseWithCountyJSON;
import com.example.coolweather.android.json.ParseWithProvinceJSON;
import com.example.coolweather.android.util.HttpUtil;

import org.litepal.LitePal;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
