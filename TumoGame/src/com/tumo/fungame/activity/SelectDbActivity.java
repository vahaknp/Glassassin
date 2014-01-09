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
import com.tumo.fungame.R;
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
			Intent intent = new Intent(SelectDbActivity.this, LiveCardService.class);
			Bundle bundle = new Bundle();
			bundle.putString(KEY_NAME, card.getText());
			bundle.putParcelable(KEY_IMAGE, card.getImage(0));
			intent.putExtras(bundle);
			startService(intent);
			finish();
		}
	};

	private void createCards() {
		mCards = new ArrayList<Card>();

		Card card;

		card = new Card(this);
		card.setText("Messi");
		card.setFootnote("Argentina");
		card.setImageLayout(Card.ImageLayout.LEFT);
		card.addImage(R.drawable.messi_1);
		card.addImage(R.drawable.messi_2);
		card.addImage(R.drawable.messi_3);
		mCards.add(card);

		card = new Card(this);
		card.setText("Neymar");
		card.setFootnote("Brazil");
		card.setImageLayout(Card.ImageLayout.LEFT);
		card.addImage(R.drawable.neymar);
		mCards.add(card);

		card = new Card(this);
		card.setText("Lavezzi");
		card.setFootnote("Argentina");
		card.setImageLayout(Card.ImageLayout.LEFT);
		card.addImage(R.drawable.lavezzi);
		mCards.add(card);
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
}
