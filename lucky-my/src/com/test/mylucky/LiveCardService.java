package com.test.mylucky;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

public class LiveCardService extends Service {

	// Tag used to identify the LiveCard in debugging logs.
	private static final String LIVE_CARD_TAG = "my_card";

	// Cached instance of the LiveCard created by the publishCard() method.
	private LiveCard mLiveCard;

	private void publishCard(Context context) {
		if (mLiveCard == null) {
			TimelineManager tm = TimelineManager.from(context);
			mLiveCard = tm.createLiveCard(LIVE_CARD_TAG);

			mLiveCard.setViews(new RemoteViews(context.getPackageName(),
					R.layout.card_text));
			Intent intent = new Intent(context, CardActivity.class);
			mLiveCard.setAction(PendingIntent
					.getActivity(context, 0, intent, 0));
			mLiveCard.publish(LiveCard.PublishMode.SILENT);
		} else {
			// Card is already published.
			return;
		}
	}

	private void unpublishCard(Context context) {
		if (mLiveCard != null) {
			mLiveCard.unpublish();
			mLiveCard = null;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "Service_start", Toast.LENGTH_SHORT).show();
		publishCard(this);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Service_stop", Toast.LENGTH_SHORT).show();
		if (mLiveCard != null && mLiveCard.isPublished()) {
			unpublishCard(this);
		}
		super.onDestroy();
	}

}
