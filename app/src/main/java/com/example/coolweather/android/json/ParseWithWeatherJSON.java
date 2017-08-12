package com.example.coolweather.android.json;

import com.example.coolweather.android.dto.gsonDto.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by angel beat on 2017/8/9.
 */

public class ParseWithWeatherJSON implements IParse {

    @Override
    public Weather execute(String data, int code) {

        Weather weather = null;
        JSONObject object = null;
        JSONArray jsonArray = null;
        try {
            object = new JSONObject(data);
            jsonArray = object.getJSONArray("HeWeather5");
            object = jsonArray.getJSONObject(0);
            weather = new Gson().fromJson(object.toString(),Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weather;
    }
}
