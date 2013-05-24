package com.app.library.sample;

import com.app.library.common.view.ScrollIndicateView;

import android.app.Activity;
import android.os.Bundle;

public class ScrollIndicateActivity extends Activity {
	private ScrollIndicateView scrollIndicateView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scroll);

		this.scrollIndicateView = (ScrollIndicateView) findViewById(R.id.scroll_indicate_view);
		this.scrollIndicateView.setOnItemChangeListener(new ScrollIndicateView.OnItemChangeListener() {
			@Override
			public void onPosition(int position, int totalCount) {

			}
		});
		this.scrollIndicateView.setOnItemChangeListener(new ScrollIndicateView.OnItemChangeListener() {
			@Override
			public void onPosition(int position, int totalCount) {

			}
		});

		this.initView();
	}

	private void initView() {
		final Integer[] resArray = new Integer[] { R.drawable.ic_launcher, R.drawable.ic_launcher };
		this.scrollIndicateView.setupLayoutByDrawable(resArray);
		this.scrollIndicateView.setIndicateStyle(ScrollIndicateView.INDICATE_USERGUIDE_STYLE);
		this.scrollIndicateView.show();
	}
}
