package com.tumo.reminder;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Reminder {
	HashMap unitToSecond = new HashMap(); 
	
	public void Reminder(String transcription) {
		
	}
	
	private void startTimer() {
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				
			}
		}, 10000);
	}
	
	private void stringToSeconds()
	{
		
	}
}
