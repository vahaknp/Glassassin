package com.test.mylucky;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.glass.app.Card;

public class CardActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Card card = new Card(this);
		card.setText("Hi Tumo");
		card.setFootnote("glass workshop");

		setContentView(card.toView());
	}

}
