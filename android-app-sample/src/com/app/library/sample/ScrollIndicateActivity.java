package com.app.library.sample;

import com.app.library.common.view.ScrollIndicateView;

import android.app.Activity;
import android.os.Bundle;

public class ScrollIndicateActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scroll);

		ScrollIndicateView scrollIndicateView = (ScrollIndicateView) findViewById(R.id.scroll_indicate_view);
		scrollIndicateView.setOnItemChangeListener(new ScrollIndicateView.OnItemChangeListener() {
			@Override
			public void onPosition(int position, int totalCount) {

			}
		});
		scrollIndicateView.setOnItemChangeListener(new ScrollIndicateView.OnItemChangeListener() {
			@Override
			public void onPosition(int position, int totalCount) {

			}
		});
		final Integer[] resArray = new Integer[] { R.drawable.ic_launcher, R.drawable.ic_launcher };
		scrollIndicateView.setupLayoutByDrawable(resArray);
		scrollIndicateView.show();
	}
}
