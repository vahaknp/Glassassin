package com.test.mylucky;

import java.util.ArrayList;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			
		super.onCreate(savedInstanceState);		
				
		ArrayList<String> voiceResults = getIntent().getExtras()
				.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);

		String res = voiceResults.get(0);
		
		if(res.equals("compare")) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);		    
		    startActivityForResult(intent, TAKE_PICTURE_REQUEST);		    
		}  
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {			
			String picturePath = data.getStringExtra(CameraManager.EXTRA_PICTURE_FILE_PATH);
//		    Toast.makeText(getApplicationContext(), picturePath, Toast.LENGTH_SHORT).show();		    
		    Card card = new Card(this);
	        card.setText(picturePath); 
	        card.setImageLayout(Card.ImageLayout.LEFT);
//	      	card.addImage(R.drawable.picturePath);
	        setContentView(card.toView());	        
	    }		

	    super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
}
