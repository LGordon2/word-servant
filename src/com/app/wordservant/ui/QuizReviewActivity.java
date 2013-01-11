package com.app.wordservant.ui;

import java.util.Random;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.text.Html;
import android.util.SparseIntArray;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.app.wordservant.R;
import com.app.wordservant.provider.WordServantContract;



public class QuizReviewActivity extends SherlockFragmentActivity {
	private SparseIntArray mAllScriptureIds;
	private Cursor quizData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz_review);
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	public void onStart(){
		super.onStart();
		quizData = new CursorLoader(this){
			public Cursor loadInBackground(){
				//Open the database.

				String [] columns = {
						WordServantContract.ScriptureEntry._ID,
						WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE,
						WordServantContract.ScriptureEntry.COLUMN_NAME_TEXT,
						WordServantContract.ScriptureEntry.COLUMN_NAME_CORRECTLY_REVIEWED_COUNT,
						WordServantContract.ScriptureEntry.COLUMN_NAME_INCORRECTLY_REVIEWED_COUNT,
						WordServantContract.ScriptureEntry.COLUMN_NAME_SKIPPED_REVIEW_COUNT
				};
				String selection = WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE+"<=date('now','localtime') OR "+
						WordServantContract.ScriptureEntry.COLUMN_NAME_TIMES_REVIEWED+">0";
				return getContentResolver().query(
						WordServantContract.ScriptureEntry.CONTENT_URI, 
						columns, selection, null, null);
			}
		}.loadInBackground();

		//Put all Ids in a sparse int array.
		mAllScriptureIds  = new SparseIntArray(quizData.getCount());
		for(int i=0;i<quizData.getCount();i++){
			quizData.moveToPosition(i);
			mAllScriptureIds.append(i,quizData.getInt(0));
		}

		displayScriptureContent(quizData);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_quiz_review, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//Id.
		Integer id = quizData.getInt(0);

		//Update missed.
		ContentValues contentValues = new ContentValues();

		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.reviewed:
			contentValues.put(
					WordServantContract.ScriptureEntry.COLUMN_NAME_CORRECTLY_REVIEWED_COUNT,
					quizData.getInt(quizData.getColumnIndex(WordServantContract.ScriptureEntry.COLUMN_NAME_CORRECTLY_REVIEWED_COUNT))+1);
			getContentResolver().update(
					Uri.withAppendedPath(WordServantContract.ScriptureEntry.CONTENT_ID_URI_BASE,String.valueOf(id)),
					contentValues, null, null);
			//Display new scripture.
			displayScriptureContent(quizData);
			break;
		case R.id.skip:
			mAllScriptureIds.put(quizData.getPosition(), id);
			contentValues.put(
					WordServantContract.ScriptureEntry.COLUMN_NAME_SKIPPED_REVIEW_COUNT,
					quizData.getInt(quizData.getColumnIndex(WordServantContract.ScriptureEntry.COLUMN_NAME_SKIPPED_REVIEW_COUNT))+1);
			getContentResolver().update(
					Uri.withAppendedPath(WordServantContract.ScriptureEntry.CONTENT_ID_URI_BASE,String.valueOf(id)),
					contentValues, null, null);

			//Display new scripture.
			displayScriptureContent(quizData);
			break;
		case R.id.missed:
			contentValues.put(
					WordServantContract.ScriptureEntry.COLUMN_NAME_INCORRECTLY_REVIEWED_COUNT,
					quizData.getInt(quizData.getColumnIndex(WordServantContract.ScriptureEntry.COLUMN_NAME_INCORRECTLY_REVIEWED_COUNT))+1);
			getContentResolver().update(
					Uri.withAppendedPath(WordServantContract.ScriptureEntry.CONTENT_ID_URI_BASE,String.valueOf(id)),
					contentValues, null, null);

			//Display new scripture.
			displayScriptureContent(quizData);
		}
		return super.onOptionsItemSelected(item);
	}

	protected void displayScriptureContent(Cursor scriptureQuery) {
		// TODO Auto-generated method stub
		try{
			if(mAllScriptureIds.size()==0){
				finish();
				return;
			}
			/*if(mAllScriptureIds.size()==1){
				Button nextButton = (Button) findViewById(R.id.nextButton);
				nextButton.setVisibility(Button.GONE);
			}*/

			//Get random index.
			Integer randomIndex = getRandomIdIndex(mAllScriptureIds);
			scriptureQuery.moveToPosition(mAllScriptureIds.keyAt(randomIndex));
			mAllScriptureIds.removeAt(randomIndex);
			ReviewFragment fragment = (ReviewFragment) getSupportFragmentManager().findFragmentById(R.id.quizReviewFragment);
			fragment.setScriptureReference(scriptureQuery.getString(1));

			fragment.setScriptureText(Html.fromHtml(scriptureQuery.getString(2)));

		} catch(SQLiteException e){
			System.err.println("Database issue..");
			e.printStackTrace();
		}
	}

	private Integer getRandomIdIndex(SparseIntArray allScriptureIds) {
		// TODO Auto-generated method stub
		if(allScriptureIds.size()==1)
			return 0;
		Integer randomIndex = ((Integer) Math.abs(new Random().nextInt()) % allScriptureIds.size());
		return mAllScriptureIds.keyAt(randomIndex) != quizData.getPosition() ? randomIndex : getRandomIdIndex(allScriptureIds);
	}

}
