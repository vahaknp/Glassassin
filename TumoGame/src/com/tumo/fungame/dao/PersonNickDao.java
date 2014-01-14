package com.tumo.fungame.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.tumo.fungame.db.DBHelper;
import com.tumo.fungame.model.Nick;

public class PersonNickDao {

	public static final String TABLE_NAME = "PERSON_NICK";
	public static final String PERSON_ID = "person_id";
	public static final String NICK_ID = "nick_id";

	public static void insert(long personId, long nickId) throws SQLiteException {
		DBHelper dbHelper = new DBHelper();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try {
			db.beginTransaction();

			ContentValues cv = new ContentValues();
			cv.put(PERSON_ID, personId);
			cv.put(NICK_ID, nickId);
			db.insertOrThrow(TABLE_NAME, null, cv);

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
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

	public static void deleteByPerson(long personId) throws SQLiteException {
		DBHelper dbHelper = new DBHelper();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try {
			db.beginTransaction();
			db.delete(TABLE_NAME, PERSON_ID + "=?",
					new String[] { String.valueOf(personId) });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	public static void deleteByNick(long nickId) throws SQLiteException {
		DBHelper dbHelper = new DBHelper();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try {
			db.beginTransaction();
			db.delete(TABLE_NAME, NICK_ID + "=?",
					new String[] { String.valueOf(nickId) });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	public static List<Nick> getNicksByPerson(long personId)
			throws SQLiteException {
		List<Nick> nicks = new ArrayList<Nick>();
		DBHelper dbHelper = new DBHelper();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor curs = null;
		try {
			curs = db.rawQuery("select  " + NICK_ID + " from " + TABLE_NAME
					+ " where " + PERSON_ID + "=?",
					new String[] { String.valueOf(personId) });

			if (curs.moveToFirst()) {
				do {
					Nick nick = NickDao.get(curs.getInt(curs
							.getColumnIndex(NICK_ID)));
					nicks.add(nick);
				} while (curs.moveToNext());
			}
		} finally {
			curs.close();
			db.close();
		}
		return nicks;
	}

}
