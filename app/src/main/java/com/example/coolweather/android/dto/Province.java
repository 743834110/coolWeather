package com.example.coolweather.android.dto;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by angel beat on 2017/8/6.
 */

public class Province extends DataSupport{
    private int id;
    private String provinceName;
    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public Province getProvinceByCode(int code){
        Province province = null;
        List<Province> lists = DataSupport.where("provinceCode = ?",code+"").find(Province.class);
        for (Province element:lists){
            element.delete();
            province = element;
        }
        return province;
    }
    public boolean save(){
        Province province = this.getProvinceByCode(this.getProvinceCode());
        if (province == null)
            return super.save();
        this.setId(province.getId());
        return super.save();

    }
}
