package com.tumo.reminder;

import java.util.ArrayList;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.widget.Toast;

public class ReminderService extends Service {

	private static final String LIVE_CARD_TAG = "reminder_tag";
	private LiveCard mLiveCard;
	private TimelineManager tm;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		tm = TimelineManager.from(this);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		ArrayList<String> voiceResults = intent.getExtras()
			.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
		String transcription = voiceResults.get(0);
		
		Reminder reminder = new Reminder(this, transcription);
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		if (mLiveCard != null && mLiveCard.isPublished()) {
			mLiveCard.unpublish();
			mLiveCard = null;
		}
		super.onDestroy();
	}
	
	private void publishCard(Context context) {
		if(mLiveCard == null) {
			mLiveCard = tm.createLiveCard(LIVE_CARD_TAG);
			
//			mLiveCard.setViews(new RemoteViews(context.getPackageName(),
//					R.layout.card_text))
		} else {
			// card is already published
			return;
		}
	}
}
