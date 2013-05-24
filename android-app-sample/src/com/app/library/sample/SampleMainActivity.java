package com.app.library.sample;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.app.library.common.manager.DialogManager;

/**
 * library中控件及工具使用示例
 * 
 * @author savant-pan
 * 
 */
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

	/**
	 * 返回键监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showExitDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 退出确认提示
	 */
	private void showExitDialog() {
		DialogManager.showDialog(SampleMainActivity.this, "确定退出应用？", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == DialogInterface.BUTTON_POSITIVE) {
					Process.killProcess(Process.myPid());
				}
			}
		});
	}

}
