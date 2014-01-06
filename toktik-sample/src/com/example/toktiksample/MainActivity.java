package com.example.toktiksample;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import com.google.android.glass.app.Card;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<String> voiceResults = getIntent().getExtras()
        		.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
        
        Card card = new Card(this);
        card.setText("This is sample app! That works.");
        card.setFootnote(voiceResults.get(0) + " is great place!");

        setContentView(card.toView());
	}

}
