/**
 * 
 */
package com.iflytek.weatherforecast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class ViewPageActivity extends Activity implements View.OnClickListener {
	private ViewPager viewPager;
	private ViewPageAdapter adapter;
	private String []citiesSHaredPreference;
	private Context context = ViewPageActivity.this;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_viewpage);
		citiesSHaredPreference = MainActivity.getCities();
		initView();
		ViewGroup viewGroup = (ViewGroup)findViewById(R.id.viewGroup);
		//初始化viewpage导航 圆圈
		for(int i=0;i<citiesSHaredPreference.length;i++){
			ImageView imageView = new ImageView(this.context);
			imageView.setLayoutParams(new ViewGroup.LayoutParams(50,50));
			imageView.setBackgroundResource(R.drawable.circlemy);
			if(i!=0)
				imageView.setAlpha((float)(0.5));
			viewGroup.addView(imageView);
		}
		Button setting = (Button) findViewById(R.id.setting);
		setting.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.setting) {
			Intent intent = new Intent();
			intent.setClass(this, City_Column.class);
			finish();
			startActivity(intent);
		}
	}

	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.viewpage);
		adapter = new ViewPageAdapter(context, citiesSHaredPreference);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {


		}

		public void onPageSelected(int position) {
			Log.d("viewpageiacitivity", "onPageSelected: "+position);
			ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
			for(int i=0;i<citiesSHaredPreference.length;i++){
				if(i==position)
					group.getChildAt(i).setAlpha((float) 1);
				else
					group.getChildAt(i).setAlpha((float) 0.5);
			}

		}
	}
}
