package com.app.library.sample;


import android.os.Bundle;

import com.app.library.common.BaseActivity;
import com.app.library.common.view.TextTabIndicateView;

public class TabIndicateActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab);
		
		TextTabIndicateView tab = (TextTabIndicateView) findViewById(R.id.tab);
		tab.setupLayout(new String[] { "TAB1", "TAB2", "TAB3" });
		tab.setOnTabChangeListener(new TextTabIndicateView.OnTabChangeListener() {
			@Override
			public void onTabChanged(int position) {
				showToast("position:" + position);
			}
		});
	}
	
}
