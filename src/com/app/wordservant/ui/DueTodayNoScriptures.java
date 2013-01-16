package com.app.wordservant.ui;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.app.wordservant.R;
import com.app.wordservant.provider.WordServantContract;
import com.app.wordservant.ui.RereviewScriptureDialogFragment.DialogListener;

public class DueTodayNoScriptures extends SherlockFragmentActivity implements DialogListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_due_today_no_scriptures);
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Calendar currentCalendar = Calendar.getInstance();
		getSupportActionBar().setSubtitle(DateFormat.format("EEEE, MMMM dd, yyyy", currentCalendar));
		
		//If there are scriptures to review go right to the review screen.
		String [] columnsToRetrieve = {WordServantContract.ScriptureEntry._ID};
		Cursor mUnreviewedScriptureQuery = new CursorLoader(this, WordServantContract.ScriptureEntry.CONTENT_URI, columnsToRetrieve, 
				WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE+"=date('now','localtime')", null, null).loadInBackground();
		if(mUnreviewedScriptureQuery.getCount()>0){
			Intent intent = new Intent(this, ScriptureReview.class);
			intent.putIntegerArrayListExtra("unreviewedScriptureIds", new ArrayList<Integer>());
			startActivityForResult(intent, 0);
		}
		
		//Set up rereview button.
		this.findViewById(R.id.rereviewButton).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogFragment dialog = new RereviewScriptureDialogFragment();
				FragmentManager manager = getSupportFragmentManager();
				dialog.show(manager, "rereview");
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater()
				.inflate(R.menu.activity_due_today_no_scriptures, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPositiveButtonClicked(Integer[] ids) {
		// TODO Auto-generated method stub
		ArrayList<Integer> unreviewedScriptureIds = new ArrayList<Integer>();
		for(int i=0;i<ids.length;i++){
			unreviewedScriptureIds.add(ids[i]);
		}
		Intent intent = new Intent(this, ScriptureReview.class);
		intent.putIntegerArrayListExtra("unreviewedScriptureIds", unreviewedScriptureIds);
		startActivityForResult(intent, 0);
	}

}
