package com.example.coolweather.android.json;

import com.example.coolweather.android.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by angel beat on 2017/8/7.
 */

public class ParseJSONFactory {

    public static <T extends IParseJSON> T newInstance ( Class<T> cls){
        IParseJSON instance = null;
        try {
            instance = (IParseJSON) java.lang.Class.forName(cls.getName()).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (T)instance;
    }
//    public static void main(String [] args){
//        HttpUtil.requestUrl("http://guolin.tech/api/china", new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                ParseWithProvinceJSON parser = ParseJSONFactory.newInstance(ParseWithProvinceJSON.class);
//                parser.execute(response.body().string());
//            }
//        });
//    }
}
