/**
 *
 */
package com.iflytek.weatherforecast;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ViewPageItemView extends FrameLayout{
	private TextView chengshi;
	private TextView riqi;
	private TextView tianqizhuangkuang;
	private TextView dangqianwendu;
	private TextView zuidiqiwen;
	private TextView zuigaoqiwen;
	private TextView fengxiang;
	private TextView fengli;

	public ViewPageItemView(Context context, int size, int position) {
		super(context);
		initView(context ,size,position);
	}

	// 初始控件
	private void initView(Context context, int size, int position) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.viewpageitem, null);
		Typeface typeface = Typeface.createFromAsset(context.getAssets(),"newword.ttf");
		//初始换控件
		chengshi = (TextView) view.findViewById(R.id.City_Query);
		riqi = (TextView) view.findViewById(R.id.Date_dis);
		tianqizhuangkuang = (TextView)view.findViewById(R.id.tianqi);
		dangqianwendu = (TextView)view.findViewById(R.id.dangqianwendu);
		zuidiqiwen = (TextView)view.findViewById(R.id.zuidiwendu);
		zuigaoqiwen = (TextView)view.findViewById(R.id.zuigaowendu);
		fengxiang = (TextView)view.findViewById(R.id.fengxiang);
		fengli = (TextView)view.findViewById(R.id.fengli);
		//设置显示天气的字体
		tianqizhuangkuang.setTypeface(typeface);
		dangqianwendu.setTypeface(typeface);
		zuidiqiwen.setTypeface(typeface);
		zuigaoqiwen.setTypeface(typeface);
		fengxiang.setTypeface(typeface);
		fengli.setTypeface(typeface);
		addView(view);
	}

	// 填充数据
	public void setData(String city) {
		Weather weather=new Weather();
		ExecutorService exec = Executors.newCachedThreadPool();
		Future<Weather> futureWeather = exec.submit(new GetWeatherThread(city));
		try{
			weather = futureWeather.get();
		}catch (InterruptedException e){
			e.printStackTrace();
		}catch(ExecutionException e){
			e.printStackTrace();
		}finally {
			exec.shutdown();
		}
		chengshi.setText(weather.city);
		String dateNow=null;
		String[]weekDays={"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		Calendar now = Calendar.getInstance();
		now.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		dateNow=(now.get(Calendar.MONTH)+1)+"月 "+now.get(Calendar.DAY_OF_MONTH)+"日 "+weekDays[now.get(Calendar.DAY_OF_WEEK)-1];
		riqi.setText(dateNow);
		tianqizhuangkuang.setText("天气状况： "+weather.weatherCurrent);
		dangqianwendu.setText("当前温度： "+Integer.toString( weather.tempatureCurrent));
		zuidiqiwen.setText("最低气温： "+Integer.toString(weather.tempatureMin));
		zuigaoqiwen.setText("最高气温： "+Integer.toString(weather.tempatureMax));
		fengli.setText("风力： "+weather.windPower);
		fengxiang.setText("风向： "+weather.windDirection);
	}
	// 资源回收
	public void recycle() {
	}
	// 重新加载资源
	public void reload() {
	}
}
