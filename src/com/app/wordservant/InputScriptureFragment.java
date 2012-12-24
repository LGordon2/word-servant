package com.app.wordservant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class InputScriptureFragment extends Fragment {

	private SQLiteDatabase wordservant_db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	public void onStart(){
		super.onStart();
		Button doneButton = (Button) getActivity().findViewById(R.id.doneButton);
		doneButton.setOnClickListener(new OnClickListener(){


			/**
			 * Provides the functionality for the done button.
			 */
			public void onClick(View inputScriptureView) {
				// Get the referenced fields.
				EditText scriptureReference = (EditText) getActivity().findViewById(R.id.scriptureReference);
				EditText scriptureText = (EditText) getActivity().findViewById(R.id.scriptureText);

				// Check for required fields.
				if (scriptureReference.getText().toString().equals("")){
					Toast.makeText(inputScriptureView.getContext(),"Scripture Reference is a required field.", Toast.LENGTH_SHORT).show();
					return;
				}
				if (scriptureText.getText().toString().equals("")){
					Toast.makeText(inputScriptureView.getContext(),"Scripture Text is a required field.", Toast.LENGTH_SHORT).show();
					return;
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

		});
		Button addTagButton = (Button) getActivity().findViewById(R.id.addTagButton);
		addTagButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(getActivity(), "Not yet implemented.", Toast.LENGTH_SHORT).show();
				//AddTagDialogFragmentAlt dialogFragment = new AddTagDialogFragmentAlt();
				//dialogFragment.show(getFragmentManager(), "tags");
			}

		});
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.activity_input_scripture_manual, null);

	}
}
