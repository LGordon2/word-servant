package com.app.wordservant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
	protected SQLiteDatabase wordservant_db;
	protected CursorAdapter scriptureAdapter;
	protected Cursor scriptureQuery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scripture_bank);
        wordservant_db = new WordServantOpenHelper(this.getApplicationContext(), getResources().getString(R.string.database_name), null, 1).getReadableDatabase();
        
    }
    
    /**
     * When the activity starts clear out any scriptures displayed on the screen 
     * and populate the screen based on the scriptures in the database.
     */
    protected void onStart(){
    	super.onStart();
    	displayScriptureBank();
    	
        Button inputScripture = (Button) this.findViewById(R.id.input_scripture);
        inputScripture.setOnClickListener(new OnClickListener(){

			public void onClick(View scriptureBank) {
				// Starts a new input scripture.
				Intent intent = new Intent(scriptureBank.getContext(),InputScriptureManual.class);
		    	startActivity(intent);
			}
        	
        });
    }
	
    /**
     * Displays the scripture bank based on the scriptures either selected or inputed manually into the database.
     */
	private void displayScriptureBank() {
		Context context = this.getApplicationContext();
		scriptureQuery = wordservant_db.query(getResources().getString(R.string.scripture_table_name), new String [] {"_id", "REFERENCE"}, null, null, null, null, null);
		String [] fromColumns = {"REFERENCE"};
		int [] toViews = {R.id.list_entry};
		scriptureAdapter = new SimpleCursorAdapter(context, R.layout.list_layout, scriptureQuery, fromColumns, toViews);
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
	}
		
	public void editEntry(View v){
		return;
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scripture_bank, menu);
        return true;
    }
	
	/**
	 * When the activity is destroyed close the database and the cursor.
	 */
	protected void onDestroy(){
		super.onDestroy();
		wordservant_db.close();
		
	}
}
