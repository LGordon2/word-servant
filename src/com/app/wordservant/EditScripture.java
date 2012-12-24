package com.app.wordservant;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EditScripture extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>{

	private EditText mEditScriptureReference;
	private EditText mEditCategory;
	private EditText mEditScripture;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        //Open the database as readable and get the scripture id from the intent passed to the activity.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_scripture_manual);
        
        //Associate Java objects with the edit fields displayed on the screen.
        mEditScriptureReference = (EditText) findViewById(R.id.scriptureReference);
       // editCategory = (EditText) findViewById(R.id.categoryName);
        mEditScripture = (EditText) findViewById(R.id.scriptureText);
        getSupportLoaderManager().initLoader(0, null, this);
        Button editDoneButton = (Button) findViewById(R.id.doneButton);
		editDoneButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				
				ContentValues updatedItems = new ContentValues();
				updatedItems.put("reference", mEditScriptureReference.getText().toString());
				//updatedItems.put("category", editCategory.getText().toString());
				updatedItems.put("text", mEditScripture.getText().toString());
				getContentResolver().update(Uri.withAppendedPath(WordServantContract.ScriptureEntry.CONTENT_ID_URI_BASE, String.valueOf(getIntent().getLongExtra("scripture_id", 0))), 
						updatedItems, null, null);
				finish();
			}
			
		});
        
        //Get the information from database.
        /*try{
        	Cursor scriptureQuery = dbQuerier.get();
			mEditScriptureReference.setText(scriptureQuery.getString(1));
			//meditCategory.setText(scriptureQuery.getString(2));
			mEditScripture.setText(Html.fromHtml(scriptureQuery.getString(2)));
			scriptureQuery.close();
        } catch(SQLiteException e){
        	System.err.println("Database issue. Scripture not found.");
        	e.printStackTrace();
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit_scripture, menu);
        return true;
    }

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		String [] columns_to_retrieve = {
				WordServantContract.ScriptureEntry._ID,
				WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE,
				WordServantContract.ScriptureEntry.COLUMN_NAME_TEXT};
		return new CursorLoader(this, 
				Uri.withAppendedPath(WordServantContract.ScriptureEntry.CONTENT_ID_URI_BASE, String.valueOf(this.getIntent().getLongExtra("scripture_id", 0))), 
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
}
