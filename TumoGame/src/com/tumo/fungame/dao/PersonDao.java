package com.tumo.fungame.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.tumo.fungame.db.DBHelper;
import com.tumo.fungame.model.Nick;
import com.tumo.fungame.model.Person;

public class PersonDao {

	public static final String TABLE_NAME = "PERSON";
	public static final String DB_NAME = "db_name";
	public static final String ID = "_id";
	public static final String NAME = "name";
	public static final String SURNAME = "surname";
	public static final String GENDER = "gender";
	public static final String PICTURE = "picture";
	public static final String LOCATION = "location";

	// Db names
	public static final String DB_ALL = "All";
	public static final String DB_MY = "Local";

	public static void add(Person person) {
		long personId = insert(person);
		if (personId != -1) {
			for (Nick nick : person.getNicks()) {
				long nickId = NickDao.insert(nick);
				if (nickId != -1) {
					PersonNickDao.insert(personId, nickId);
				}
			}
		}
	}

	public static void remove(long personId) {
		List<Nick> nicks = PersonNickDao.getNicksByPerson(personId);
		delete(personId);
		PersonNickDao.deleteByPerson(personId);
		for (Nick nick : nicks) {
			NickDao.delete(nick.getId());
		}
	}

	public static void edit(Person person) {
		update(person);

		List<Nick> nicks = PersonNickDao.getNicksByPerson(person.getId());
		PersonNickDao.deleteByPerson(person.getId());
		for (Nick nick : nicks) {
			NickDao.delete(nick.getId());
		}

		for (Nick nick : person.getNicks()) {
			long nickId = NickDao.insert(nick);
			if (nickId != -1) {
				PersonNickDao.insert(person.getId(), nickId);
			}
		}
	}

	private static long insert(Person person) throws SQLiteException {
		DBHelper dbHelper = new DBHelper();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long id = -1;
		try {
			db.beginTransaction();

			ContentValues cv = new ContentValues();
			cv.put(DB_NAME, person.getDbName());
			cv.put(NAME, person.getName());
			cv.put(SURNAME, person.getSurname());
			cv.put(GENDER, person.getGender());
			cv.put(PICTURE, person.getPicture());
			cv.put(LOCATION, person.getLocation());
			id = db.insertOrThrow(TABLE_NAME, null, cv);

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
		return id;
	}

	public static void deleteTable() throws SQLiteException {
		DBHelper dbHelper = new DBHelper();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try {
			db.delete(TABLE_NAME, null, null);
		} finally {
			db.close();
		}
	}

	private static void delete(long id) throws SQLiteException {
		DBHelper dbHelper = new DBHelper();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try {
			db.beginTransaction();
			db.delete(TABLE_NAME, ID + "=?",
					new String[] { String.valueOf(id) });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	private static void update(Person person) throws SQLiteException {
		DBHelper dbHelper = new DBHelper();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try {
			db.beginTransaction();
			String where = ID + " =? ";
			ContentValues cv = new ContentValues();
			cv.put(DB_NAME, person.getDbName());
			cv.put(NAME, person.getName());
			cv.put(SURNAME, person.getSurname());
			cv.put(GENDER, person.getGender());
			cv.put(PICTURE, person.getPicture());
			cv.put(LOCATION, person.getLocation());
			db.update(TABLE_NAME, cv, where,
					new String[] { String.valueOf(person.getId()) });

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	public static Person get(long id) throws SQLiteException {
		Person person = null;
		DBHelper dbHelper = new DBHelper();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor curs = null;
		try {
			curs = db.rawQuery("select  " + DB_NAME + " , " + ID + " , " + NAME
					+ " , " + SURNAME + " , " + GENDER + " , " + PICTURE
					+ " , " + LOCATION + " from " + TABLE_NAME + " where " + ID
					+ "=?", new String[] { String.valueOf(id) });

			if (curs.moveToFirst()) {
				person = new Person();
				person.setDbName(curs.getString(curs.getColumnIndex(DB_NAME)));
				person.setId(curs.getInt(curs.getColumnIndex(ID)));
				person.setName(curs.getString(curs.getColumnIndex(NAME)));
				person.setSurname(curs.getString(curs.getColumnIndex(SURNAME)));
				person.setGender(curs.getInt(curs.getColumnIndex(GENDER)));
				person.setPicture(curs.getString(curs.getColumnIndex(PICTURE)));
				person.setLocation(curs.getString(curs.getColumnIndex(LOCATION)));

				List<Nick> nicks = PersonNickDao.getNicksByPerson(person
						.getId());
				person.setNicks(nicks);
			}
		} finally {
			curs.close();
			db.close();
		}
		return person;
	}

	public static List<Person> getPersons(String dbName) throws SQLiteException {
		if (dbName.equals(DB_ALL)) {
			dbName = "";
		}
		List<Person> persons = new ArrayList<Person>();

		DBHelper dbHelper = new DBHelper();
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		Cursor cursor = null;

		String sql = "select  " + DB_NAME + " , " + ID + " , " + NAME + " , "
				+ SURNAME + " , " + GENDER + " , " + PICTURE + " , " + LOCATION
				+ " from " + TABLE_NAME;

		ArrayList<String> args = new ArrayList<String>();
		String whereAnd = " where ";
		if (dbName != null && !dbName.isEmpty()) {
			sql = sql + whereAnd + DB_NAME + " = ?";
			args.add(dbName);
			whereAnd = " and ";
		}

		try {
			cursor = db.rawQuery(sql, args.toArray(new String[] {}));

			if (cursor.moveToFirst()) {
				do {
					Person person = new Person();
					person.setDbName(cursor.getString(cursor
							.getColumnIndex(DB_NAME)));
					person.setId(cursor.getInt(cursor.getColumnIndex(ID)));
					person.setName(cursor.getString(cursor.getColumnIndex(NAME)));
					person.setSurname(cursor.getString(cursor
							.getColumnIndex(SURNAME)));
					person.setGender(cursor.getInt(cursor
							.getColumnIndex(GENDER)));
					person.setPicture(cursor.getString(cursor
							.getColumnIndex(PICTURE)));
					person.setLocation(cursor.getString(cursor
							.getColumnIndex(LOCATION)));

					List<Nick> nicks = PersonNickDao.getNicksByPerson(person
							.getId());
					person.setNicks(nicks);
					persons.add(person);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
			db.close();
		}

		return persons;
	}

	public static List<String> getDbNames() throws SQLiteException {
		List<String> dbs = new ArrayList<String>();

		DBHelper dbHelper = new DBHelper();
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select distinct " + DB_NAME + " from "
					+ TABLE_NAME, null);

			if (cursor.moveToFirst()) {
				do {
					dbs.add(cursor.getString(cursor.getColumnIndex(DB_NAME)));
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
			db.close();
		}

		return dbs;
	}
}
