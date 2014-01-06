package com.tumo.reminder;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;

import android.R;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ReminderService extends Service {

	private static final String LIVE_CARD_TAG = "reminder_tag";
	private LiveCard mLiveCard;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		Toast.makeText(this, "ReminderService onCreate", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "ReminderService onStartCommand", Toast.LENGTH_SHORT).show();
		return START_STICKY;
	}
	
	private void publishCard(Context context) {
		if(mLiveCard == null) {
			TimelineManager tm = TimelineManager.from(this);
			mLiveCard = tm.createLiveCard(LIVE_CARD_TAG);
			
//			mLiveCard.setViews(new RemoteViews(context.getPackageName(),
//					R.layout.card_text))
		} else {
			// card is already published
			return;
		}
	}
}
