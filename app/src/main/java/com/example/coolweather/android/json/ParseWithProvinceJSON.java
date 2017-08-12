package com.example.coolweather.android.json;

import com.example.coolweather.android.dto.DatabaseDto.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by angel beat on 2017/8/7.
 */

public class ParseWithProvinceJSON implements IParse {
    @Override
    public Object execute(String data,int code) {
        if (data != null && !data.equals(""))
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0 ; i < jsonArray.length() ; i++){
                JSONObject object = jsonArray.getJSONObject(i);
                Province province = new Province();
                province.setProvinceName(object.getString("name"));
                province.setProvinceCode(object.getInt("id"));
                province.save();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

