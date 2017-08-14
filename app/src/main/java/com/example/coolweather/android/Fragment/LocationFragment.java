package com.example.coolweather.android.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coolweather.android.CoolWeatherApplication;
import com.example.coolweather.android.R;
import com.example.coolweather.android.activity.MainActivity;
import com.example.coolweather.android.activity.WeatherActivity;
import com.example.coolweather.android.dto.DatabaseDto.City;
import com.example.coolweather.android.dto.DatabaseDto.County;
import com.example.coolweather.android.dto.DatabaseDto.Province;
import com.example.coolweather.android.dto.gsonDto.Weather;
import com.example.coolweather.android.json.IParse;
import com.example.coolweather.android.json.ParseFactory;
import com.example.coolweather.android.json.ParseWithCityJSON;
import com.example.coolweather.android.json.ParseWithCountyJSON;
import com.example.coolweather.android.json.ParseWithProvinceJSON;
import com.example.coolweather.android.util.HttpUtil;
import com.example.coolweather.android.util.SaveUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Response;

public class LocationFragment extends Fragment {

    private TextView textView = null;
    private ImageView imageView = null;
    private ListView listView = null;
    private ProgressDialog progressDialog = null;
    private ArrayAdapter<String> arrayAdapter = null;

    public final static int PROVINCE = 1;
    public final static int CITY = 2;
    public final static int COUNTY = 3;
    public final static int WEATHER = 4;

    private List<String> data = new ArrayList<String>();

    private LoadDataContext context = null;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch(msg.what){
                        case PROVINCE:
                            textView.setText("中国");
                            imageView.setVisibility(View.GONE);
                            listView.setClickable(true);
                            progressDialog.dismiss();
                            data.clear();
                            for (Province element:(List<Province>)msg.obj){
                                data.add(element.getProvinceName());
                            }
                            arrayAdapter.notifyDataSetChanged();
                            break;
                        case CITY:
                            imageView.setClickable(true);
                            imageView.setVisibility(View.VISIBLE);
                            listView.setClickable(true);
                            progressDialog.dismiss();
                            data.clear();
                            for (City element:(List<City>)msg.obj){
                                data.add(element.getCityName());
                            }
                            arrayAdapter.notifyDataSetChanged();
                            break;
                        case COUNTY:
                            progressDialog.dismiss();
                            imageView.setClickable(true);
                            imageView.setVisibility(View.VISIBLE);
                            listView.setClickable(true);
                            data.clear();
                            for (County element:(List<County>)msg.obj){
                                data.add(element.getCountyName());
                            }
                            arrayAdapter.notifyDataSetChanged();
                            break;
                        case WEATHER:
                            //切换到另一个页面
                            progressDialog.dismiss();
                            if (LocationFragment.this.getActivity() instanceof MainActivity) {
                                Intent intent = new Intent(LocationFragment.this.getContext(), WeatherActivity.class);
                                //传不传都无所谓
                                intent.putExtra(LocationFragment.this.getString(R.string.weather_id), (String) msg.obj);
                                LocationFragment.this.startActivity(intent);
                                LocationFragment.this.getActivity().finish();
                            }
                            else{
                                WeatherActivity activity = (WeatherActivity) LocationFragment.this.getActivity();
                                activity.initData();
                                activity.drawerLayout.closeDrawers();
                            }
                            break;
                        //0代表要进行加载数据处理：展开进度栏
                        case 0:
                            showProgressDialog();
                            break;
                        //-1代表读取数据出错,超时
                        case -1:
                            progressDialog.dismiss();
                            Toast.makeText(LocationFragment.this.getContext(), "电波无法到达哟", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        }
    };

    //碎片OnCreate的时候后调用，用户加载布局
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_location,container,false);
        this.imageView = (ImageView)view.findViewById(R.id.fragment_imageView);
        this.textView = (TextView)view.findViewById(R.id.fragment_textView);
        this.listView = (ListView)view.findViewById(R.id.fragment_listView);

        this.initData();

        return view;
    }
    public void initData(){
        this.arrayAdapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_list_item_1,data);
        this.listView.setAdapter(arrayAdapter);
        this.context = new LoadDataContext(LoadDataContext.loadProvinceStatus,this.handler);
        this.context.execute();
    }
    public void showProgressDialog(){

        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(this.getContext());
            this.progressDialog.setTitle("状态栏");
            this.progressDialog.setMessage("正在加载中,请稍后......");
            this.progressDialog.setCancelable(false);
            this.progressDialog.setIndeterminate(true);
        }
        this.progressDialog.show();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (listView.isClickable()) {
                    textView.setText(LocationFragment.this.data.get(position));
                    LocationFragment.this.context.next(position);
                }
            }
        });

        this.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationFragment.this.context.last();
            }
        });
    }

}

//加载数据的状态接口
abstract class AbstractLoadDataStatus{

    private LoadDataContext context = null;

    public void setContext(LoadDataContext _context){
        this.context = _context;
    }
    public LoadDataContext getContext(){
        return this.context;
    }
    public abstract void loadProvince();
    public abstract void loadCity();
    public abstract void loadCounty();
    public abstract void parseData(String data);
    public abstract void errorBack();
    public abstract void loadWeather();
    public void queryFromServer(String url){

        try {
            Response response = HttpUtil.requestUrl(url);
            this.parseData(response.body().string());
        }
        catch (IOException e){
            Message message = new Message();
            message.what = -1;
            this.getContext().getHandler().handleMessage(message);
            this.errorBack();

        }
    }

}
//加载省份数据的状态
class LoadProvinceStatus extends AbstractLoadDataStatus{

    @Override
    public void loadProvince() {
        LoadDataContext context = this.getContext();
       context.provinces = DataSupport.findAll(Province.class);
        if (context.provinces.isEmpty()) {
            String url = CoolWeatherApplication.context.getString(R.string.init_url);
            this.queryFromServer(url);
        }
        else{
            Message message = new Message();
            message.what = LocationFragment.PROVINCE;
            message.obj = context.provinces;
            this.getContext().getHandler().handleMessage(message);
//            this.getContext().changeStatus(LoadDataContext.loadCityStatus);
            this.getContext().loadProvince();
        }
    }

    @Override
    public void loadCity() {

    }

    @Override
    public void loadCounty() {

    }

    @Override
    public void parseData(String data) {
        IParse iParse = ParseFactory.newInstance(ParseWithProvinceJSON.class);
        iParse.execute(data,0);
        this.loadProvince();

    }

    @Override
    public void errorBack() {
        this.getContext().loadProvince();
    }

    @Override
    public void loadWeather() {

    }
}
//加载城市数据的状态
class LoadCityStatus extends AbstractLoadDataStatus{

    @Override
    public void loadProvince() {

    }

    @Override
    public void loadCity() {
        LoadDataContext context = this.getContext();
        if (context.city == null)
            context.province = context.provinces.get(context.getPosition());
        else
            context.city = null;
        context.cities = DataSupport.where("provinceCode = ?",""+context.province.getProvinceCode()).find(City.class);
        if (context.cities.isEmpty()){
            String url = CoolWeatherApplication.context.getString(R.string.init_url)+"/"+""+context.province.getProvinceCode();
            this.queryFromServer(url);
        }
        else{
            Message message = new Message();
            message.what = LocationFragment.CITY;
            message.obj = context.cities;
            context.getHandler().handleMessage(message);
//          context.changeStatus(LoadDataContext.loadCountyStatus);
            context.loadCity();
        }
    }

    @Override
    public void loadCounty() {

    }

    @Override
    public void parseData(String data) {
        IParse iParse = ParseFactory.newInstance(ParseWithCityJSON.class);
        LoadDataContext context = this.getContext();
        iParse.execute(data,context.province.getProvinceCode());
        this.loadCity();
    }

    @Override
    public void errorBack() {

        this.getContext().removeCurrentStatus().getContext().loadProvince();
    }

    @Override
    public void loadWeather() {

    }
}
//加载县区数据的状态
class LoadCountyStatus extends  AbstractLoadDataStatus{

    @Override
    public void loadProvince() {

    }

    @Override
    public void loadCity() {

    }

    @Override
    public void loadCounty() {
        LoadDataContext context = this.getContext();
        context.city = context.cities.get(context.getPosition());
        context.counties = DataSupport.where("cityCode = ?",""+context.city.getCityCode()).find(County.class);
        if (context.counties.isEmpty()){
            String url = CoolWeatherApplication.context.getString(R.string.init_url)+"/"+context.province.getProvinceCode()+"/"+context.city.getCityCode();
            this.queryFromServer(url);
        }
        else{
            Message message = new Message();
            message.what = LocationFragment.COUNTY;
            message.obj = context.counties;
            context.getHandler().handleMessage(message);
            context.loadCounty();

        }
    }

    @Override
    public void parseData(String data) {
        IParse iParse = ParseFactory.newInstance(ParseWithCountyJSON.class);
        LoadDataContext context = this.getContext();
        iParse.execute(data,context.city.getCityCode());
        this.loadCounty();
    }

    @Override
    public void errorBack() {
        this.getContext().removeCurrentStatus().getContext().loadCity();
    }

    @Override
    public void loadWeather() {

    }
}
//读取天气信息的状态
class LoadWeatherStatus extends AbstractLoadDataStatus{

    @Override
    public void loadProvince() {

    }

    @Override
    public void loadCity() {

    }

    @Override
    public void loadCounty() {

    }

    @Override
    public void parseData(String data) {
        //将得来的天气数据保存:用的是键R.string.weather_id;
        LoadDataContext context = this.getContext();
        SaveUtil.saveDataInSharedPreferences(CoolWeatherApplication.context.getString(R.string.weather_id),data);
        Message message = new Message();
        message.what = LocationFragment.WEATHER;
        message.obj = context.county.getWeatherID();
        context.getHandler().handleMessage(message);


    }

    @Override
    public void errorBack() {
        LoadDataContext context = this.getContext();
        context.removeCurrentStatus().getContext().loadCounty();
    }

    @Override
    public void loadWeather() {
        //暂时去获取天气状况
        LoadDataContext context = this.getContext();
        context.county = context.counties.get(context.getPosition());

        String url = CoolWeatherApplication.context.getString(R.string.weather_url);
        this.queryFromServer(String.format(url, context.county.getWeatherID()));
        context.loadWeather();

    }
}
//状态模式
 class LoadDataContext{
    public final static int NEXT = 1;
    public final static int LAST = 0;
    public final static LoadProvinceStatus loadProvinceStatus = new LoadProvinceStatus();
    public final static LoadCityStatus loadCityStatus = new LoadCityStatus();
    public final static LoadCountyStatus loadCountyStatus = new LoadCountyStatus();
    public final static LoadWeatherStatus loadWeatherStatus = new LoadWeatherStatus();

    private  LinkedList<AbstractLoadDataStatus> historyStatus = new LinkedList<AbstractLoadDataStatus>();
    private AbstractLoadDataStatus currentStatus = null;
    private int orientation ;
    private int position ;
    private Handler handler = null;

    public List<Province> provinces = null;
    public List<City> cities = null;
    public List<County> counties = null;
    public City city = null;
    public Province province = null;
    public County county = null;
    public Weather weather = null;

    public LoadDataContext(AbstractLoadDataStatus _currentStatus,Handler _handler){
        this.currentStatus = _currentStatus;
        this.currentStatus.setContext(this);
        this.handler = _handler;
    }
    public LoadDataContext(Handler _handler){
        this.currentStatus = LoadDataContext.loadProvinceStatus;
        this.currentStatus.setContext(this);
        this.handler = _handler;
    }
    public void last(){
        synchronized (this) {
            this.orientation = LAST;
            this.notifyAll();
        }
    }
    public  void next(int _position){
        synchronized (this) {
            this.orientation = NEXT;
            this.position = _position;
            this.notifyAll();
        }
    }
    public void setBlock(){
        synchronized (this){
            try {
               wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void sendMessage(){
        Message message = new Message();
        message.what = 0;
        this.handler.handleMessage(message);
    }
    public int getPosition() {
        return position;
    }
    public AbstractLoadDataStatus removeCurrentStatus(){
        this.currentStatus = this.historyStatus.removeLast();
        return this.currentStatus;
    }

    public Handler getHandler(){
        return this.handler;
    }
    public void changeStatus(AbstractLoadDataStatus status){
        status.setContext(this);

        this.historyStatus.add(this.currentStatus);
//        this.lastStatus = this.currentStatus;
        this.currentStatus = status;
    }

    public void loadProvince(){
        this.setBlock();
        if (this.orientation == NEXT) {
            this.sendMessage();
            this.changeStatus(LoadDataContext.loadCityStatus);
            this.currentStatus.loadCity();
        }
    }
    public void loadCity(){
        this.setBlock();
        if (this.orientation == NEXT) {
            this.sendMessage();
            this.changeStatus(LoadDataContext.loadCountyStatus);
            this.currentStatus.loadCounty();
        }
        else{
//            this.currentStatus = this.lastStatus;
            this.currentStatus = this.historyStatus.removeLast();
            this.currentStatus.loadProvince();
        }
    }
    public void loadCounty(){
        this.setBlock();
        if (this.orientation == NEXT) {
            this.sendMessage();
            this.changeStatus(LoadDataContext.loadWeatherStatus);
            this.currentStatus.loadWeather();
        }
        else{
//            this.currentStatus = this.lastStatus;
            this.currentStatus = this.historyStatus.removeLast();
            this.currentStatus.loadCity();
        }
    }
    public void loadWeather(){
        //读取完weather信息类后将来要做的事。

    }
    public void  execute(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendMessage();
                currentStatus.loadProvince();
            }
        }).start();
    }
}