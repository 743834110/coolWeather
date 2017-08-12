package com.example.coolweather.android.json;

/**
 * Created by angel beat on 2017/8/7.
 */

public class ParseFactory {

    public static <T extends IParse> T newInstance (Class<T> cls){
        IParse instance = null;
        try {
            instance = (IParse) java.lang.Class.forName(cls.getName()).newInstance();
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
//                ParseWithProvinceJSON parser = ParseFactory.newInstance(ParseWithProvinceJSON.class);
//                parser.execute(response.body().string());
//            }
//        });
//    }
}
