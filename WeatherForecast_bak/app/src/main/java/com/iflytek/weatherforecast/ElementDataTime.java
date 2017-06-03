package com.iflytek.weatherforecast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/27.
 */

public class ElementDataTime {
    private  String dateTime ;
    public ElementDataTime(Date date){
        dateTime = date.toString();
    }
    public void  print(){
        System.out.println(dateTime);
    }
    public Date getDate(){
        Date mDate = new Date();
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            mDate = formatter.parse(dateTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mDate;
    }

}
