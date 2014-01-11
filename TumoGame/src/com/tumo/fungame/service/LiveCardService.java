package com.tumo.fungame.service;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;
import com.tumo.fungame.R;
import com.tumo.fungame.activity.LiveCardMenuActivity;
import com.tumo.fungame.activity.SelectDbActivity;
import com.tumo.fungame.dao.PersonDao;
import com.tumo.fungame.model.Nick;
import com.tumo.fungame.model.Person;

public class LiveCardService extends Service {

	public class LocalBinder extends Binder {

		// Return this instance of LiveCardService
		public LiveCardService getService() {
			return LiveCardService.this;
		}
	}

	public static boolean isPersonGuessed = false;
	private final IBinder mBinder = new LocalBinder();

	// Tag used to identify the LiveCard in debugging logs.
	private static final String LIVE_CARD_TAG = "game_card";

	// Cached instance of the LiveCard created by the publishCard() method.
	private LiveCard mLiveCard;

	private String dbName;

	private Person personToGuess;
	private Person person;

	private void publishCard(Context context) {
		if (mLiveCard == null) {
			TimelineManager tm = TimelineManager.from(context);
			mLiveCard = tm.createLiveCard(LIVE_CARD_TAG);

			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.game_card);
			remoteViews.setCharSequence(R.id.livecard_content, "setText",
					beautifyPerson(person));
			remoteViews.setImageViewResource(R.id.livecard_image,
					R.drawable.question_mark);
			mLiveCard.setViews(remoteViews);
			Intent intent = new Intent(context, LiveCardMenuActivity.class);
			mLiveCard.setAction(PendingIntent
					.getActivity(context, 0, intent, 0));
			mLiveCard.publish(LiveCard.PublishMode.REVEAL);
		} else {
			// Card already published
		}
	}

	private void unpublishCard(Context context) {
		if (mLiveCard != null) {
			mLiveCard.unpublish();
			mLiveCard = null;
		}
	}

	public void updateCard(Context context, String spokenNick) {
		if (mLiveCard == null) {
			publishCard(this);
		} else {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.game_card);

			spokenNick = spokenNick.toLowerCase(Locale.ENGLISH).trim();

			if (!hasAlready(spokenNick) && hasNick(spokenNick)) {
				person.getNicks().add(findNick(spokenNick));
			}

			if (isGuessed()) {
				remoteViews.setCharSequence(R.id.livecard_content, "setText",
						beautifyPerson(personToGuess));

				remoteViews.setImageViewResource(R.id.livecard_image,
						R.drawable.messi_3);
			} else {
				remoteViews.setCharSequence(R.id.livecard_content, "setText",
						beautifyPerson(person));

				remoteViews.setImageViewResource(R.id.livecard_image,
						R.drawable.question_mark);
			}

			mLiveCard.setViews(remoteViews);
			
			isPersonGuessed = isGuessed();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Bundle bundle = intent.getExtras();
		dbName = bundle.getString(SelectDbActivity.KEY_DB_NAME);

		List<Person> persons = PersonDao.getPersons(dbName);
		Random random = new Random();
		int randomPosition = random.nextInt(persons.size());
		personToGuess = persons.get(randomPosition);
		person = new Person(personToGuess.getDbName(), personToGuess.getId(),
				personToGuess.getName(), personToGuess.getSurname(),
				personToGuess.getGender(), personToGuess.getPicture(),
				personToGuess.getLocation());
		isPersonGuessed = false;
		publishCard(this);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		if (mLiveCard != null && mLiveCard.isPublished()) {
			unpublishCard(this);
		}
		super.onDestroy();
	}

	private String beautifyPerson(Person per) {
		String res = "ID:		  " + per.getId() + "\n" + "Gender:	  "
				+ (per.getGender() == 1 ? "man" : "woman") + "\n"
				+ "Location: " + per.getLocation() + "\n" + "\n";

		res += per.nicksToString();

		return res;
	}

	private boolean hasAlready(String string) {
		for (Nick nick : person.getNicks()) {
			if (nick.getName().equals(string)) {
				return true;
			}
		}
		return false;
	}

	private boolean hasNick(String string) {
		for (Nick nick : personToGuess.getNicks()) {
			if (nick.getName().equals(string)) {
				return true;
			}
		}
		return false;
	}

	private Nick findNick(String string) {
		for (Nick nick : personToGuess.getNicks()) {
			if (nick.getName().equals(string)) {
				return nick;
			}
		}

		return null;
	}

	public boolean isGuessed() {
		return person.getNicks().size() >= personToGuess.getNicks().size() / 2 + 1;
	}
}
