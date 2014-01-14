package com.test.mylucky;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.IBinder;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;

public class ReminderService extends Service 
{
	// Tag used to identify the LiveCard in debugging logs.
	private static final String LIVE_CARD_TAG = "my_card";
	// Cached instance of the LiveCard created by the publishCard() method.
	private static LiveCard mLiveCard;
	private TimelineManager tm;
	private static long startTime;
	private static long elapsedTime;
	private static long totalTime;
	private static String reminderSubject;
	
	// Alarm vars
	public static SoundPool mSoundPool;
	private static int mTimerFinishedSoundId;
    private static final int SOUND_PRIORITY = 1;
    private static final int MAX_STREAMS = 1;
    public static int soundStreamID;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		// Alarm initialization
        mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        mTimerFinishedSoundId = mSoundPool.load(this, R.raw.timer_finished, SOUND_PRIORITY);
        
        
		ArrayList<String> voiceResults = intent.getExtras().getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
		
		//totalTime = Integer.parseInt(voiceResults.get(0));
		
		publishCard(this);
		
		String[] commands = voiceResults.get(0).split(" ");
		totalTime = Integer.parseInt(commands[1]) * 1000;
		reminderSubject = "";
		for(int i = 4; i < commands.length; i++)
			reminderSubject += commands[i] + " ";
		
		startTime = SystemClock.elapsedRealtime();
		new Timer().scheduleAtFixedRate(new updateCardToTimeTask(), 0, 1000);
		return START_STICKY;
	}
	
	private void publishCard(Context context) 
	{
		if (mLiveCard == null) 
		{
			// Make TimelineManager and create LiveCard on it
			mLiveCard = tm.createLiveCard(LIVE_CARD_TAG);

			// Make LiveCard view @card_text
			mLiveCard.setViews(new RemoteViews(context.getPackageName(), R.layout.card_text));
			
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
	
	public static void updateCard(Context context, String cardText) {
		if(mLiveCard == null) {
			return;
		} else {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.card_text);	
			remoteViews.setCharSequence(R.id.livecard_content, "setText", cardText);
			mLiveCard.setViews(remoteViews);
		}
	}
	
    class updateCardToTimeTask extends TimerTask 
    {
        public void run()
        {
        	ReminderService.elapsedTime = SystemClock.elapsedRealtime() - ReminderService.startTime;
        	String secondsRemaining = Long.toString(ReminderService.miltoSec(totalTime - elapsedTime));
        	
        	if(Integer.parseInt(secondsRemaining) <= 0)
        	{
        		ReminderService.updateCard(ReminderService.this, ReminderService.reminderSubject + "!!!");
        		playSound();
        		
        	}
        	else
        		ReminderService.updateCard(ReminderService.this, "Reminding about : " + ReminderService.reminderSubject + "\n" + "in " + secondsRemaining + " seconds");
        }
    }
    
    public static int miltoSec(long mills)
    {
    	return (int) (mills / 1000);
    }
    
    private void playSound() {
    	soundStreamID = mSoundPool.play(mTimerFinishedSoundId,
		                       1 /* leftVolume */,
		                       1 /* rightVolume */,
		                       SOUND_PRIORITY,
		                       0 /* loop */,
		                       1 /* rate */);
   }
    
    public static void stopSound() 
    {
        mSoundPool.stop(soundStreamID);
    }
   
	@Override
	public void onCreate() 
	{
		tm = TimelineManager.from(this);
	}
	
	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
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
	
	private void unpublishCard(Context context) 
	{
		if (mLiveCard != null)
		{
			mLiveCard.unpublish();
			mLiveCard = null;
		}
	}
	
}
