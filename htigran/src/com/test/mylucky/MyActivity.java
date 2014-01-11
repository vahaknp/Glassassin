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
	private String picturePath;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			
		super.onCreate(savedInstanceState);	
		Toast.makeText(getApplicationContext(), "The picture will be captured in 3 seconds.", Toast.LENGTH_SHORT).show();
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
	
	private void takePicture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);		    
		startActivityForResult(intent, TAKE_PICTURE_REQUEST);
	}
	
	private Runnable takePicture = new Runnable() {
		public void run() {
			takePicture();
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) {			
			List<String> results = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			String spokenText = results.get(0);
	        
	        if(spokenText.equals("yes") || spokenText.equals("Yes")) {
	        	Toast.makeText(getApplicationContext(), "The picture will be captured in 3 seconds.", Toast.LENGTH_SHORT).show();
	    		customHandler.postDelayed(takePicture, 3000);
	        } else if (spokenText.equals("No") || spokenText.equals("no")) {
	        	Intent pictureAccept = new Intent(this, CardActivity.class);
	        	pictureAccept.putExtra("picturePath",picturePath);
	        	startActivity(pictureAccept);
	        } else {
	        	displaySpeechRecognizer();
	        }			        
	    }
		
		if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {			
			
			picturePath = data.getStringExtra(CameraManager.EXTRA_THUMBNAIL_FILE_PATH);			
		    Card card = new Card(this);		    
	        card.setImageLayout(Card.ImageLayout.FULL);
	        card.addImage(Uri.fromFile(new File(picturePath)));
	        setContentView(card.toView());
	        Toast.makeText(getApplicationContext(), "Do yo want to take new picture?", Toast.LENGTH_SHORT).show();
	        customHandler.postDelayed(recognize, 3000);
	    }

	    super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	
}
