package com.test.mylucky;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.provider.MediaStore;
import com.google.android.glass.media.CameraManager;
import com.google.android.glass.app.Card;
import android.widget.Toast;

public class MyActivity extends Activity {

	private static final int TAKE_PICTURE_REQUEST = 1;
	private static final int SPEECH_REQUEST = 0;
	private int iterator_for_name = 0;
	private Map<String, String> register_player = new HashMap<String, String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			
		super.onCreate(savedInstanceState);		
		
//		if(res.equals("compare")) {			
//			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);		    
//		    startActivityForResult(intent, TAKE_PICTURE_REQUEST);		    
//		}  
		
	}
	
	private void displaySpeechRecognizer() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		startActivityForResult(intent, SPEECH_REQUEST);
	}	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) {
			ArrayList<String> voiceResults = getIntent().getExtras().getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
			String spokenText = voiceResults.get(0);
	        
	        if(spokenText.equals("retry")) {
	        	displaySpeechRecognizer();
	        } else if (spokenText.equals("accept")) {
	        	Card card = new Card(this);
		        card.setText(register_player.get("first_name") + " " + register_player.get("last_name"));
		        card.setFootnote("Accepted");
	        } else {
	        	while (iterator_for_name < 2) {
	        		StringTokenizer player = new StringTokenizer(spokenText);
	    			String token = player.nextElement().toString();
	    			if (iterator_for_name == 0){
	    				register_player.put("first_name", token);  
	    			} else {
	    				register_player.put("last_name", token);
	    			}					
	    			iterator_for_name++;
	    		}
	        	Card card = new Card(this);
		        card.setText(register_player.get("first_name") + " " + register_player.get("last_name"));
		        card.setFootnote("Retry or accept");
		        displaySpeechRecognizer();
	        }				        
	    }
		
//		if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {			
//			
//			String picturePath = data.getStringExtra(CameraManager.EXTRA_PICTURE_FILE_PATH);
//		    Toast.makeText(getApplicationContext(), picturePath, Toast.LENGTH_SHORT).show();		    
//		    Card card = new Card(this);
//	        card.setText(picturePath); 
//	        card.setImageLayout(Card.ImageLayout.LEFT);
//	      	card.addImage(R.drawable.picturePath);
//	        setContentView(card.toView());	        
//	    }

	    super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	
}
