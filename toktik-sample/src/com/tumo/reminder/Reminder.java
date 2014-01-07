package com.tumo.reminder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.widget.Toast;

public class Reminder {
	ReminderService service;
	String seconds;
	
	HashMap<String, Integer> unitToSecond = new HashMap<String, Integer>(); 
	
	public Reminder(ReminderService rs, String transcription) {
		service = rs;
		
		unitToSecond.put("second", 1);
		unitToSecond.put("minute", 60);
		unitToSecond.put("hour", 60 * 60);
		
		try {
			Integer seconds = stringToSeconds(transcription);
		} catch(Exception e) {
			Toast.makeText(service, e.getMessage(), Toast.LENGTH_LONG).show();
		}		
	}
	
	private void startTimer(Integer seconds) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Toast.makeText(service, "Reminding!", Toast.LENGTH_LONG);
			}
		}, seconds);
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
}
