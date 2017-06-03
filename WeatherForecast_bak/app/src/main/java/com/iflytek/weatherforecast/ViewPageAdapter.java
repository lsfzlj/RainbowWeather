/**
 *
 */
package com.iflytek.weatherforecast;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ViewPageAdapter extends PagerAdapter {
	static final String TAG ="adapter";
	private Context context;
	private String[] setCities;
	int pos;

	private Map<Integer, ViewPageItemView> hashMap;

	public ViewPageAdapter(Context context, String[] setString) {
		this.context = context;
		setCities = setString;
		this.hashMap = new HashMap<Integer, ViewPageItemView>();
	}

	// 返回资源数目
	@Override
	public int getCount() {
		return setCities.length;
	}

	// 初始化要显示的资源
	@Override
	public Object instantiateItem(View container, int position) {
		ViewPageItemView itemView;
		pos=position;
		if (hashMap.containsKey(position)) {
			itemView = hashMap.get(position);
			itemView.reload();
		} else {
			itemView = new ViewPageItemView(context,setCities.length,position);
			String city = setCities[position];
			//初始化界面信息
			itemView.setData(city);
			//Log.d(TAG, "instantiateItem: Position"+position);
			//滑动底部的小圆圈
			//imageView.setLayoutParams();
			itemView.setGroup(context,setCities.length,position);
			hashMap.put(position, itemView);
			((ViewPager) container).addView(itemView);
		}



		return itemView;
	}

	// 进行资源回收
	@Override
	public void destroyItem(View container, int position, Object object) {
		ViewPageItemView itemView = (ViewPageItemView) object;
		itemView.recycle();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
		;
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View view) {
		;
	}

	@Override
	public void finishUpdate(View view) {

		;
	}

}
