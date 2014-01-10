package com.test.mylucky;

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		mCard = new Card(this);
		mCard.setText("In how much time?");
		displaySpeechRecognizer();
		
		mCard.setText("About What?");
		displaySpeechRecognizer();
		
		// End this activity
		finish();
	}
	
	private void displaySpeechRecognizer() 
	{
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		startActivityForResult(intent, SPEECH_REQUEST);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	        Intent data) {
	    if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) {
	        List<String> results = data.getStringArrayListExtra(
	                RecognizerIntent.EXTRA_RESULTS);
	        String spokenText = results.get(0);
	        
	        // Do something with spokenText.
			Toast.makeText(this, spokenText, Toast.LENGTH_SHORT).show();
	    }
	    super.onActivityResult(requestCode, resultCode, data);
	}

}
