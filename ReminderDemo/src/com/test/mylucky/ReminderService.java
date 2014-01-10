package com.test.mylucky;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class ReminderService extends Service {

	private void ReminderService(Context context) 
	{
		
	}

	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		return 0;
	}

	@Override
	public void onDestroy() 
	{
		super.onDestroy();
	}
}
