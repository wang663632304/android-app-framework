package com.app.library.sample;

import com.app.library.LibraryConfig;

import android.app.Application;

/**
 * 在AndroidMainifest.xml中设置该类为程序入口
 * 
 * @author savant
 * 
 */
public class SampleApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		LibraryConfig.init(this, true);// 初始化资源包名
	}
}
