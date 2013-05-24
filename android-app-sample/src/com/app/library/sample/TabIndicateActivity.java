package com.app.library.sample;

import android.os.Bundle;

import com.app.library.common.BaseActivity;
import com.app.library.common.view.TextTabIndicateView;

/**
 * TAB切换标签控件示例
 * 
 * @author savant-pan
 * 
 */
public class TabIndicateActivity extends BaseActivity {
	private TextTabIndicateView textTabIndicateView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab);

		this.textTabIndicateView = (TextTabIndicateView) findViewById(R.id.tab);
		this.textTabIndicateView.setupLayout(new String[] { "TAB1", "TAB2", "TAB3" });
		this.textTabIndicateView.setOnTabChangeListener(new TextTabIndicateView.OnTabChangeListener() {
			@Override
			public void onTabChanged(int position) {
				showToast("position:" + position);
			}
		});
	}

}
