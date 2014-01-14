package com.tumo.fungame.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.google.android.glass.app.Card;
import com.google.android.glass.media.CameraManager;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;
import com.tumo.fungame.R;
import com.tumo.fungame.dao.PersonDao;
import com.tumo.fungame.model.Nick;
import com.tumo.fungame.model.Person;

public class AddPersonActivity extends Activity {

	private List<Card> mCards;
	private CardScrollView mCardScrollView;
	private GenderScrollAdapter mAdapter;

	private Person person;
	private boolean shouldMenuClose;
	private boolean isMenuOpened = false;

	private static final int SPEECH_REQUEST = 0;
	private static final int TAKE_PICTURE_REQUEST = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		person = new Person();
		person.setDbName(PersonDao.DB_MY);
		person.setLocation(getString(R.string.game_location));
		getCards();

		mCardScrollView = new CardScrollView(this);
		mAdapter = new GenderScrollAdapter();
		mCardScrollView.setAdapter(mAdapter);
		mCardScrollView.activate();

		mCardScrollView.setOnItemSelectedListener(itemSelectedListener);
		mCardScrollView.setOnItemClickListener(itemClickListener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (TextUtils.isEmpty(person.getPicture())) {
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
		menuInflater.inflate(R.menu.activity_add_person, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		isMenuOpened = true;
		MenuItem itemPicture = menu.findItem(R.id.menu_add_person_picture);
		MenuItem itemAdj = menu.findItem(R.id.menu_add_person_adj);
		MenuItem itemDone = menu.findItem(R.id.menu_add_person_done);
		itemPicture.setVisible(TextUtils.isEmpty(person.getPicture()));
		itemAdj.setVisible(!TextUtils.isEmpty(person.getPicture()));
		itemDone.setVisible(!TextUtils.isEmpty(person.getPicture())
				&& person.getNicks().size() >= 4);

		// limit for adjectives count
		itemAdj.setEnabled(person.getNicks().size() <= 12);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add_person_picture:
			shouldMenuClose = false;
			takePicture();
			return true;

		case R.id.menu_add_person_adj:
			shouldMenuClose = false;
			displaySpeechRecognizer();
			return true;

		case R.id.menu_add_person_done:
			shouldMenuClose = false;
			PersonDao.add(person);
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		super.onOptionsMenuClosed(menu);
		if (TextUtils.isEmpty(person.getPicture()) && shouldMenuClose) {
			finish();
		}
		isMenuOpened = false;
	}

	private void takePicture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, TAKE_PICTURE_REQUEST);
	}

	private void displaySpeechRecognizer() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		startActivityForResult(intent, SPEECH_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {
			String picturePath = data
					.getStringExtra(CameraManager.EXTRA_THUMBNAIL_FILE_PATH);
			person.setPicture(picturePath);
			setContentView(mCardScrollView);
		}

		if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) {
			List<String> results = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			String spokenText = results.get(0);
			person.getNicks().add(new Nick(PersonDao.DB_MY, 0, spokenText));
			reloadCard();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void processPictureWhenReady(final String picturePath) {
		final File pictureFile = new File(picturePath);

		if (pictureFile.exists()) {
			reloadCard();
		} else {
			// The file does not exist yet. Before starting the file observer,
			// you
			// can update your UI to let the user know that the application is
			// waiting for the picture (for example, by displaying the thumbnail
			// image and a progress indicator).

			final File parentDirectory = pictureFile.getParentFile();
			FileObserver observer = new FileObserver(parentDirectory.getPath()) {
				// Protect against additional pending events after CLOSE_WRITE
				// is
				// handled.
				private boolean isFileWritten;

				@Override
				public void onEvent(int event, String path) {
					if (!isFileWritten) {
						// For safety, make sure that the file that was created
						// in
						// the directory is actually the one that we're
						// expecting.
						File affectedFile = new File(parentDirectory, path);
						isFileWritten = (event == FileObserver.CLOSE_WRITE && affectedFile
								.equals(pictureFile));

						if (isFileWritten) {
							stopWatching();

							// Now that the file is ready, recursively call
							// processPictureWhenReady again (on the UI thread).
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									processPictureWhenReady(picturePath);
								}
							});
						}
					}
				}
			};
			observer.startWatching();
		}
	}

	private void reloadCard() {
		Card card = new Card(AddPersonActivity.this);
		card.setImageLayout(Card.ImageLayout.LEFT);
		card.addImage(Uri.fromFile(new File(person.getPicture())));
		card.setText(person.beautifyCard());
		setContentView(card.toView());
	}

	OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};

	OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			person.setGender(position + 1);
			reloadCard();
			processPictureWhenReady(person.getPicture());
		}
	};

	private void getCards() {
		mCards = new ArrayList<Card>();

		List<Integer> boys = new ArrayList<Integer>();
		boys.add(R.drawable.b1);
		boys.add(R.drawable.b2);
		boys.add(R.drawable.b3);

		List<Integer> girls = new ArrayList<Integer>();
		girls.add(R.drawable.g1);
		girls.add(R.drawable.g2);
		girls.add(R.drawable.g3);

		Random random = new Random();

		Card cardBoy = new Card(this);
		cardBoy.setImageLayout(Card.ImageLayout.FULL);
		cardBoy.addImage(boys.get(random.nextInt(3)));
		cardBoy.setText("Male");
		mCards.add(cardBoy);

		Card cardGirl = new Card(this);
		cardGirl.setImageLayout(Card.ImageLayout.FULL);
		cardGirl.addImage(girls.get(random.nextInt(3)));
		cardGirl.setText("Female");
		mCards.add(cardGirl);
	}

	private class GenderScrollAdapter extends CardScrollAdapter {

		@Override
		public int findIdPosition(Object id) {
			return -1;
		}

		@Override
		public int findItemPosition(Object item) {
			return mCards.indexOf(item);
		}

		@Override
		public int getCount() {
			return mCards.size();
		}

		@Override
		public Object getItem(int position) {
			return mCards.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return mCards.get(position).toView();
		}
	}
}
