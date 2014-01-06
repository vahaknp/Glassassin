package com.test.mylucky;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.TextView;

public class MyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ArrayList<String> voiceResults = getIntent().getExtras()
				.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);

		String res = voiceResults.get(0);
		if (res.equals("begin") || res.equals("start") || res.equals("cat")) {
			startService(new Intent(this, LiveCardService.class));
		} else if (res.equals("stop")) {
			stopService(new Intent(this, LiveCardService.class));
		}

		setContentView(R.layout.main);

		TextView tv = (TextView) findViewById(R.id.livecard_content_main);
		tv.setText(res);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
