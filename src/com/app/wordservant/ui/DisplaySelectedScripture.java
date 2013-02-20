package com.app.wordservant.ui;

import java.util.ArrayList;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.app.wordservant.R;
import com.app.wordservant.bible.Bible;
import com.app.wordservant.bible.BibleVerse;
import com.app.wordservant.provider.WordServantContract;

public class DisplaySelectedScripture extends SherlockActivity {

	String mReference;
	ArrayList<Integer> mVerseNumbers;
	int mChapterNumber;
	String mBookName;
	private String mScriptureText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_selected_scripture);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		TextView text = (TextView) findViewById(R.id.selectedScriptureText);

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
		BibleVerse currentVerse = null;
		if(mVerseNumbers.size() < Bible.getInstance().getBook(mBookName).mBibleChapters.get(mChapterNumber-1).getVersesArray().length){
			for(int i=0;i<mVerseNumbers.size();i++){
				//verses.add(Bible.getInstance().getBook(bookName).chapters.get(chapterNumber).getVerse(i));
				currentVerse = Bible.getInstance().getBook(mBookName).mBibleChapters.get(mChapterNumber-1).mBibleVerses.get(mVerseNumbers.get(i)-1);
				createdScriptureText += "<sup><small>"+currentVerse.mVerseNumber+"</small></sup>"+currentVerse.mVerseText;
				if(i==0){
					verseString = String.valueOf(mVerseNumbers.get(i));
				}else if(mVerseNumbers.get(i)==mVerseNumbers.get(i-1)+1){
					verseString+= "-";
					i+=1;
					while(i<mVerseNumbers.size() && mVerseNumbers.get(i)==mVerseNumbers.get(i-1)+1){
						currentVerse = Bible.getInstance().getBook(mBookName).mBibleChapters.get(mChapterNumber-1).mBibleVerses.get(mVerseNumbers.get(i)-1);
						createdScriptureText += "<sup><small>"+currentVerse.mVerseNumber+"</small></sup>"+currentVerse.mVerseText;
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
				currentVerse = Bible.getInstance().getBook(mBookName).mBibleChapters.get(mChapterNumber-1).mBibleVerses.get(mVerseNumbers.get(i)-1);
				createdScriptureText += "<sup><small>"+currentVerse.mVerseNumber+"</small></sup>"+currentVerse.mVerseText;
			}
		}
		mScriptureText = createdScriptureText;
		text.setText(Html.fromHtml(mScriptureText));
		getSupportActionBar().setTitle(mReference);
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
		getSupportMenuInflater().inflate(R.menu.activity_display_selected_scripture,
				menu);
		return true;
	}
	public boolean onMenuItemSelected(int featureId, MenuItem item){
		switch(item.getItemId()){
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.add:
			ContentValues scriptureValues = new ContentValues();
			scriptureValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE, mReference);
			scriptureValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_TEXT, mScriptureText);
			scriptureValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE, (String) null);

			//Open the database and add the row.
			DisplaySelectedScripture.this.getContentResolver().insert(WordServantContract.ScriptureEntry.CONTENT_URI, scriptureValues);
			setResult(0);
			finish();
		}
		return true;
	}
	public void onBackPressed(){
		setResult(1);
		super.onBackPressed();
	}
}
