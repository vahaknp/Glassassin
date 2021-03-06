package com.tumo.fungame.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tumo.fungame.R;
import com.tumo.fungame.service.LiveCardService;

public class MainActivity extends Activity {

	private boolean shouldMenuClose;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		shouldMenuClose = true;
		openOptionsMenu();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.activity_main, menu);
		MenuItem itemPlay = menu.findItem(R.id.menu_main_play);
		MenuItem itemRestart = menu.findItem(R.id.menu_main_restart);
		itemPlay.setVisible(!isLiveCardServiceRunning());
		itemRestart.setVisible(isLiveCardServiceRunning());
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_main_play:
			Intent intentPlay = new Intent(this, SelectDbActivity.class);
			startActivity(intentPlay);
			return true;

		case R.id.menu_main_restart:
			stopService(new Intent(this, LiveCardService.class));
			Intent intentRestart = new Intent(this, SelectDbActivity.class);
			startActivity(intentRestart);
			return true;

		case R.id.menu_main_manage_db:
			Intent intentManage = new Intent(this, ManageDbActivity.class);
			startActivity(intentManage);
			shouldMenuClose = false;
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		super.onOptionsMenuClosed(menu);
		if (shouldMenuClose) {
			finish();
		}
	}

	private boolean isLiveCardServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (LiveCardService.class.getName().equals(
					service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
	// openOptionsMenu();
	// return true;
	// }
	// return false;
	// }

}
