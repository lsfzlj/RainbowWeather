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
import android.view.Window;
import android.widget.Button;

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

		Set<String> temp = new HashSet<>();
		Set<String> temp2=new TreeSet<>();
		temp = getSharedPreferences("cities", MODE_PRIVATE).getStringSet("cities", temp);
		citiesSHaredPreference = MainActivity.getCities();
		initView();
		Button setting = (Button) findViewById(R.id.setting);
		setting.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.setting) {
			Intent intent = new Intent();
			intent.setClass(this, City_Column.class);
			startActivity(intent);
		}
	}

	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.viewpage);
		adapter = new ViewPageAdapter(context, citiesSHaredPreference);
		viewPager.setAdapter(adapter);
	}

	class MyComparator implements Comparator<String> {

		@Override
		public int compare(String o1, String o2) {
			return o2.compareTo(o1);//降序排列
		}
	}
}
