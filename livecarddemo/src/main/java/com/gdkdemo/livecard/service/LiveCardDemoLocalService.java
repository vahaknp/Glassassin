package com.gdkdemo.livecard.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;
import com.gdkdemo.livecard.common.LiveCardDemoConstants;
import com.gdkdemo.livecard.LiveCardDemoActivity;
import com.gdkdemo.livecard.R;


// ...
public class LiveCardDemoLocalService extends Service
{
    // "Life cycle" constants

    // [1] Starts from this..
    private static final int STATE_NORMAL = 1;

    // [2] When panic action has been triggered by the user.
    private static final int STATE_PANIC_TRIGGERED = 2;

    // [3] Note that cancel, or successful send, etc. change the state back to normal
    // These are intermediate states...
    private static final int STATE_CANCEL_REQUESTED = 4;
    private static final int STATE_CANCEL_PROCESSED = 8;
    private static final int STATE_PANIC_PROCESSED = 16;
    // ....

    // Global "state" of the service.
    // Currently not being used...
    private int currentState;


    // For live card
    private LiveCard liveCard;


    // No need for IPC...
    public class LocalBinder extends Binder {
        public LiveCardDemoLocalService getService() {
            return LiveCardDemoLocalService.this;
        }
    }
    private final IBinder mBinder = new LocalBinder();


    public LiveCardDemoLocalService()
    {
        super();
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        currentState = STATE_NORMAL;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i("Received start id " + startId + ": " + intent);
        onServiceStart();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // ????
        onServiceStart();
        return mBinder;
    }

    @Override
    public void onDestroy()
    {
        // ???
        onServiceStop();

        super.onDestroy();
    }


    // Service state handlers.
    // ....

    private boolean onServiceStart()
    {
        Log.d("onServiceStart() called.");

        // TBD:
        // Publish live card...
        // ....
        publishCard(this);
        // ....

        currentState = STATE_NORMAL;
        return true;
    }

    private boolean onServicePause()
    {
        Log.d("onServicePause() called.");
        return true;
    }
    private boolean onServiceResume()
    {
        Log.d("onServiceResume() called.");
        return true;
    }

    private boolean onServiceStop()
    {
        Log.d("onServiceStop() called.");

        // TBD:
        // Unpublish livecard here
        // .....
        unpublishCard(this);
        // ...

        return true;
    }


    // For live cards...

    private void publishCard(Context context)
    {
        Log.d("publishCard() called.");
        if (liveCard == null) {
            String cardId = "livecarddemo_card";
            TimelineManager tm = TimelineManager.from(context);
            liveCard = tm.createLiveCard(cardId);

            liveCard.setViews(new RemoteViews(context.getPackageName(),
                    R.layout.livecard_livecarddemo));
            Intent intent = new Intent(context, LiveCardDemoActivity.class);
            liveCard.setAction(PendingIntent.getActivity(context, 0, intent, 0));
            liveCard.publish(LiveCard.PublishMode.SILENT);
        } else {
            // Card is already published.
            return;
        }
    }

    private void unpublishCard(Context context)
    {
        Log.d("unpublishCard() called.");
        if (liveCard != null) {
            liveCard.unpublish();
            liveCard = null;
        }
    }


}
