package com.app.wordservant;

import java.util.concurrent.ExecutionException;

import com.app.wordservant.R;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EditScripture extends Activity {

	private EditText mEditScriptureReference;
	private EditText mEditCategory;
	private EditText mEditScripture;
	private int selectedScriptureId;

	private class ScriptureQuerier extends AsyncTask<Integer, Integer, Cursor>{

		@Override
		protected Cursor doInBackground(Integer... scriptureId) {
			// Gets all the scriptures from the database.
			String [] columns_to_retrieve = {
					WordServantContract.ScriptureEntry._ID,
					WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE,
					WordServantContract.ScriptureEntry.COLUMN_NAME_TEXT};
			SQLiteDatabase wordservantReadableDatabase = new WordServantDbHelper(
					EditScripture.this, 
					WordServantContract.DB_NAME, 
					null, 
					WordServantDbHelper.DATABASE_VERSION).getReadableDatabase();
			Cursor scriptureQuery = wordservantReadableDatabase.query(
					WordServantContract.ScriptureEntry.TABLE_NAME, 
					columns_to_retrieve, 
					WordServantContract.ScriptureEntry._ID+"="+scriptureId[0], 
					null, null, null, null);
			scriptureQuery.moveToFirst();
			wordservantReadableDatabase.close();
			return scriptureQuery;
		}

	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        //Open the database as readable and get the scripture id from the intent passed to the activity.
        selectedScriptureId = this.getIntent().getIntExtra("scripture_id", 0);
        ScriptureQuerier dbQuerier = new ScriptureQuerier();
        dbQuerier.execute(selectedScriptureId);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_scripture_manual);
        
        //Associate Java objects with the edit fields displayed on the screen.
        mEditScriptureReference = (EditText) findViewById(R.id.scriptureReference);
       // editCategory = (EditText) findViewById(R.id.categoryName);
        mEditScripture = (EditText) findViewById(R.id.scriptureText);
        
        Button editDoneButton = (Button) findViewById(R.id.doneButton);
		editDoneButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				
				ContentValues updatedItems = new ContentValues();
				updatedItems.put("reference", mEditScriptureReference.getText().toString());
				//updatedItems.put("category", editCategory.getText().toString());
				updatedItems.put("text", mEditScripture.getText().toString());
				SQLiteDatabase wordservantReadableDatabase = new WordServantDbHelper(
						EditScripture.this, 
						WordServantContract.DB_NAME, 
						null, 
						WordServantDbHelper.DATABASE_VERSION).getWritableDatabase();
				wordservantReadableDatabase.update(
						WordServantContract.ScriptureEntry.TABLE_NAME, 
						updatedItems, 
						WordServantContract.ScriptureEntry._ID+"="+selectedScriptureId, 
						null);
				wordservantReadableDatabase.close();
				finish();
			}
			
		});
        
        //Get the information from database.
        try{
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
		}
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit_scripture, menu);
        return true;
    }
}
