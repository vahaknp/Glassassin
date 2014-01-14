package com.test.mylucky;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import com.google.android.glass.app.Card;

public class ReminderStart extends Activity {

	private Card mCard;
	private static final int SPEECH_REQUEST = 0;
	private ArrayList<String> spokenWords = new ArrayList<String>();
	private int speechRecognizerCount = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		mCard = new Card(this);
		displaySpeechRecognizer();
	}
	
	private void displaySpeechRecognizer() 
	{	
		speechRecognizerCount++;
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		startActivityForResult(intent, SPEECH_REQUEST);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) 
		{
			List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			String spokenText = results.get(0);
			spokenWords.add(spokenText);
			Toast.makeText(ReminderStart.this, "Recorded Text", Toast.LENGTH_SHORT).show();
			if (speechRecognizerCount > 2) 
			{
				Toast.makeText(ReminderStart.this, "Asking for another", Toast.LENGTH_SHORT).show();
				displaySpeechRecognizer();
			}
			else
			{
				// Launch serviceW
				launchService();
				// End this activity
				finish();
			}
				
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void launchService()
	{
		// Launch service
		Intent serviceIntent = new Intent();
		serviceIntent.setAction("com.test.mylucky.ReminderService");
		if (extras == null && serviceIntent!= null) 
		{
			serviceIntent.setAction("com.test.mylucky.ReminderService");
			serviceIntent.putExtra("spokenWords", spokenWords);
			startService(serviceIntent);
		}
		else
		{
			Toast.makeText(ReminderStart.this, "twas null", Toast.LENGTH_SHORT).show();
		}
	}

}
