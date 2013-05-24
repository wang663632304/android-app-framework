package com.app.library.sample;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.app.library.common.view.ViewPagerIndacateView;

/**
 * TAB切换标签+ViewPager控件示例，滑动ViewPager也可改变TAB内容
 * 
 * @author savant-pan
 * 
 */
public class TabViewPagerActivity extends Activity {
	private ViewPagerIndacateView viewPagerIndacateView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_viewpager);

		this.viewPagerIndacateView = (ViewPagerIndacateView) findViewById(R.id.tab_viewpager);

		final Map<String, View> map = new HashMap<String, View>();
		map.put("TAB1", LayoutInflater.from(this).inflate(R.layout.pager_0, null));
		map.put("TAB2", LayoutInflater.from(this).inflate(R.layout.pager_1, null));
		this.viewPagerIndacateView.setupLayout(map);
	}
}
