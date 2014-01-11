package com.tumo.fungame.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.tumo.fungame.db.DBHelper;
import com.tumo.fungame.model.Nick;

public class NickDao {

	public static final String TABLE_NAME = "NICK";
	public static final String DB_NAME = "db_name";
	public static final String ID = "_id";
	public static final String NAME = "name";

	public static long insert(Nick nick) throws SQLiteException {
		DBHelper dbHelper = new DBHelper();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long id = -1;
		try {
			db.beginTransaction();

			ContentValues cv = new ContentValues();
			cv.put(DB_NAME, nick.getDbName());
			cv.put(NAME, nick.getName());
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

	public static void delete(long id) throws SQLiteException {
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

	public static void update(Nick nick) throws SQLiteException {
		DBHelper dbHelper = new DBHelper();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try {
			db.beginTransaction();
			String where = ID + " =? ";
			ContentValues cv = new ContentValues();
			cv.put(DB_NAME, nick.getDbName());
			cv.put(NAME, nick.getName());
			db.update(TABLE_NAME, cv, where,
					new String[] { String.valueOf(nick.getId()) });

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	public static Nick get(long id) throws SQLiteException {
		Nick nick = null;
		DBHelper dbHelper = new DBHelper();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor curs = null;
		try {
			curs = db.rawQuery("select  " + DB_NAME + " , " + ID + " , " + NAME
					+ " from " + TABLE_NAME + " where " + ID + "=?",
					new String[] { String.valueOf(id) });

			if (curs.moveToFirst()) {
				nick = new Nick();
				nick.setDbName(curs.getString(curs.getColumnIndex(DB_NAME)));
				nick.setId(curs.getInt(curs.getColumnIndex(ID)));
				nick.setName(curs.getString(curs.getColumnIndex(NAME)));
			}
		} finally {
			curs.close();
			db.close();
		}
		return nick;
	}

	public static List<Nick> getNicks(String dbName) throws SQLiteException {
		List<Nick> nicks = new ArrayList<Nick>();

		DBHelper dbHelper = new DBHelper();
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		Cursor cursor = null;

		String sql = "select  " + DB_NAME + " , " + ID + " , " + NAME
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
					Nick nick = new Nick();
					nick.setDbName(cursor.getString(cursor
							.getColumnIndex(DB_NAME)));
					nick.setId(cursor.getInt(cursor.getColumnIndex(ID)));
					nick.setName(cursor.getString(cursor.getColumnIndex(NAME)));
					nicks.add(nick);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
			db.close();
		}

		return nicks;
	}
}
