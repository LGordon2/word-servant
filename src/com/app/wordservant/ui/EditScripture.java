package com.app.wordservant.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.app.wordservant.R;
import com.app.wordservant.provider.WordServantContract;

public class EditScripture extends SherlockFragmentActivity{

	public static class EditScriptureFragment extends SherlockFragment implements LoaderManager.LoaderCallbacks<Cursor>{
		private EditText mEditScriptureReference;
		private EditText mEditCategory;
		private EditText mEditScripture;
		
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			this.setHasOptionsMenu(true);
		}
		
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
			return inflater.inflate(R.layout.activity_input_scripture_manual, null);
		}
		
		public void onViewCreated(View view, Bundle savedInstanceState){
			super.onViewCreated(view, savedInstanceState);
	        //Associate Java objects with the edit fields displayed on the screen.
	        mEditScriptureReference = (EditText) getView().findViewById(R.id.scriptureReference);
	       // editCategory = (EditText) findViewById(R.id.categoryName);
	        mEditScripture = (EditText) getView().findViewById(R.id.scriptureText);
	        getActivity().getSupportLoaderManager().initLoader(0, null, this);
	        /*Button editDoneButton = (Button) getView().findViewById(R.id.doneButton);
			editDoneButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					
					ContentValues updatedItems = new ContentValues();
					updatedItems.put("reference", mEditScriptureReference.getText().toString());
					//updatedItems.put("category", editCategory.getText().toString());
					updatedItems.put("text", mEditScripture.getText().toString());
					getActivity().getContentResolver().update(Uri.withAppendedPath(WordServantContract.ScriptureEntry.CONTENT_ID_URI_BASE, String.valueOf(getActivity().getIntent().getLongExtra("scripture_id", 0))), 
							updatedItems, null, null);
					getActivity().finish();
				}
				
			});*/
		}
		
		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			// TODO Auto-generated method stub
			String [] columns_to_retrieve = {
					WordServantContract.ScriptureEntry._ID,
					WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE,
					WordServantContract.ScriptureEntry.COLUMN_NAME_TEXT};
			return new CursorLoader(getActivity(), 
					Uri.withAppendedPath(WordServantContract.ScriptureEntry.CONTENT_ID_URI_BASE, String.valueOf(getActivity().getIntent().getLongExtra("scripture_id", 0))), 
					columns_to_retrieve, 
					null, null, null);
		}
		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			data.moveToFirst();
			// TODO Auto-generated method stub
			mEditScriptureReference.setText(data.getString(1));
			//meditCategory.setText(scriptureQuery.getString(2));
			mEditScripture.setText(Html.fromHtml(data.getString(2)));
			// The list should now be shown.
		}
		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			// TODO Auto-generated method stub
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
				ContentValues updatedItems = new ContentValues();
				updatedItems.put("reference", mEditScriptureReference.getText().toString());
				//updatedItems.put("category", editCategory.getText().toString());
				updatedItems.put("text", Html.toHtml(mEditScripture.getText()));
				getActivity().getContentResolver().update(Uri.withAppendedPath(WordServantContract.ScriptureEntry.CONTENT_ID_URI_BASE, String.valueOf(getActivity().getIntent().getLongExtra("scripture_id", 0))), 
						updatedItems, null, null);
				getActivity().finish();
			}
			return true;
		}
		
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        //Open the database as readable and get the scripture id from the intent passed to the activity.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_scripture);
		
    }

}
