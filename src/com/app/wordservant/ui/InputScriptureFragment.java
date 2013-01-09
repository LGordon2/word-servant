package com.app.wordservant.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.app.wordservant.R;
import com.app.wordservant.provider.WordServantContract;


public class InputScriptureFragment extends SherlockFragment {

	public void onStart(){
		super.onStart();
		this.setHasOptionsMenu(true);
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.activity_input_scripture_manual, null);

	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.activity_input_scripture_manual, menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			intent = new Intent(getActivity(), ScriptureBankActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case R.id.done:
			// Get the referenced fields.
			EditText scriptureReference = (EditText) getActivity().findViewById(R.id.scriptureReference);
			EditText scriptureText = (EditText) getActivity().findViewById(R.id.scriptureText);

			// Check for required fields.
			if (scriptureReference.getText().toString().equals("")){
				Toast.makeText(getActivity(),"Scripture Reference is a required field.", Toast.LENGTH_SHORT).show();
				return true;
			}
			if (scriptureText.getText().toString().equals("")){
				Toast.makeText(getActivity(),"Scripture Text is a required field.", Toast.LENGTH_SHORT).show();
				return true;
			}

			Runtime r = Runtime.getRuntime();
			// Map the values of the fields to columns that are used in the database.
			SimpleDateFormat dbDateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format), Locale.US);
			ContentValues scriptureValues = new ContentValues();
			scriptureValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE, scriptureReference.getText().toString());
			scriptureValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_TEXT, scriptureText.getText().toString());
			//Query for any "running" scriptures.
			String [] columnsToRetrieve = {
					WordServantContract.ScriptureEntry.COLUMN_NAME_SCHEDULE,
					WordServantContract.ScriptureEntry.COLUMN_NAME_TIMES_REVIEWED,
					WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE};
			Cursor runningScriptureQuery = getActivity().getContentResolver().query(
					WordServantContract.ScriptureEntry.CONTENT_URI, 
					columnsToRetrieve, 
					"SCHEDULE='daily' AND TIMES_REVIEWED<7", null, null);
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
			r.gc();

			//Open the database and add the row.
			getActivity().getContentResolver().insert(WordServantContract.ScriptureEntry.CONTENT_URI, scriptureValues);



			//Go back to the scripture bank screen.
			if(getFragmentManager().findFragmentById(R.id.scriptureBankFragment) == null){
				getActivity().finish();
			}else{
				getFragmentManager().findFragmentById(R.id.scriptureBankFragment).onStart();

				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.remove(fragmentManager.findFragmentByTag("input_scripture"));
				fragmentTransaction.commit();
			}
			

		}
		return super.onOptionsItemSelected(item);
	}
}
