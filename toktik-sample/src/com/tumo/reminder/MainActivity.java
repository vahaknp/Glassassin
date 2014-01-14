package com.tumo.reminder;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import com.google.android.glass.app.Card;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ArrayList<String> voiceResults = getIntent().getExtras()
        		.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
        
        // start reminder service
        Intent serviceIntent = new Intent();
        serviceIntent.setAction("com.test.mylucky.ReminderService");
        serviceIntent.putExtra("time", voiceResults.get(0).toLowerCase());
        startService(serviceIntent);
	}

}
