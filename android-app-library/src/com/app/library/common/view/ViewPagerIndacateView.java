package com.app.library.common.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.app.library.common.view.AbsTabIndicateView.OnTabChangeListener;

/**
 * 文字切换标签+ViewPager内容区控件
 * 
 * @author savant-pan
 * 
 */
public class ViewPagerIndacateView extends LinearLayout implements OnTabChangeListener, OnPageChangeListener {
	private TextTabIndicateView textTabIndicateView;
	private ViewPager viewPager;

	public ViewPagerIndacateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initView();
	}

	public ViewPagerIndacateView(Context context) {
		super(context);
		this.initView();
	}

	private void initView() {
		this.setOrientation(LinearLayout.VERTICAL);

		this.textTabIndicateView = new TextTabIndicateView(getContext());
		this.viewPager = new ViewPager(getContext());

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		this.addView(textTabIndicateView, params);
		params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.addView(viewPager, params);

		this.textTabIndicateView.setOnTabChangeListener(this);
		this.viewPager.setOnPageChangeListener(this);

	}

	/**
	 * 设置显示标签文字及对应内容布局
	 * 
	 * @param titleViewMap
	 *            标题及对应View map数据
	 */
	public void setupLayout(Map<String, View> titleViewMap) {
		if (titleViewMap == null || titleViewMap.size() == 0) {
			throw new NullPointerException();
		}

		final List<String> textList = new ArrayList<String>();
		final List<View> viewList = new ArrayList<View>();

		final Iterator<Entry<String, View>> iterator = titleViewMap.entrySet().iterator();
		while (iterator.hasNext()) {// 生成数据列表
			final Entry<String, View> item = iterator.next();
			textList.add(item.getKey());
			viewList.add(item.getValue());
		}

		// 初始化TextTabIndicateView及ViewPager
		this.textTabIndicateView.setupLayout(textList);
		this.viewPager.setAdapter(new MyPagerAdapter(viewList));
	}

	@Override
	public void onTabChanged(int position) {
		viewPager.setCurrentItem(position, true);
	}

	@Override
	public void onPageSelected(int position) {
		this.textTabIndicateView.setCurrentTab(position, false);// 无回设置TextTabIndicateView
	}

	private class MyPagerAdapter extends PagerAdapter {
		private List<View> viewList = new ArrayList<View>();

		public MyPagerAdapter(List<View> viewList) {
			this.viewList = viewList;
		}

		@Override
		public int getCount() {
			return viewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			Log.i("voice", arg1 + "----------------destroyItem");
			((ViewPager) arg0).removeView(viewList.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			Log.i("voice", arg1 + "----------------instantiateItem");
			((ViewPager) arg0).addView(viewList.get(arg1));
			return viewList.get(arg1);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

}
