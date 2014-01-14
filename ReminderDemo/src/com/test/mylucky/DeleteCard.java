package com.test.mylucky;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class DeleteCard extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		// Stop sound
		ReminderService.stopSound();
		
		// Delete LiveCard service
		stopService(new Intent(this, ReminderService.class));
		
		// End this activity
		finish();
	}

}
