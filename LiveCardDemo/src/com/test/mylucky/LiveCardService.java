package com.test.mylucky;

import com.google.android.glass.sample.stopwatch.R;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

public class LiveCardService extends Service {

	// Tag used to identify the LiveCard in debugging logs.
	private static final String LIVE_CARD_TAG = "my_card";

	// Cached instance of the LiveCard created by the publishCard() method.
	private LiveCard mLiveCard;

	private void publishCard(Context context) 
	{
		if (mLiveCard == null) 
		{
			// Make TimelineManager and create LiveCard on it
			TimelineManager tm = TimelineManager.from(context);
			mLiveCard = tm.createLiveCard(LIVE_CARD_TAG);

			// Make LiveCard view @card_text
			mLiveCard.setViews(new RemoteViews(context.getPackageName(), 
					R.layout.card_text));
			
			// Run DeleteCard class on tap (REQUIRED)
			Intent intent = new Intent(context, DeleteCard.class);
			mLiveCard.setAction(PendingIntent
					.getActivity(context, 0, intent, 0));
			
			// Publish the LiveCard in REVEAL mode (alternative is SILENT)
			mLiveCard.publish(LiveCard.PublishMode.REVEAL);
		}
		else 
		{
			// Card is already published.
			// Do nothing
			return;
		}
	}

	private void unpublishCard(Context context) 
	{
		if (mLiveCard != null)
		{
			mLiveCard.unpublish();
			mLiveCard = null;
		}
	}

	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		publishCard(this);
		return START_STICKY;
	}

	@Override
	public void onDestroy() 
	{
		if (mLiveCard != null && mLiveCard.isPublished()) 
		{
			unpublishCard(this);
		}
		super.onDestroy();
	}

}
