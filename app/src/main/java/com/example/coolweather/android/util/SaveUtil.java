package com.example.coolweather.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.coolweather.android.CoolWeatherApplication;

/**
 * Created by angel beat on 2017/8/9.
 */

public class SaveUtil {

    public static String getDataInSharedPreferences(String key){

        String data;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(CoolWeatherApplication.context);
        data = sharedPreferences.getString(key,null);
        return data;
    }
    public static void saveDataInSharedPreferences(String key,String values){

        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(CoolWeatherApplication.context).edit();
        edit.putString(key,values);
        edit.apply();
    }
}
