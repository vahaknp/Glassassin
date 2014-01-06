package com.example.lucky;

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
			"long hair", "green eyes"));;
	private List<String> nicks = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		displaySpeechRecognizer();

		Card card = new Card(this);

		if (Collections.indexOfSubList(hardcodedNicks, nicks) != -1) {
			card.setText(nicks.get(0));
			card.setFootnote("glass workshop");
		}

		setContentView(card.toView());
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
			// Do something with spokenText.

			if (spokenText != "done") {
				nicks.add(spokenText);
				displaySpeechRecognizer();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
