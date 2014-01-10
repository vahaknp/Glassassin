package com.tumo.fungame.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tumo.fungame.R;
import com.tumo.fungame.model.Nick;
import com.tumo.fungame.service.LiveCardService;

public class LiveCardMenuActivity extends Activity {

	private List<Nick> hardcodedNicks = new ArrayList<Nick>(Arrays.asList(
			new Nick(1, "red"), new Nick(2, "green"), new Nick(3, "red hat"),
			new Nick(4, "don't care")));

	private static final int SPEECH_REQUEST = 0;

	private boolean shouldMenuClose = true;

	// binding to Service

	private LiveCardService mService;
	private boolean mIsBound = false;

	private ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
			mIsBound = false;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = ((LiveCardService.LocalBinder) service).getService();
			mIsBound = true;
		}
	};

	/*
	 * Bind to service
	 */
	private void doBind() {
		Intent intent = new Intent(this, LiveCardService.class);
		bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
		mIsBound = true;
	}

	/*
	 * Unbind from service
	 */
	private void doUnbind() {
		if (mIsBound) {
			unbindService(mServiceConnection);
			mIsBound = false;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
		doBind();
	}

	@Override
	protected void onStop() {
		super.onStop();
		doUnbind();
	}

	@Override
	protected void onResume() {
		super.onResume();
		openOptionsMenu();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_live_card, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_card_guess_adj:
			shouldMenuClose = false;
			displaySpeechRecognizer();
			return true;

		case R.id.menu_card_guess_person:
			// TODO
			return true;

		case R.id.menu_card_settings:
			goToMain();
			return true;
		case R.id.menu_card_exit:
			stopLiveCard();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void displaySpeechRecognizer() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		startActivityForResult(intent, SPEECH_REQUEST);
	}

	private boolean isInDB(Nick adj) {
		// TODO
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) {
			List<String> results = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			String spokenText = results.get(0);

			if (isInDB(new Nick(5, spokenText))) {
				mService.updateCard(this, new Nick(5, spokenText));
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
		finish();
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		super.onOptionsMenuClosed(menu);
		if (shouldMenuClose) {
			finish();
		}
	}

	private void stopLiveCard() {
		stopService(new Intent(this, LiveCardService.class));
	}

	private void goToMain() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
}
