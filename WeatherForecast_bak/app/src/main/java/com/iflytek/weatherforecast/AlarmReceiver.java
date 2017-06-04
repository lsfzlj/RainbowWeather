package com.iflytek.weatherforecast;

/**
 * Created by Administrator on 2017/6/4.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        //设置通知内容并在onReceive()这个函数执行时开启

        // 获取城市天气，在通知栏中显示 城市+温度+天气状况 信息
        String city = intent.getAction();
        Weather wea = new Weather();
        ExecutorService executorService2 = Executors.newCachedThreadPool();
        Future<Weather> futureWeather2 = executorService2.submit(new GetWeatherThread(city));
        try {
            wea=futureWeather2.get();
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }finally {
            executorService2.shutdown();
        }
        //需要显示ViewPageActivity，将待显示的城市放在最前端显示
        MainActivity.addCityThread addNewCity = new MainActivity.addCityThread(city);
        Thread reStorCity = new Thread(addNewCity);
        reStorCity.start();

        String messager = wea.city+"的当前温度为"+wea.tempatureCurrent+"度  "+wea.weatherCurrent;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, ViewPageActivity.class), 0);
        Notification notify = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.icon)
                .setTicker("TicketText"+"您有新消息，注意查收")
                .setContentText(messager)
                .setContentIntent(pendingIntent).setNumber(1).build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, notify);
        Log.d("alarmreceiver", "onReceive: ");
    }
}
