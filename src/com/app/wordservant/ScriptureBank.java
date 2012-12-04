package com.app.wordservant;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ScriptureBank extends Activity {
	protected CursorAdapter scriptureAdapter;
	private Cursor scriptureQuery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scripture_bank);
        
    }
	private class ScriptureQuerier extends AsyncTask<Void, Void, Cursor>{

		@Override
		protected Cursor doInBackground(Void... params) {
			// Gets the scripture that matches the scripture id.
			String [] columns_to_retrieve = {"_id", "reference"};
	        SQLiteDatabase wordservantReadableDatabase = new WordServantDbHelper(getApplicationContext(), "wordservant_db", null, WordServantDbHelper.DATABASE_VERSION).getReadableDatabase();
			Cursor scriptureQuery = wordservantReadableDatabase.query(getResources().getString(R.string.scripture_table_name), columns_to_retrieve, null, null, null, null, null);
			scriptureQuery.moveToFirst();
			wordservantReadableDatabase.close();
			return scriptureQuery;
		}

	}
    
    /**
     * When the activity starts clear out any scriptures displayed on the screen 
     * and populate the screen based on the scriptures in the database.
     */
    protected void onStart(){
    	super.onStart();
    	ScriptureQuerier dbQuerier = new ScriptureQuerier();
    	dbQuerier.execute();
    	
    	//Functionality for the input scripture button.
        Button inputScripture = (Button) this.findViewById(R.id.input_scripture);
        inputScripture.setOnClickListener(new OnClickListener(){

			public void onClick(View scriptureBank) {
				// Starts a new input scripture.
				Intent intent = new Intent(scriptureBank.getContext(),InputScriptureManual.class);
		    	startActivity(intent);
			}
        	
        });
        
        //Displays the scripture bank.
		try {
			String [] fromColumns = {WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE};
			int [] toViews = {R.id.list_entry};
			scriptureQuery = dbQuerier.get();
			scriptureAdapter = new SimpleCursorAdapter(this, R.layout.list_layout, scriptureQuery, fromColumns, toViews);
			ListView scriptureList = (ListView) findViewById(R.id.scripture_bank_list);
			scriptureList.setAdapter(scriptureAdapter);
			
			scriptureList.setOnItemClickListener(new OnItemClickListener(){
				public void onItemClick(AdapterView<?> parent, View linearLayout, int position,long id) {
					// TODO Auto-generated method stub
					// Bundle data
					Intent intent = new Intent(linearLayout.getContext(),EditScripture.class);
					scriptureQuery.moveToPosition(position);
					intent.putExtra("scripture_id",scriptureQuery.getInt(0));
			    	startActivity(intent);
					
				}
			});
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
        //getMenuInflater().inflate(R.menu.landing_screen, menu);
        return true;
    }
	
	/**
	 * When the activity is destroyed close the database and the cursor.
	 */
	protected void onDestroy(){
		super.onDestroy();
		scriptureQuery.close();
	}
}
