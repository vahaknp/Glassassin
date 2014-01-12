package com.tumo.fungame.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;
import com.tumo.fungame.R;
import com.tumo.fungame.dao.PersonDao;
import com.tumo.fungame.model.Person;

public class ViewAllActivity extends Activity {

	private List<Person> mPersons;
	private List<Card> mCards;
	private CardScrollView mCardScrollView;
	private ViewAllScrollAdapter mAdapter;

	private int positionToRemove;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getCards();
		loadUI();
	}

	private void loadUI() {
		if (mPersons.isEmpty()) {
			Card card = new Card(this);
			card.setText("No person found");
			card.setFootnote("Go back to add");
			setContentView(card.toView());
		} else {
			mCardScrollView = new CardScrollView(this);
			mAdapter = new ViewAllScrollAdapter();
			mCardScrollView.setAdapter(mAdapter);
			mCardScrollView.activate();

			mCardScrollView.setOnItemSelectedListener(itemSelectedListener);
			mCardScrollView.setOnItemClickListener(itemClickListener);
			setContentView(mCardScrollView);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.activity_view_all, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_view_all_delete:
			PersonDao.remove(mPersons.get(positionToRemove).getId());
			getCards();
			loadUI();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		super.onOptionsMenuClosed(menu);
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
			positionToRemove = position;
			openOptionsMenu();
		}
	};

	private void getCards() {
		mPersons = PersonDao.getPersons(PersonDao.DB_MY);
		mCards = new ArrayList<Card>();

		for (Person person : mPersons) {
			Card card = new Card(this);
			card.setImageLayout(Card.ImageLayout.LEFT);
			card.addImage(Uri.fromFile(new File(person.getPicture())));
			card.setText(person.beautify());
			mCards.add(card);
		}
	}

	private class ViewAllScrollAdapter extends CardScrollAdapter {

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
