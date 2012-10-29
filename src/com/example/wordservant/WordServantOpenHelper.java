package com.example.wordservant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class WordServantOpenHelper extends SQLiteOpenHelper{
	
	private static final String SCRIPTURE_BANK_TABLE_NAME = "scripture_bank";
	
    private static final String SCRIPTURE_BANK_TABLE_CREATE =
            "CREATE TABLE " + SCRIPTURE_BANK_TABLE_NAME + " (" +
            " SCRIPTURE_ID INTEGER PRIMARY KEY, " +
            " SCRIPTURE_REFERENCE TEXT, " +
            " CATEGORY TEXT, " +
            " SCRIPTURE_TEXT TEXT, " +
            " REVIEW_DATE TEXT, " + 
            " REVIEW_SCHEDULE TEXT, "+
            " REVIEWED INTEGER);";
    
	public WordServantOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Creates the scripture bank table when the database is created.
		db.execSQL(SCRIPTURE_BANK_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Kills the table and existing data
        db.execSQL("DROP TABLE IF EXISTS "+ SCRIPTURE_BANK_TABLE_NAME);

        // Recreates the database with a new version
        onCreate(db);
	}

}
