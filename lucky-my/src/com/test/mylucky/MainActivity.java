package com.test.mylucky;

import java.util.ArrayList;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

public class MainActivity extends Activity {

	private GestureDetector mGestureDetector;

	// Gestures

	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		if (mGestureDetector != null) {
			return mGestureDetector.onMotionEvent(event);
		}
		return false;
	}

	private GestureDetector createGestureDetector(Context context) {
		GestureDetector gestureDetector = new GestureDetector(context);
		gestureDetector.setBaseListener(new GestureDetector.BaseListener() {

			@Override
			public boolean onGesture(Gesture gesture) {
				if (gesture == Gesture.TWO_LONG_PRESS) {
					openOptionsMenu();
					return true;
				}
				return false;
			}
		});
		return gestureDetector;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ArrayList<String> voiceResults = getIntent().getExtras()
		// .getStringArrayList(RecognizerIntent.EXTRA_RESULTS);

		String res = "begin"; // voiceResults.get(0);
		if (res.equals("begin") || res.equals("start") || res.equals("cat")) {
			startService(new Intent(this, LiveCardService.class));
		} else if (res.equals("stop")) {
			stopService(new Intent(this, LiveCardService.class));
		}

		setContentView(R.layout.main);

		TextView tv = (TextView) findViewById(R.id.livecard_content_main);
		tv.setText(res);

		mGestureDetector = createGestureDetector(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_card_stop:
			finish();
			return true;

		case R.id.menu_card_goto__main:
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
