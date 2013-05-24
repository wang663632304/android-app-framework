package com.app.library.sample;

import com.app.library.common.view.AutoScrollIndicateView;
import com.app.library.common.view.ScrollIndicateView;

import android.app.Activity;
import android.os.Bundle;

public class AutoScrollIndicateActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scroll_auto);

		AutoScrollIndicateView scrollIndicateView = (AutoScrollIndicateView) findViewById(R.id.auto_scroll_indicate_view);
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
		scrollIndicateView.setBroadcastEnable(true);
		scrollIndicateView.setBroadCastTimes(5);
		scrollIndicateView.setBroadcastTimeIntevel(2 * 1000, 3 * 1000);
		scrollIndicateView.setupLayoutByDrawable(resArray);
		scrollIndicateView.show();
	}

}
