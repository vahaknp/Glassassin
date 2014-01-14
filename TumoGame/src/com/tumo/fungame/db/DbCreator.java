package com.tumo.fungame.db;

import android.database.sqlite.SQLiteDatabase;

import com.tumo.fungame.dao.NickDao;
import com.tumo.fungame.dao.PersonDao;
import com.tumo.fungame.dao.PersonNickDao;

public class DbCreator {
    
    public static void createTablePerson(SQLiteDatabase db){
	db.execSQL("create table " +
        	         PersonDao.TABLE_NAME + "("    
        	+ PersonDao.ID             + " integer primary key autoincrement,"
        	+ PersonDao.DB_NAME		   + " text not null," 
        	+ PersonDao.NAME		   + " text," 
        	+ PersonDao.SURNAME		   + " text," 
        	+ PersonDao.GENDER		   + " integer not null," 
        	+ PersonDao.PICTURE		   + " text not null," 
        	+ PersonDao.LOCATION	   + " text not null)"); 
    }
    
    public static void createTableNick(SQLiteDatabase db){
	db.execSQL("create table " +
        	         NickDao.TABLE_NAME + "("    
        	+ NickDao.ID             	+ " integer primary key autoincrement,"
        	+ NickDao.DB_NAME		 	+ " text not null," 
        	+ NickDao.NAME		   		+ " text not null)"); 
    }
    
    public static void createTablePersonNick(SQLiteDatabase db){
	db.execSQL("create table " +
        	         PersonNickDao.TABLE_NAME + "("    
        	+ PersonNickDao.PERSON_ID       + " integer not null,"
        	+ PersonNickDao.NICK_ID		 	+ " integer not null)"); 
    }
}
