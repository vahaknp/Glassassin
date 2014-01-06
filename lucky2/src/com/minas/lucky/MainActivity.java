package com.minas.lucky;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import com.google.android.glass.app.Card;

public class MainActivity extends Activity {

	private List<String> hardcodedNicks = new ArrayList<String>(Arrays.asList(
			"long hair", "green", "red hat"));
	private List<String> nicks = new ArrayList<String>();
	private Card mCard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mCard = new Card(this);
		
		
		displaySpeechRecognizer();
	}

	private static final int SPEECH_REQUEST = 0;

	private void displaySpeechRecognizer() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		startActivityForResult(intent, SPEECH_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) {
			List<String> results = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			String spokenText = results.get(0);

			if (!spokenText.equals("stop")) {
				nicks.add(spokenText);
				displaySpeechRecognizer();
			} else {
				hardcodedNicks.retainAll(nicks);

				if (Collections.indexOfSubList(hardcodedNicks, nicks) != -1) {
					mCard.setText(hardcodedNicks.size() + " --- " + hardcodedNicks);
					mCard.setFootnote("number of matches");
					setContentView(mCard.toView());
				}

			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
