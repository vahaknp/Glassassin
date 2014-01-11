package com.test.mylucky;

import java.io.File;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Toast;
import com.google.android.glass.app.Card;

public class CardActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		String picturePath = extras.getString("picturePath");
		
		Card card = new Card(this);		    
        card.setImageLayout(Card.ImageLayout.LEFT);
        card.addImage(Uri.fromFile(new File(picturePath)));
        setContentView(card.toView());
        Toast.makeText(getApplicationContext(), "Did you guess a person?", Toast.LENGTH_SHORT).show();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	    super.onActivityResult(requestCode, resultCode, data);
	}

}
