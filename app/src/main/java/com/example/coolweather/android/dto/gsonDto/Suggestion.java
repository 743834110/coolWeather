package com.example.coolweather.android.dto.gsonDto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by angel beat on 2017/8/9.
 */

public class Suggestion {

    @SerializedName("air")
    public Air 空气指数;
    @SerializedName("comf")
    public Comfort 舒适度;
    @SerializedName("cw")
    public CarWash 洗车指数;
    @SerializedName("drsg")
    public DressG 穿衣建议;
    @SerializedName("flu")
    public Flu 感冒指数;
    @SerializedName("sport")
    public Sport 运动建议;
    @SerializedName("trav")
    public Travel 旅游建议;
    @SerializedName("uv")
    public UV 辐射强度;

    public abstract class suggestion{
        public abstract String getTxt();
    }

    public class Air extends suggestion{
        public String brf;
        public String txt;

        @Override
        public String getTxt() {
            return this.txt;
        }
    }
    public class Comfort extends suggestion{
        public String brf;
        public String txt;

        @Override
        public String getTxt() {
            return this.txt;
        }
    }
    public class CarWash extends suggestion{
        public String brf;
        public String txt;

        @Override
        public String getTxt() {
            return this.txt;
        }
    }
    public class DressG extends suggestion{
        public String brf;
        public String txt;

        @Override
        public String getTxt() {
            return this.txt;
        }
    }
    public class Flu extends suggestion{
        public String brf;
        public String txt;

        @Override
        public String getTxt() {
            return this.txt;
        }
    }
    public class Sport extends suggestion{
        public String brf;
        public String txt;

        @Override
        public String getTxt() {
            return this.txt;
        }
    }
    public class Travel  extends suggestion{
        public String brf;
        public String txt;

        @Override
        public String getTxt() {
            return txt;
        }
    }
    public class UV extends suggestion{
        public String brf;
        public String txt;

        @Override
        public String getTxt() {
            return this.txt;
        }
    }
}
