package com.example.coolweather.android.json;

/**
 * Created by angel beat on 2017/8/7.
 */

public interface IParse<T>{
    public abstract T execute(String data,int code);
}
