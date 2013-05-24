package com.app.library.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SampleMainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.button1).setOnClickListener(this);
		findViewById(R.id.button2).setOnClickListener(this);
		findViewById(R.id.button3).setOnClickListener(this);
		findViewById(R.id.button4).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		final int resId = v.getId();
		if (resId == R.id.button1) {
			startActivity(new Intent(this, TabIndicateActivity.class));
		} else if (resId == R.id.button2) {
			startActivity(new Intent(this, TabViewPagerActivity.class));
		} else if (resId == R.id.button3) {
			startActivity(new Intent(this, ScrollIndicateActivity.class));
		} else if (resId == R.id.button4) {
			startActivity(new Intent(this, AutoScrollIndicateActivity.class));
		} else {
			// add if any
		}
	}

}
