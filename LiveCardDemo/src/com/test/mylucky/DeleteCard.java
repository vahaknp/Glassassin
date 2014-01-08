package com.test.mylucky;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class DeleteCard extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		// Delete LiveCard service
		stopService(new Intent(this, LiveCardService.class));
		
		// End this activity
		finish();
	}

}
