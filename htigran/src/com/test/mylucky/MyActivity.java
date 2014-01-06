package com.test.mylucky;

import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.provider.MediaStore;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;
import com.google.android.glass.app.Card;
import android.net.Uri;

public class MyActivity extends Activity {

	private static final int TAKE_PICTURE_REQUEST = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			
		super.onCreate(savedInstanceState);
		Card card = new Card(this);
				
		ArrayList<String> voiceResults = getIntent().getExtras()
				.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);

		String res = voiceResults.get(0);
		
		if(res.equals("compare")) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);		    
		    startActivityForResult(intent, TAKE_PICTURE_REQUEST);	    
		    System.out.println(intent);
//	        card.setText("Comparing photo");       
	        
//	        setContentView(card.toView());
		}		
		
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
