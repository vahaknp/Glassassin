package com.tumo.fungame.db;

import com.tumo.fungame.TumoGameApp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "Tumo_Game";
	private static final int VERSION = 1;

	public DBHelper(Context context, CursorFactory factory, int version) {
		super(context, DBHelper.DATABASE_NAME, factory, version);
	}

	public DBHelper() {
		super(TumoGameApp.getContext(), DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		DbCreator.createTablePerson(db);
		DbCreator.createTableNick(db);
		DbCreator.createTablePersonNick(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// for next version
	}
}