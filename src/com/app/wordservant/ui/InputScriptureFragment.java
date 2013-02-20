package com.app.wordservant.ui;

import android.content.ContentValues;
import android.content.Intent;
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
			ContentValues scriptureValues = new ContentValues();
			scriptureValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE, scriptureReference.getText().toString());
			scriptureValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_TEXT, scriptureText.getText().toString());
			scriptureValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE, (String)null);
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
