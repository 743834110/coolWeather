package com.example.coolweather.android.json;

import com.example.coolweather.android.CoolWeatherApplication;
import com.example.coolweather.android.R;
import com.example.coolweather.android.util.HttpUtil;
import com.example.coolweather.android.util.SaveUtil;

import java.io.IOException;

/**
 * Created by angel beat on 2017/8/12.
 */

public class ParseWithImageUrl<T extends String> implements IParse {
    @Override
    public T execute(String data, int code) {

        T url = null;
        try {
            url = (T)HttpUtil.requestUrl(data).body().string();
            String name = CoolWeatherApplication.context.getString(R.string.weather_image_id);
            SaveUtil.saveDataInSharedPreferences(name,url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }
}
