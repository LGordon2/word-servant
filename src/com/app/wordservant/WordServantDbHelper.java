package com.app.wordservant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class WordServantDbHelper extends SQLiteOpenHelper{
	
	public static final int DATABASE_VERSION = 1;
    private static String SCRIPTURE_BANK_TABLE_CREATE = "CREATE TABLE "+ WordServantContract.ScriptureEntry.TABLE_NAME+ " ("+
    		WordServantContract.ScriptureEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
    		WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE+" TEXT NOT NULL, "+
    		WordServantContract.ScriptureEntry.COLUMN_NAME_TAG_ID+" INTEGER, "+
    		WordServantContract.ScriptureEntry.COLUMN_NAME_TEXT+" TEXT NOT NULL, "+
    		WordServantContract.ScriptureEntry.COLUMN_NAME_CREATED_DATE+" TEXT DEFAULT CURRENT_DATE, "+
    		WordServantContract.ScriptureEntry.COLUMN_NAME_SCHEDULE+" TEXT NOT NULL DEFAULT daily, "+
    		WordServantContract.ScriptureEntry.COLUMN_NAME_LAST_REVIEWED_DATE+" TEXT DEFAULT NULL, "+
    		WordServantContract.ScriptureEntry.COLUMN_NAME_TIMES_REVIEWED+" INTEGER NOT NULL DEFAULT 0, "+
    		WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE+" TEXT DEFAULT CURRENT_DATE);";
	private static String TAG_TABLE_CREATE = "CREATE TABLE "+WordServantContract.TagEntry.TABLE_NAME+" ("+
    		WordServantContract.TagEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT);";
	
	
	public WordServantDbHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Creates the scripture bank table when the database is created.
		//db.execSQL("PRAGMA foreign_keys=ON;");
		db.execSQL(SCRIPTURE_BANK_TABLE_CREATE);
		db.execSQL(TAG_TABLE_CREATE);
	}

	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Kills the table and existing data
        db.execSQL("DROP TABLE IF EXISTS "+ WordServantContract.ScriptureEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ WordServantContract.TagEntry.TABLE_NAME);

        // Recreates the database with a new version
        onCreate(db);
	}
}
