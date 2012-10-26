package com.example.wordservant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class WordServantOpenHelper extends SQLiteOpenHelper{
	
	private static final int DATABASE_VERSION = 1;
    private static final String SCRIPTURE_BANK_TABLE_NAME = "scripture_bank";
	
    private static final String SCRIPTURE_BANK_TABLE_CREATE =
            "CREATE TABLE " + SCRIPTURE_BANK_TABLE_NAME + " (" +
            " SCRIPTURE_ID INTEGER PRIMARY KEY, " +
            " SCRIPTURE_REFERENCE STRING, " +
            " CATEGORY STRING, " +
            " SCRIPTURE TEXT);";
	private static final String DUE_TODAY_TABLE_NAME = "due_today";
    
    private static final String DUE_TODAY_TABLE_CREATE = 
    		"CREATE TABLE " + DUE_TODAY_TABLE_NAME + " (" +
    		" DUE_TODAY_ID INTEGER PRIMARY KEY, " +
    		" REVIEW_DATE TEXT, " +
    		" SCHEDULE TEXT, " +
    		" DATE_ADDED TEXT, " +
    		" SCRIPTURE_ID REFERENCES SCRIPTURE_BANK (SCRIPTURE_ID));";

	public WordServantOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(SCRIPTURE_BANK_TABLE_CREATE);
		db.execSQL(DUE_TODAY_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
        // Logs that the database is being upgraded

        // Kills the table and existing data
        db.execSQL("DROP TABLE IF EXISTS "+ SCRIPTURE_BANK_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ DUE_TODAY_TABLE_NAME);

        // Recreates the database with a new version
        onCreate(db);
	}

}
