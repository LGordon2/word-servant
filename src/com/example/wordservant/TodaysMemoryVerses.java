package com.example.wordservant;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TodaysMemoryVerses extends Activity{

    private SQLiteDatabase myDB;
	private ArrayAdapter<String> scriptureAdapter;
	private Cursor scriptureQuery;
	private SparseIntArray allScriptures;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_memory_verses);
        allScriptures = new SparseIntArray();
        myDB = new WordServantOpenHelper(this.getApplicationContext(), "wordservant_db", null, 1).getReadableDatabase();
    }
	
    protected void onStart(){
    	super.onStart();
    	
    	DateFormat shortFormat = DateFormat.getDateInstance(DateFormat.SHORT);
    	String [] queryColumns = {"SCRIPTURE_REFERENCE","SCRIPTURE_ID"}; 
		String builtQuery = SQLiteQueryBuilder.buildQueryString(false, "scripture_bank", queryColumns, "REVIEW_DATE = '"+shortFormat.format(new Date())+"'", null, null, null, null);
    	displayScriptureList((ListView) this.findViewById(R.id.dueToday), builtQuery);
    }
	
	/**
	 * @author Lewis Gordon
	 * Queries the database for any memory verses that are due when the screen is accessed.
	 */
	private void displayScriptureList(ListView view, String query) {
		// Set up the adapter that is going to be displayed in the list view.
		Context context = this.getApplicationContext();
		scriptureAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
		final Bundle bundledScriptureList = new Bundle();
		ListView scriptureList = view;//(ListView) findViewById(com.example.wordservant.R.id.dueToday);
		scriptureList.setAdapter(scriptureAdapter);
		
		//Queries the database for any verses that have the same review date as the date when the screen was accessed.
		try{
			scriptureQuery = myDB.rawQuery(query, null);
			for(int positionOnScreen=0;positionOnScreen<scriptureQuery.getCount();positionOnScreen++){
				scriptureQuery.moveToNext();
				scriptureAdapter.add(scriptureQuery.getString(0));
				bundledScriptureList.putInt(String.valueOf(positionOnScreen), scriptureQuery.getInt(1));
			}
		} catch(SQLiteException e){
			System.err.println("Database issue...");
			e.printStackTrace();
		}
		
		// When any of the entries are pressed they can then be edited.
		scriptureList.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View dueTodayView, int position,long id) {
				// Sends the query row that holds the values we would like to edit.
				
				Intent intent = new Intent(dueTodayView.getContext(),ScriptureReview.class);
				intent.putExtra("bundledScriptureList",bundledScriptureList);
				intent.putExtra("current_position", position);
				intent.putExtra("number_of_values", scriptureQuery.getCount());
		    	startActivity(intent);
				
			}

			
		});
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_todays_memory_verses, menu);
        return true;
    }
    
	protected void onDestroy(){
		super.onDestroy();
		myDB.close();
		scriptureQuery.close();
	}
}
