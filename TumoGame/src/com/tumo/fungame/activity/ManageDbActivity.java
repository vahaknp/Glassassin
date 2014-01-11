package com.tumo.fungame.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tumo.fungame.R;
import com.tumo.fungame.dao.PersonDao;
import com.tumo.fungame.model.Nick;
import com.tumo.fungame.model.Person;

public class ManageDbActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Person person = new Person();
		person.setDbName("my");
		person.setGender(Person.Gender.MAN);
		person.setPicture("path/external/pic.jpg");
		person.setLocation("Tumo centre");
		
		List<Nick> nicks = new ArrayList<Nick>();
		Nick nick1 = new Nick();
		nick1.setDbName("my");
		nick1.setName("long hair");
		nicks.add(nick1);
		
		Nick nick2 = new Nick();
		nick2.setDbName("my");
		nick2.setName("fast");
		nicks.add(nick2);
		
		Nick nick3 = new Nick();
		nick3.setDbName("my");
		nick3.setName("barcelona");
		nicks.add(nick3);
		
		Nick nick4 = new Nick();
		nick4.setDbName("my");
		nick4.setName("small");
		nicks.add(nick4);
		
		Nick nick5 = new Nick();
		nick5.setDbName("my");
		nick5.setName("best player");
		nicks.add(nick5);
		
		person.setNicks(nicks);

		PersonDao.add(person);
	}

	@Override
	protected void onResume() {
		super.onResume();
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
			// finish();//TODO
			return true;

		case R.id.menu_manage_edit:
			// TODO
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

}
