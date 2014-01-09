package com.tumo.fungame.service;

import java.util.Calendar;
import java.util.Date;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;
import com.tumo.fungame.R;
import com.tumo.fungame.activity.LiveCardMenuActivity;
import com.tumo.fungame.activity.SelectDbActivity;

public class LiveCardService extends Service {

	public class LocalBinder extends Binder {

		// Return this instance of LiveCardService
		public LiveCardService getService() {
			return LiveCardService.this;
		}
	}

	private final IBinder mBinder = new LocalBinder();

	// Tag used to identify the LiveCard in debugging logs.
	private static final String LIVE_CARD_TAG = "game_card";

	// Cached instance of the LiveCard created by the publishCard() method.
	private LiveCard mLiveCard;

	private String name;
	private Uri imageUri;

	private void publishCard(Context context) {
		if (mLiveCard == null) {
			TimelineManager tm = TimelineManager.from(context);
			mLiveCard = tm.createLiveCard(LIVE_CARD_TAG);

			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.game_card);
			remoteViews.setCharSequence(R.id.livecard_content, "setText", name);
			remoteViews.setImageViewUri(R.id.livecard_image, imageUri);
			mLiveCard.setViews(remoteViews);
			Intent intent = new Intent(context, LiveCardMenuActivity.class);
			mLiveCard.setAction(PendingIntent
					.getActivity(context, 0, intent, 0));
			mLiveCard.publish(LiveCard.PublishMode.REVEAL);
		} else {
			// Card already published
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.game_card);
			remoteViews.setCharSequence(R.id.livecard_content, "setText", name);
			remoteViews.setImageViewUri(R.id.livecard_image, imageUri);
			mLiveCard.setViews(remoteViews);
		}
	}

	private void unpublishCard(Context context) {
		if (mLiveCard != null) {
			mLiveCard.unpublish();
			mLiveCard = null;
		}
	}

	public void updateCard(Context context) {
		if (mLiveCard == null) {
			publishCard(this);
		} else {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.game_card);

			Calendar calendar = Calendar.getInstance();
			Date date = calendar.getTime();

			remoteViews.setCharSequence(R.id.livecard_content, "setText",
					date.toString());
			mLiveCard.setViews(remoteViews);

			/*
			 * works without this
			 */

			// Intent intent = new Intent(context, CardActivity.class);
			// mLiveCard.setAction(PendingIntent
			// .getActivity(context, 0, intent, 0));
			// mLiveCard.publish(LiveCard.PublishMode.REVEAL);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "Service_start", Toast.LENGTH_SHORT).show();
		Bundle bundle = intent.getExtras();
		name = bundle.getString(SelectDbActivity.KEY_NAME);
		imageUri = bundle.getParcelable(SelectDbActivity.KEY_IMAGE);
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
