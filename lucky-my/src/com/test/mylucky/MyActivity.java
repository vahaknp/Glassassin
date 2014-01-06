package com.test.mylucky;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;

public class MyActivity extends Activity {

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
	        mLiveCard.setAction(PendingIntent.getActivity(context, 0,
	                intent, 0));
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		publishCard(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setContentView(R.layout.card_text);
	}
}
