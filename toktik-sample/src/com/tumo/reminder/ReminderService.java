package com.tumo.reminder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ReminderService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("ReminderService", "Received start id " + startId + ":" + intent);
		Toast.makeText(this, "ReminderService started", 1).show();
		return START_STICKY;
	}
	
}
