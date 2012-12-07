package com.app.wordservant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DisplaySelectedScripture extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_selected_scripture);
		TextView t = (TextView) findViewById(R.id.selectedScriptureText);
		TextView referenceView = (TextView) findViewById(R.id.scriptureReference);
		final String scriptureText = this.getIntent().getStringExtra("scripture_text");
		final String reference = this.getIntent().getStringExtra("reference");
		t.setText(scriptureText);
		referenceView.setText(reference);
		
		Button addScriptureButton = (Button) findViewById(R.id.addSelectedScripture);
		addScriptureButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*SQLiteDatabase db = new WordServantDbHelper(getApplicationContext(), getResources().getString(R.string.database_name), null, 1).getWritableDatabase();
				ContentValues contentValues = new ContentValues(); 
				contentValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE, reference);
				contentValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_TEXT, scriptureText);
				db.insert(WordServantContract.ScriptureEntry.TABLE_NAME, null, contentValues);*/
				
				SimpleDateFormat dbDateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format), Locale.US);
				ContentValues scriptureValues = new ContentValues();
				scriptureValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE, reference);
				scriptureValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_TEXT, scriptureText);
				
				String [] columnsToRetrieve = {"SCHEDULE","TIMES_REVIEWED","NEXT_REVIEW_DATE"};
				SQLiteDatabase wordservant_db = new WordServantDbHelper(getApplicationContext(), getResources().getString(R.string.database_name), null, 1).getWritableDatabase();
				Cursor runningScriptureQuery = wordservant_db.query("scriptures", columnsToRetrieve, "SCHEDULE='daily' AND TIMES_REVIEWED<7", null, null, null, null);
				if (runningScriptureQuery.getCount()>0){
					runningScriptureQuery.moveToLast();
					Calendar currentCalendar = Calendar.getInstance();
					try {
						currentCalendar.setTime(dbDateFormat.parse(runningScriptureQuery.getString(2)));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					currentCalendar.add(Calendar.DATE, 7-runningScriptureQuery.getInt(1));
					scriptureValues.put("next_review_date", dbDateFormat.format(currentCalendar.getTime()));
				}
				
				//Open the database and add the row.
				try{
					wordservant_db.insert(WordServantContract.ScriptureEntry.TABLE_NAME, null, scriptureValues);
				} catch(SQLiteException e){
					System.err.println("Error with SQL statement.");
					e.printStackTrace();
				} finally{
					wordservant_db.close();
				}
				finish();
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_display_selected_scripture,
				menu);
		return true;
	}

}
