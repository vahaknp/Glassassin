package com.test.mylucky;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class CardActivity extends Activity {

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

		// Card card = new Card(this);
		// card.setText("Hi Tumo");
		// card.setFootnote("glass workshop");
		//
		// setContentView(card.toView());
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
		inflater.inflate(R.menu.activity_card, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_card_stop:
			stopLiveCard();
			finish();
			return true;

		case R.id.menu_card_goto__main:
			goToMain();
			return true;

		case R.id.menu_card_service:
			testService();
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		super.onOptionsMenuClosed(menu);
		finish();
	}

	private void stopLiveCard() {
		stopService(new Intent(this, LiveCardService.class));
	}

	private void goToMain() {
		finish();
	}

	private void testService() {
		if (mIsBound) {
			mService.updateCard(this);
		}
	}
}
