package com.tumo.fungame.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;
import com.tumo.fungame.R;
import com.tumo.fungame.activity.LiveCardMenuActivity;
import com.tumo.fungame.activity.SelectDbActivity;
import com.tumo.fungame.model.Nick;
import com.tumo.fungame.model.Person;

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

	private Person person;

	private void publishCard(Context context) {

		person = new Person();

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

	public void updateCard(Context context, Nick nick) {
		if (mLiveCard == null) {
			publishCard(this);
		} else {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.game_card);

			person.getNicks().add(nick);
			remoteViews.setCharSequence(R.id.livecard_content, "setText",
					person.nicksToString());
			remoteViews.setImageViewUri(R.id.livecard_image, imageUri);
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
		Bundle bundle = intent.getExtras();
		name = bundle.getString(SelectDbActivity.KEY_NAME);
		imageUri = bundle.getParcelable(SelectDbActivity.KEY_IMAGE);
		publishCard(this);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		if (mLiveCard != null && mLiveCard.isPublished()) {
			unpublishCard(this);
		}
		super.onDestroy();
	}
}
