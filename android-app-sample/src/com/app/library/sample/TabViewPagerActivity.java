package com.app.library.sample;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.app.library.common.view.ViewPagerIndacateView;

public class TabViewPagerActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_viewpager);
		
		ViewPagerIndacateView tabWithViewPagerView = (ViewPagerIndacateView)findViewById(R.id.tab_viewpager);
		
		final Map<String, View> map = new HashMap<String, View>();
		map.put("TAB1", LayoutInflater.from(this).inflate(R.layout.pager_0, null));
		map.put("TAB2", LayoutInflater.from(this).inflate(R.layout.pager_1, null));
		tabWithViewPagerView.setupLayout(map );
	}
}
