package com.allthelucky.dianping;

import com.app.library.LibraryConfig;

import android.app.Application;

public class DianpingApp  extends Application{
	
	 @Override
	public void onCreate() {
		super.onCreate();
		LibraryConfig.init(this, false);
	}
	 
}
