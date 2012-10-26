package com.example.wordservant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class WordServantOpenHelper extends SQLiteOpenHelper{
	
	private static final int DATABASE_VERSION = 1;
    private static final String WORDSERVANT_TABLE_NAME = "scripture_bank";
	
    private static final String WORDSERVANT_TABLE_CREATE =
            "CREATE TABLE " + WORDSERVANT_TABLE_NAME + " (" +
            " _ID INTEGER PRIMARY KEY, " +
            " SCRIPTURE_REFERENCE STRING, " +
            " CATEGORY STRING, " +
            " SCRIPTURE TEXT);";

	public WordServantOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(WORDSERVANT_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
        // Logs that the database is being upgraded

        // Kills the table and existing data
        db.execSQL("DROP TABLE IF EXISTS "+ WORDSERVANT_TABLE_NAME);

        // Recreates the database with a new version
        onCreate(db);
	}

}
