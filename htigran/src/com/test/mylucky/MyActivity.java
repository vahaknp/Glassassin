package com.test.mylucky;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.provider.MediaStore;
import com.google.android.glass.media.CameraManager;
import com.google.android.glass.app.Card;

import android.view.View;
import android.widget.Toast;

public class MyActivity extends Activity {

	private static final int TAKE_PICTURE_REQUEST = 1;
	private static final int SPEECH_REQUEST = 0;
	private int iterator_for_name = 0;
	private Handler customHandler = new Handler();

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			
		super.onCreate(savedInstanceState);	
		Card card = new Card(this);
        card.setText("The photo will be captured after 3 seconds");        
        setContentView(card.toView());
		customHandler.postDelayed(takePicture, 3000);
		
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);		    
//		startActivityForResult(intent, TAKE_PICTURE_REQUEST);	 
		
	}
	
	private void displaySpeechRecognizer() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		startActivityForResult(intent, SPEECH_REQUEST);
	}
	
	private Runnable recognize = new Runnable() {
		public void run() {
			displaySpeechRecognizer();
		}
	};
	
	private Runnable takePicture = new Runnable() {
		public void run() {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);		    
			startActivityForResult(intent, TAKE_PICTURE_REQUEST);
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
//		if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) {
//			ArrayList<String> voiceResults = getIntent().getExtras().getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
//			String spokenText = voiceResults.get(0);
//	        
//	        if(spokenText.equals("new")) {
//	        	displaySpeechRecognizer();
//	        } else if (spokenText.equals("ok")) {
//	        	Card card = new Card(this);
//		        card.setText(spokenText);
//		        card.setFootnote("Accepted");
//		        setContentView(card.toView());
//	        } else {
//	        	Card card = new Card(this);
//	            card.setText(spokenText);
//	            card.setFootnote("Retry or accept ");
//	            setContentView(card.toView());
//	            customHandler.postDelayed(recognize, 5000);
//	        }				        
//	    }
		
		if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {			
			
			String picturePath = data.getStringExtra(CameraManager.EXTRA_THUMBNAIL_FILE_PATH);			
		    Card card = new Card(this);		    
	        card.setImageLayout(Card.ImageLayout.FULL);
	        card.addImage(Uri.fromFile(new File(picturePath)));
	        setContentView(card.toView());	        
	    }

	    super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	
}
