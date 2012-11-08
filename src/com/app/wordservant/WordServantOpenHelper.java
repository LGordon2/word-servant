package com.app.wordservant;

import com.app.wordservant.R;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class WordServantOpenHelper extends SQLiteOpenHelper{
	
	private static String SCRIPTURE_BANK_TABLE_NAME;
	private static String TAG_TABLE_NAME;
    private static String SCRIPTURE_BANK_TABLE_CREATE;
	private static String TAG_TABLE_CREATE;
    
	public WordServantOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		SCRIPTURE_BANK_TABLE_NAME = context.getResources().getString(R.string.scripture_table_name);
		TAG_TABLE_NAME = context.getResources().getString(R.string.tag_table_name);
		updateCreateTableStrings();
	}

	private void updateCreateTableStrings() {
		// Create the tables to be used in the database.
		TAG_TABLE_CREATE = "CREATE TABLE " + TAG_TABLE_NAME + " (" +
				" TAG_ID INTEGER PRIMARY KEY, "+
				" NAME TEXT NOT NULL);";
		
		SCRIPTURE_BANK_TABLE_CREATE =
	            "CREATE TABLE " + SCRIPTURE_BANK_TABLE_NAME + " (" +
	            " SCRIPTURE_ID INTEGER PRIMARY KEY, " +
	            " REFERENCE TEXT NOT NULL, " +
	            " TAG_ID INTEGER, " +
	            " TEXT TEXT NOT NULL, " +
	            " CREATED_DATE TEXT NOT NULL, " + 
	            " SCHEDULE TEXT NOT NULL, "+
	            " LAST_REVIEWED_DATE TEXT," +
	            " TIMES_REVIEWED INTEGER NOT NULL," +
	            " NEXT_REVIEW_DATE TEXT NOT NULL);";
	            //" FOREIGN_KEY(TAG_ID) REFERENCES "+TAG_TABLE_NAME+"(TAG_ID));";
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
        db.execSQL("DROP TABLE IF EXISTS "+ SCRIPTURE_BANK_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ TAG_TABLE_NAME);

        // Recreates the database with a new version
        onCreate(db);
	}

}
