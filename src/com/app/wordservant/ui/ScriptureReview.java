package com.app.wordservant.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.text.format.DateFormat;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.app.wordservant.R;
import com.app.wordservant.provider.WordServantContract;

public class ScriptureReview extends SherlockFragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>{

	private ArrayList<Integer> mUnreviewedScriptureIds;
	private final int UNREVIEWED_SCRIPTURE_QUERY = 0;
	private final int CURRENT_SCRIPTURE_QUERY = 1;
	private int mCurrentIdPosition;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scripture_review);
		Calendar currentCalendar = Calendar.getInstance();
		((TextView) this.findViewById(R.id.todaysDate)).setText(DateFormat.format("EEEE, MMMM dd, yyyy", currentCalendar));
		mUnreviewedScriptureIds = new ArrayList<Integer>();
		mCurrentIdPosition = getIntent().getIntExtra("positionOnScreen", 0);
		final Bundle bundledScriptureList = getIntent().getBundleExtra("bundledScriptureList");
		int mFirstSelectedScriptureId = bundledScriptureList.getInt(String.valueOf(mCurrentIdPosition));
		

		String [] columnsToRetrieve = {WordServantContract.ScriptureEntry._ID};
		Cursor mUnreviewedScriptureQuery = new CursorLoader(this, WordServantContract.ScriptureEntry.CONTENT_URI, columnsToRetrieve, 
				WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE+"=date('now','localtime') OR "+
						WordServantContract.ScriptureEntry._ID+"="+mFirstSelectedScriptureId, null, null).loadInBackground();
		
		for(int i=0;i<mUnreviewedScriptureQuery.getCount();i++){
			mUnreviewedScriptureQuery.moveToPosition(i);
			mUnreviewedScriptureIds.add(mUnreviewedScriptureQuery.getInt(0));
		}
		this.invalidateOptionsMenu();
	}
	
	public void onStart(){
		super.onStart();
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		Fragment fragment = null;
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		//Display flashcards if the setting is enabled.
		if(sharedPreferences.getString("pref_key_review_select", "none").equals("showing_reference") ||
				sharedPreferences.getString("pref_key_review_select", "none").equals("showing_scripture")){
			fragment = new FlashcardFragment();

		}else{
			fragment = new StaticDisplayReviewFragment();
		}
		fragmentTransaction.replace(R.id.fragmentHolder, fragment);
		fragmentTransaction.commit();
		getSupportLoaderManager().restartLoader(CURRENT_SCRIPTURE_QUERY, null, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_scripture_review, menu);
		return true;
	}

	public boolean onPrepareOptionsMenu (Menu menu){
		if(mUnreviewedScriptureIds.size()==1){
			menu.findItem(R.id.skip).setEnabled(false);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		Intent intent;
		switch (item.getItemId()) {
		case R.id.reviewed:
			String [] columnsToRetrieve = {WordServantContract.ScriptureEntry._ID,
					WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE};
			Cursor currentScriptureReviewDateQuery = new CursorLoader(this, 
					Uri.withAppendedPath(WordServantContract.ScriptureEntry.CONTENT_ID_URI_BASE,String.valueOf(mUnreviewedScriptureIds.get(mCurrentIdPosition))), 
					columnsToRetrieve, 
					null, null, null).loadInBackground();
			currentScriptureReviewDateQuery.moveToFirst();
			//Update the database.
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String scriptureDateNextReviewDate = currentScriptureReviewDateQuery.getString(currentScriptureReviewDateQuery.getColumnIndex(WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE));
			try {
				if(dateFormat.parse(scriptureDateNextReviewDate).compareTo(calendar.getTime())<=0){
					updateReviewedScripture(this, currentScriptureReviewDateQuery.getInt(0), true);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			mUnreviewedScriptureIds.remove(mCurrentIdPosition);
			if(mUnreviewedScriptureIds.size()==0){
				finish();
				return true;
			}
			mCurrentIdPosition = mCurrentIdPosition < mUnreviewedScriptureIds.size() ? mCurrentIdPosition : 0;
			break;
		case R.id.skip:
			mCurrentIdPosition = mCurrentIdPosition+1 < mUnreviewedScriptureIds.size() ? mCurrentIdPosition+1 : 0;
			getSupportLoaderManager().restartLoader(CURRENT_SCRIPTURE_QUERY, null, this);
			break;
		case R.id.settings:
        	intent = new Intent(this, Settings.class);
        	startActivityForResult(intent, 10);
        	break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	public void updateReviewedScripture(Context context, int scriptureId, boolean increment){
		String incrementOrDecrement = increment
				?"INCREMENT_TIMES_REVIEWED":"DECREMENT_TIMES_REVIEWED";
		getContentResolver().update(
				Uri.withAppendedPath(WordServantContract.ScriptureEntry.CONTENT_ID_URI_BASE, String.valueOf(scriptureId)+"/"+incrementOrDecrement), null, null, null);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		if(id == CURRENT_SCRIPTURE_QUERY){
			String [] columnsToRetrieve = {WordServantContract.ScriptureEntry._ID,
					WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE,
					WordServantContract.ScriptureEntry.COLUMN_NAME_TEXT};
			return new CursorLoader(this, 
					Uri.withAppendedPath(WordServantContract.ScriptureEntry.CONTENT_ID_URI_BASE,String.valueOf(mUnreviewedScriptureIds.get(mCurrentIdPosition))), 
					columnsToRetrieve, 
					null, null, null);
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// TODO Auto-generated method stub
		if(loader.getId() == CURRENT_SCRIPTURE_QUERY){
			data.moveToFirst();
			FragmentManager fragmentManager = getSupportFragmentManager();
			((ReviewFragment) fragmentManager.findFragmentById(R.id.fragmentHolder)).setScriptureReference(data.getString(1));
			((ReviewFragment) fragmentManager.findFragmentById(R.id.fragmentHolder)).setScriptureText(Html.fromHtml(data.getString(2)));
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub

	}
}
