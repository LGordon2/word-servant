package com.app.wordservant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DisplaySelectedScripture extends Activity {

	String mReference;
	ArrayList<Integer> mVerseNumbers;
	int mChapterNumber;
	String mBookName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_selected_scripture);
		TextView text = (TextView) findViewById(R.id.selectedScriptureText);
		TextView referenceView = (TextView) findViewById(R.id.scriptureReference);

		//Grabbing intent values..
		if(savedInstanceState==null){
			mVerseNumbers = this.getIntent().getBundleExtra("bundle").getIntegerArrayList("verses");
			mChapterNumber = this.getIntent().getIntExtra("chapter_number", 0);
			mBookName = this.getIntent().getStringExtra("book_name");
		}else{
			mVerseNumbers = savedInstanceState.getIntegerArrayList("verses");
			mChapterNumber = savedInstanceState.getInt("chapter_number", 0);
			mBookName = savedInstanceState.getString("book_name");
		}
		String createdScriptureText = "";
		String verseString = "";
		Bible.BibleVerse currentVerse = null;
		if(mVerseNumbers.size() < Bible.getInstance().getBook(mBookName).chapters.get(mChapterNumber-1).getVersesArray().length){
			for(int i=0;i<mVerseNumbers.size();i++){
				//verses.add(Bible.getInstance().getBook(bookName).chapters.get(chapterNumber).getVerse(i));
				currentVerse = Bible.getInstance().getBook(mBookName).chapters.get(mChapterNumber-1).getVerse(mVerseNumbers.get(i)-1);
				createdScriptureText += "<sup><small>"+currentVerse.verseNumber+"</small></sup>"+currentVerse.text;
				if(i==0){
					verseString = String.valueOf(mVerseNumbers.get(i));
				}else if(mVerseNumbers.get(i)==mVerseNumbers.get(i-1)+1){
					verseString+= "-";
					i+=1;
					while(i<mVerseNumbers.size() && mVerseNumbers.get(i)==mVerseNumbers.get(i-1)+1){
						currentVerse = Bible.getInstance().getBook(mBookName).chapters.get(mChapterNumber-1).getVerse(mVerseNumbers.get(i)-1);
						createdScriptureText += "<sup><small>"+currentVerse.verseNumber+"</small></sup>"+currentVerse.text;
						i+=1;
					}
					i-=1;
					verseString+= mVerseNumbers.get(i);
				}else{
					verseString+=",";
					verseString+=mVerseNumbers.get(i);
				}

			}
			mReference = mBookName+" "+mChapterNumber+":"+verseString;
		}
		else{
			mReference = mBookName+" "+mChapterNumber;
			for(int i=0;i<mVerseNumbers.size();i++){
				currentVerse = Bible.getInstance().getBook(mBookName).chapters.get(mChapterNumber-1).getVerse(mVerseNumbers.get(i)-1);
				createdScriptureText += "<sup><small>"+currentVerse.verseNumber+"</small></sup>"+currentVerse.text;
			}
		}
		final String scriptureText = createdScriptureText;
		text.setText(Html.fromHtml(scriptureText));
		referenceView.setText(mReference);

		Button addScriptureButton = (Button) findViewById(R.id.addSelectedScripture);
		addScriptureButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				SimpleDateFormat dbDateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format), Locale.US);
				ContentValues scriptureValues = new ContentValues();
				scriptureValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE, mReference);
				scriptureValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_TEXT, scriptureText);

				String [] columnsToRetrieve = {
						WordServantContract.ScriptureEntry.COLUMN_NAME_SCHEDULE,
						WordServantContract.ScriptureEntry.COLUMN_NAME_TIMES_REVIEWED,
						WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE};
				SQLiteDatabase wordservant_db = new WordServantDbHelper(DisplaySelectedScripture.this, WordServantContract.DB_NAME, null, WordServantDbHelper.DATABASE_VERSION).getWritableDatabase();
				Cursor runningScriptureQuery = wordservant_db.query(
						WordServantContract.ScriptureEntry.TABLE_NAME, 
						columnsToRetrieve, 
						WordServantContract.ScriptureEntry.COLUMN_NAME_SCHEDULE+"='daily' AND "+
								WordServantContract.ScriptureEntry.COLUMN_NAME_TIMES_REVIEWED+"<7", null, null, null, null);
				if (runningScriptureQuery.getCount()>0){
					runningScriptureQuery.moveToLast();
					Calendar currentCalendar = Calendar.getInstance();
					try {
						currentCalendar.setTime(dbDateFormat.parse(runningScriptureQuery.getString(2)));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					currentCalendar.add(Calendar.DATE, 7-runningScriptureQuery.getInt(1));
					scriptureValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE, dbDateFormat.format(currentCalendar.getTime()));
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
				setResult(0);
				finish();
			}

		});
	}

	protected void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		outState.putIntegerArrayList("verses", this.getIntent().getBundleExtra("bundle").getIntegerArrayList("verses"));
		outState.putInt("chapter_number", this.getIntent().getIntExtra("chapter_number", 0));
		outState.putString("book_name", this.getIntent().getStringExtra("book_name"));
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_display_selected_scripture,
				menu);
		return true;
	}
	public void onBackPressed(){
		setResult(1);
		super.onBackPressed();
	}
}
