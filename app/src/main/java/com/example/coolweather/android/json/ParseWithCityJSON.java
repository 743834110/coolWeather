package com.example.coolweather.android.json;

import android.util.Log;

import com.example.coolweather.android.dto.City;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

/**
 * Created by angel beat on 2017/8/7.
 */

public class ParseWithCityJSON implements IParseJSON {
    @Override
    public void execute(String data,int code) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            for(int i = 0 ; i < jsonArray.length() ;i ++){
                JSONObject object = jsonArray.getJSONObject(i);
                City city = new City();
                city.setProvinceCode(code);
                city.setCityCode(object.getInt("id"));
                city.setCityName(object.getString("name"));
                city.save();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
