package com.tumo.fungame.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tumo.fungame.R;

public class ManageDbActivity extends Activity {

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
		menuInflater.inflate(R.menu.activity_manage_db, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_manage_send:
			shouldMenuClose = false;
			// finish();//TODO
			return true;

		case R.id.menu_manage_edit:
			shouldMenuClose = false;
			Intent intentEdit = new Intent(this, EditDbActivity.class);
			startActivity(intentEdit);
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
}
