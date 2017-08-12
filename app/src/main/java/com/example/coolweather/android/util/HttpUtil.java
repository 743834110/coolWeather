package com.example.coolweather.android.util;



import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by angel beat on 2017/8/7.
 */

public class HttpUtil {

    public static void requestUrl(String url , Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
    public static Response requestUrl(String url) throws IOException {
        Response response = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        response = client.newCall(request).execute();
        return response;
    }

}
