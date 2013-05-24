package com.app.library.sample;

import com.app.library.LibraryConfig;

import android.app.Application;

public class SampleApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		LibraryConfig.init(this, true);
	}
}
