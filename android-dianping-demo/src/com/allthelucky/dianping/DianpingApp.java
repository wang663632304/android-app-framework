package com.allthelucky.dianping;

import android.app.Application;

import com.allthelucky.app.LibraryConfig;

public class DianpingApp  extends Application{
	
	 @Override
	public void onCreate() {
		super.onCreate();
		LibraryConfig.init(this, false);
	}
	 
}
