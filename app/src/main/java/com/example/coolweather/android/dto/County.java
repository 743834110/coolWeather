package com.example.coolweather.android.dto;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by angel beat on 2017/8/6.
 */

public class County extends DataSupport{

    private int id;
    private String countyName;
    private String weatherID;
    private int countyCode;
    private int cityCode;

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherID() {
        return weatherID;
    }

    public void setWeatherID(String weatherID) {
        this.weatherID = weatherID;
    }

    public int getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(int countyCode) {
        this.countyCode = countyCode;
    }

    public County getCityByCountyCode(int code){
        County county = null;
        List<County> countys = DataSupport.where("countyCode = ?",""+code).find(County.class);
        for (County element:countys){
            element.delete();
            county = element;
        }
        return county;
    }
    public boolean save(){
        County county = this.getCityByCountyCode(this.getCountyCode());
        if (county == null)
            return super.save();
        this.setId(county.getId());
        return super.save();
    }
}
