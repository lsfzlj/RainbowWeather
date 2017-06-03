package com.iflytek.weatherforecast;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Created by Administrator on 2017/5/16.
 */
public class Weather implements Serializable{
    String city;
    String status;
    String weatherCurrent;
    int tempatureCurrent;
    int tempatureMax;
    int tempatureMin;
    String windDirection;
    String windPower;

    void print(){
        System.out.println("the weather status is :"+this.status);
        if(this.status.equals("ok"))
            System.out.println("current"+weatherCurrent+"	"+tempatureCurrent+"	"+tempatureMax
                    +"	"+tempatureMin+"	"
                    +windDirection+windPower);
    }
}