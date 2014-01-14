package com.tumo.fungame.service;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

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

	private static boolean isPersonGuessed = false;
	private static boolean isGameOver = false;
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
					person.beautify());
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

			if (isGameOver()) {
				String text = "";
				if (isGuessed()) {
					text = "You win :) \n";
				} else {
					text = "You lose :( \n";
				}

				remoteViews.setCharSequence(R.id.livecard_content, "setText",
						text + personToGuess.beautify());

				remoteViews.setImageViewUri(R.id.livecard_image,
						Uri.fromFile(new File(personToGuess.getPicture())));
			} else {
				remoteViews.setCharSequence(R.id.livecard_content, "setText",
						person.beautify());

				remoteViews.setImageViewResource(R.id.livecard_image,
						R.drawable.question_mark);
			}

			mLiveCard.setViews(remoteViews);
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
		if (persons.isEmpty()) {
			Toast.makeText(this, "This database is empty", Toast.LENGTH_SHORT)
					.show();
			stopSelf();
		} else {
			Random random = new Random();
			int randomPosition = random.nextInt(persons.size());
			personToGuess = persons.get(randomPosition);
			person = new Person(personToGuess.getDbName(),
					personToGuess.getId(), personToGuess.getName(),
					personToGuess.getSurname(), personToGuess.getGender(),
					personToGuess.getPicture(), personToGuess.getLocation());
			person.getNicks().add(personToGuess.getNicks().get(0));
			person.getNicks().add(personToGuess.getNicks().get(1));
			isPersonGuessed = false;
			isGameOver = false;
			publishCard(this);
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		if (mLiveCard != null && mLiveCard.isPublished()) {
			unpublishCard(this);
		}
		super.onDestroy();
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

	public static boolean isGuessed() {
		return isPersonGuessed;
	}

	public static void setGuessed(boolean t) {
		isPersonGuessed = t;
	}

	public static boolean isGameOver() {
		return isGameOver;
	}

	public static void setGameOver(boolean t) {
		isGameOver = t;
	}

	public Person getPersonToGuess() {
		return personToGuess;
	}
}
