package com.tumo.fungame.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;
import com.tumo.fungame.dao.PersonDao;
import com.tumo.fungame.service.LiveCardService;

public class SelectDbActivity extends Activity {

	public static final String KEY_NAME = "key_name";
	public static final String KEY_IMAGE = "key_image";

	private List<Card> mCards;
	private CardScrollView mCardScrollView;
	private SelectDbScrollAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		createCards();

		mCardScrollView = new CardScrollView(this);
		mAdapter = new SelectDbScrollAdapter();
		mCardScrollView.setAdapter(mAdapter);
		mCardScrollView.activate();

		mCardScrollView.setOnItemSelectedListener(itemSelectedListener);
		mCardScrollView.setOnItemClickListener(itemClickListener);
		setContentView(mCardScrollView);
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
			Card card = (Card) mAdapter.getItem(position);
			Intent intent = new Intent(SelectDbActivity.this,
					LiveCardService.class);
			Bundle bundle = new Bundle();
			bundle.putString(KEY_NAME, card.getText());
			bundle.putParcelable(KEY_IMAGE, card.getImage(0));
			intent.putExtras(bundle);
			startService(intent);
			finish();
		}
	};

	private void createCards() {
		List<String> dbs = PersonDao.getDbNames();
		dbs.add(0, PersonDao.DB_ALL);
		mCards = new ArrayList<Card>();

		for (String string : dbs) {
			Card card = new Card(this);
			card.setText(string);
			mCards.add(card);
		}
	}

	private class SelectDbScrollAdapter extends CardScrollAdapter {

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

	private class Player {
		private String name;
		private String country;
		private int imageId;

		public Player(String name, String country, int imageId) {
			super();
			this.name = name;
			this.country = country;
			this.imageId = imageId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public int getImageId() {
			return imageId;
		}

		public void setImageId(int imageId) {
			this.imageId = imageId;
		}

	}
}
