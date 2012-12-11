package com.app.wordservant;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class AddTagActivity extends Activity {
	private class TagListLoader extends CursorLoader{

		public TagListLoader(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		public Cursor loadInBackground(){
			SQLiteDatabase db = new WordServantDbHelper(this.getContext(), WordServantContract.DB_NAME, null, WordServantDbHelper.DATABASE_VERSION).getWritableDatabase();
			String [] columns = {
					WordServantContract.TagEntry.COLUMN_NAME_TAG_NAME
			};
			return db.query(WordServantContract.TagEntry.TABLE_NAME, columns, null, null, null, null, null);
		}
		public void deliverResult(Cursor cursor){
			ListView tagList = (ListView) findViewById(R.id.tagList);
			SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this.getContext(), R.id.tagList, cursor, mProjection, null);
		}
	}
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		TagListLoader tagLoader = new TagListLoader(this);
		tagLoader.deliverResult(tagLoader.loadInBackground());
	}
}
