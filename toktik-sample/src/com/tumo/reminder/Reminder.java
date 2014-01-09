package com.tumo.reminder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class Reminder {
	ReminderService service;
	Integer seconds;
	String about;
	
	HashMap<String, Integer> unitToSecond = new HashMap<String, Integer>(); 
	
	public Reminder(ReminderService rs, String transcription) {
		service = rs;
		
		unitToSecond.put("second", 1);
		unitToSecond.put("minute", 60);
		unitToSecond.put("hour", 60 * 60);
		
		try {
			seconds = stringToSeconds(transcription);
			about = stringToAbout(transcription);
			startTimer();
		} catch(Exception e) {
			Toast.makeText(service, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	private void startTimer() {
		final Context ctx = service;
		
		Handler mHandler = new Handler();
		Runnable makeToast = new Runnable() {
		    public void run() {
		    	Toast.makeText(ctx, about, Toast.LENGTH_LONG).show();
		    }
		};
		mHandler.postDelayed(makeToast, seconds * 1000);
	}
	
	private Integer stringToSeconds(String transcription) throws Exception {
		transcription = transcription.toLowerCase();
		
		String unitKey = null; 
		Iterator it = unitToSecond.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			if(transcription.contains(pair.getKey().toString())) {
				unitKey = pair.getKey().toString();
				break;
			}
		}
		
		// take first substring until space
		Integer amount = Integer.parseInt(transcription.substring(0, transcription.indexOf(' ')));
		
		if(unitKey == null || amount == null) {
			throw new Exception("I don't understand what you speak");
		}
		
		return amount * unitToSecond.get(unitKey);
	}
	
	public String stringToAbout(String transcription) throws Exception {
		if(!transcription.contains("about ")) {
			throw new Exception("I don't understand what to remind");
		}
		
		int about_index = transcription.indexOf("about ") + "about ".length();
		return transcription.substring(about_index);
	}
}
