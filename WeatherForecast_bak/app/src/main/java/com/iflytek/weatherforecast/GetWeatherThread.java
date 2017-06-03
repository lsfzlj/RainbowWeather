package com.iflytek.weatherforecast;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Wrapper;
import java.util.concurrent.Callable;

/**
 * Created by Administrator on 2017/5/19.
 */

public class GetWeatherThread  implements Callable<Weather>{
    final String httpUrl="http://apis.baidu.com/heweather/pro/weather";
    final String httpArg="city=";
    String finalUrl;

    Weather weatherResult;
    String city;
    public GetWeatherThread(String cityIn){
      finalUrl=httpUrl+"?"+httpArg+cityIn;
        city = cityIn;
    }
    public Weather call(){
        Weather resultWeather = new Weather();
        String json="";
        JsonParser par = new JsonParser();
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            URL url = new URL(finalUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            //my apikey is : 3c9b3e3824efcffcc88786b64f360c60
            connection.setRequestProperty("apikey",  "d5de4e9e6ee16ebfedb3db3655d1386b");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            json = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            JsonObject gsonTotal = par.parse(json).getAsJsonObject();
            JsonArray array = gsonTotal.getAsJsonArray("HeWeather data service 3.0");
            JsonObject gson = array.get(0).getAsJsonObject();
            resultWeather.status = gson.get("status").toString().replace("\"","");
            if(resultWeather.status.equals("ok")){
                JsonObject gsonNowWeather = gson.get("now").getAsJsonObject();
                JsonObject gsonBasic      = gson.get("basic").getAsJsonObject();
                resultWeather.city =gsonBasic.get("city").toString().replace("\"","");
                resultWeather.tempatureCurrent = Integer.valueOf(gsonNowWeather.get("tmp").toString().replace("\"",""));
                resultWeather.windPower 		  = gsonNowWeather.get("wind").getAsJsonObject().get("sc").toString().replace("\"","");
                resultWeather.windDirection	  = gsonNowWeather.get("wind").getAsJsonObject().get("dir").toString().replace("\"","");
                resultWeather.weatherCurrent    = gsonNowWeather.get("cond").getAsJsonObject().get("txt").toString().replace("\"","");
                JsonObject gsonTodayWeather = gson.get("daily_forecast").getAsJsonArray().get(0).getAsJsonObject();
                resultWeather.tempatureMax = Integer.valueOf(gsonTodayWeather.get("tmp").getAsJsonObject().get("max").toString().replace("\"",""));
                resultWeather.tempatureMin = Integer.valueOf(gsonTodayWeather.get("tmp").getAsJsonObject().get("min").toString().replace("\"",""));
            }
        }catch(JsonIOException e){
            e.printStackTrace();
        }catch(JsonSyntaxException e){
            e.printStackTrace();
        }
        return resultWeather;
    }
}
