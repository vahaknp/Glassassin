package com.tumo.fungame;

import android.app.Application;
import android.content.Context;

public class TumoGameApp extends Application {

	private static TumoGameApp instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;

	}

	public static Context getContext() {
		return instance.getApplicationContext();
	}

}
