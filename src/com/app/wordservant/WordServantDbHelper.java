package com.app.wordservant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class WordServantDbHelper extends SQLiteOpenHelper{
	
	public static final int DATABASE_VERSION = 3;
    private static String SCRIPTURE_BANK_TABLE_CREATE = "CREATE TABLE "+ WordServantContract.ScriptureEntry.TABLE_NAME+ " ("+
    		WordServantContract.ScriptureEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
    		WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE+" TEXT NOT NULL, "+
    		WordServantContract.ScriptureEntry.COLUMN_NAME_TEXT+" TEXT NOT NULL, "+
    		WordServantContract.ScriptureEntry.COLUMN_NAME_CREATED_DATE+" TEXT DEFAULT CURRENT_DATE, "+
    		WordServantContract.ScriptureEntry.COLUMN_NAME_SCHEDULE+" TEXT NOT NULL DEFAULT daily, "+
    		WordServantContract.ScriptureEntry.COLUMN_NAME_LAST_REVIEWED_DATE+" TEXT DEFAULT NULL, "+
    		WordServantContract.ScriptureEntry.COLUMN_NAME_TIMES_REVIEWED+" INTEGER NOT NULL DEFAULT 0, "+
    		WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE+" TEXT DEFAULT CURRENT_DATE, "+
			WordServantContract.ScriptureEntry.COLUMN_NAME_CORRECTLY_REVIEWED_COUNT+" INTEGER NOT NULL DEFAULT 0, "+
			WordServantContract.ScriptureEntry.COLUMN_NAME_INCORRECTLY_REVIEWED_COUNT+" INTEGER NOT NULL DEFAULT 0, "+
			WordServantContract.ScriptureEntry.COLUMN_NAME_SKIPPED_REVIEW_COUNT+" INTEGER NOT NULL DEFAULT 0 "+
			");";
    
	private static String TAG_TABLE_CREATE = "CREATE TABLE "+WordServantContract.TagEntry.TABLE_NAME+" ("+
    		WordServantContract.TagEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
			WordServantContract.TagEntry.COLUMN_NAME_TAG_NAME+" TEXT NOT NULL);";
	
	private static String CATEGORY_TABLE_CREATE = "CREATE TABLE "+WordServantContract.CategoryEntry.TABLE_NAME+" ("+
    		WordServantContract.CategoryEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
			WordServantContract.CategoryEntry.COLUMN_NAME_SCRIPTURE_ID+ " INTEGER NOT NULL, "+
			WordServantContract.CategoryEntry.COLUMN_NAME_TAG_ID+ " INTEGER NOT NULL, "+
			"FOREIGN KEY ("+WordServantContract.CategoryEntry.COLUMN_NAME_SCRIPTURE_ID+") REFERENCES "+
				WordServantContract.ScriptureEntry.TABLE_NAME+"("+WordServantContract.ScriptureEntry._ID+"), "+
			"FOREIGN KEY ("+WordServantContract.CategoryEntry.COLUMN_NAME_TAG_ID+") REFERENCES "+
				WordServantContract.TagEntry.TABLE_NAME+"("+WordServantContract.TagEntry._ID+"));";
	

	public WordServantDbHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Creates the scripture bank table when the database is created.
		db.execSQL(SCRIPTURE_BANK_TABLE_CREATE);
		db.execSQL(TAG_TABLE_CREATE);
		db.execSQL(CATEGORY_TABLE_CREATE);
	}

	public void onOpen(SQLiteDatabase db){
		super.onOpen(db);
		db.execSQL("PRAGMA foreign_keys=ON;");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Kills the table and existing data
        db.execSQL("DROP TABLE IF EXISTS "+ WordServantContract.ScriptureEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ WordServantContract.TagEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ WordServantContract.CategoryEntry.TABLE_NAME);

        // Recreates the database with a new version
        onCreate(db);
	}
}
