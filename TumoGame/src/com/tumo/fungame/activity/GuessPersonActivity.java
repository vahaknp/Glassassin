package com.tumo.fungame.activity;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.glass.media.CameraManager;
import com.tumo.fungame.R;

public class GuessPersonActivity extends Activity {

	private String picPathReal;
	private String picPathGuessed;
	private boolean shouldMenuClose;
	private boolean isMenuOpened = false;

	private static final int TAKE_PICTURE_REQUEST = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		picPathReal = getIntent().getStringExtra(
				LiveCardMenuActivity.KEY_PIC_PATH);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (TextUtils.isEmpty(picPathGuessed)) {
			openOptionsMenu();
		}
		shouldMenuClose = true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			openOptionsMenu();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!isMenuOpened) {
				finish();
			} else {
				isMenuOpened = false;
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.activity_guess_person, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		isMenuOpened = true;
		MenuItem itemPicture = menu.findItem(R.id.menu_guess_person_picture);
		MenuItem itemAccept = menu.findItem(R.id.menu_guess_person_accept);
		MenuItem itemDecline = menu.findItem(R.id.menu_guess_person_decline);
		itemPicture.setVisible(TextUtils.isEmpty(picPathGuessed));
		itemAccept.setVisible(!TextUtils.isEmpty(picPathGuessed));
		itemDecline.setVisible(!TextUtils.isEmpty(picPathGuessed));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_guess_person_picture:
			shouldMenuClose = false;
			takePicture();
			return true;

		case R.id.menu_guess_person_accept:
			shouldMenuClose = false;
			Intent returnIntentA = new Intent();
			returnIntentA.putExtra("result", 1);
			setResult(RESULT_OK, returnIntentA);
			finish();
			return true;

		case R.id.menu_guess_person_decline:
			shouldMenuClose = false;
			Intent returnIntentD = new Intent();
			returnIntentD.putExtra("result", 0);
			setResult(RESULT_OK, returnIntentD);
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		super.onOptionsMenuClosed(menu);
		if (TextUtils.isEmpty(picPathGuessed) && shouldMenuClose) {
			finish();
		}
		isMenuOpened = false;
	}

	private void takePicture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, TAKE_PICTURE_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {
			String picturePath = data
					.getStringExtra(CameraManager.EXTRA_THUMBNAIL_FILE_PATH);
			picPathGuessed = picturePath;
			setContentView(R.layout.compare_person);
			ImageView imageView1 = (ImageView) findViewById(R.id.compare_img_1);
			ImageView imageView2 = (ImageView) findViewById(R.id.compare_img_2);
			imageView1.setImageURI(Uri.fromFile(new File(picPathGuessed)));
			imageView2.setImageURI(Uri.fromFile(new File(picPathReal)));
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
}
