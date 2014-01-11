package com.test.mylucky;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import com.google.android.glass.app.Card;

public class CardActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		ArrayList<String> voiceResults = getIntent().getExtras()
//				.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);

		Card card = new Card(this);
		card.setText("You gues a person");
//		card.setText(voiceResults.get(0));
		card.setFootnote("glass workshop");

		setContentView(card.toView());
	}

}
