/**
 *
 */
package com.iflytek.weatherforecast;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ViewPageItemView extends FrameLayout {

	/** 图片的名称 */
	private ImageView[] imageViews;
	private ImageView imageView;
	private ViewGroup group;
	//
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
				R.layout.viewpage_itemview, null);
		chengshi = (TextView) view.findViewById(R.id.City_Query);
		riqi = (TextView) view.findViewById(R.id.Date_dis);
		tianqizhuangkuang = (TextView)view.findViewById(R.id.tianqi);
	//	tianqizhuangkuang.setTypeface(Typeface.createFromAsset(getAssets(),"newword.ttf"));
		dangqianwendu = (TextView)view.findViewById(R.id.dangqianwendu);
		zuidiqiwen = (TextView)view.findViewById(R.id.zuidiwendu);
		zuigaoqiwen = (TextView)view.findViewById(R.id.zuigaowendu);
		fengxiang = (TextView)view.findViewById(R.id.fengxiang);
		fengli = (TextView)view.findViewById(R.id.fengli);
		group = (ViewGroup) view.findViewById(R.id.viewGroup);
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
		dateNow=now.get(Calendar.MONTH)+"月 "+now.get(Calendar.DAY_OF_MONTH)+"日 "+weekDays[now.get(Calendar.DAY_OF_WEEK)-1];
		riqi.setText(dateNow);
		tianqizhuangkuang.setText("天气状况： "+weather.weatherCurrent);
		dangqianwendu.setText("当前温度： "+Integer.toString( weather.tempatureCurrent));
		zuidiqiwen.setText("最低气温： "+Integer.toString(weather.tempatureMin));
		zuigaoqiwen.setText("最高气温： "+Integer.toString(weather.tempatureMax));
		fengli.setText("风力： "+weather.windPower);
		fengxiang.setText("风向： "+weather.windDirection);
	}
	//实现滑动 导航小圆圈
	public void setGroup(Context context, int len , int position){
		imageViews = new ImageView[len];
		group = (ViewGroup) findViewById(R.id.viewGroup);
		for(int i=0;i<len;i++){
			imageView = new ImageView(context);
			if(i==position){
				imageView.setLayoutParams(new ViewGroup.LayoutParams(60,60));
				imageViews[i] = imageView;
				imageViews[i].setBackgroundResource(R.drawable.circlemy);  //page_indicator_focused
				imageView.setPadding(0, 40, 40, 40);
			//	imageView.layout
			}else{
				imageView.setLayoutParams(new ViewGroup.LayoutParams(40,40));
				imageView.setPadding(10, 40, 0, 40);
				imageViews[i] = imageView;
				imageViews[i].setBackgroundResource(R.drawable.circlemy);  //page_indicator
			}
			group.addView(imageViews[i]);
		}
	}
	// 资源回收
	public void recycle() {
	}
	// 重新加载资源
	public void reload() {
	}

}
