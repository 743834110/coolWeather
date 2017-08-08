package com.example.coolweather.android.dto;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by angel beat on 2017/8/6.
 */

public class City extends DataSupport{

    private int id;
    private String cityName;
    private int cityCode;
    private int provinceCode;

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }
    public City getCityByCode(int code){
        City city = null;
        List<City> lists = DataSupport.where("cityCode = ?",code+"").find(City.class);
        for (City element:lists){
            element.delete();
            city = element;
        }
        return city;
    }
    public boolean save(){
        City city = this.getCityByCode(this.getCityCode());
        if (city == null)
            return super.save();
        this.setId(city.getId());
        return super.save();

    }
}
