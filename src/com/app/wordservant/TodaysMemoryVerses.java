package com.app.wordservant;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

public class TodaysMemoryVerses extends Activity{

    private SQLiteDatabase myDB;
	private ArrayAdapter<CheckBox> scriptureAdapter;
	private Cursor scriptureQuery;
	private SparseIntArray allScriptures;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_memory_verses);
        allScriptures = new SparseIntArray();
        myDB = new WordServantOpenHelper(this.getApplicationContext(), getResources().getString(R.string.database_name), null, 1).getReadableDatabase();
    }
	
    protected void onStart(){
    	super.onStart();
    	
    	String [] queryColumns = {"REFERENCE","SCRIPTURE_ID", "NEXT_REVIEW_DATE"}; 
		SimpleDateFormat dbDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String todaysDate = dbDateFormat.format(new Date());
		String builtQuery = SQLiteQueryBuilder.buildQueryString(false, getResources().getString(R.string.scripture_table_name), queryColumns, "NEXT_REVIEW_DATE = '"+todaysDate+"' or LAST_REVIEWED_DATE = '"+todaysDate+"'", null, null, null, null);
    	displayScriptureList((ListView) this.findViewById(R.id.dueToday), builtQuery);
    }
	
	/**
	 * @author Lewis Gordon
	 * Queries the database for any memory verses that are due when the screen is accessed.
	 */
	private void displayScriptureList(ListView view, String query) {
		// Set up the adapter that is going to be displayed in the list view.
		Context context = this.getApplicationContext();
		scriptureAdapter = new ArrayAdapter<CheckBox>(context, android.R.layout.simple_list_item_1){
			public View getView (int position, View convertView, ViewGroup parent){
				return (View) this.getItem(position);
			}
		};
		final Bundle bundledScriptureList = new Bundle();
		final ListView scriptureList = view;//(ListView) findViewById(com.example.wordservant.R.id.dueToday);
		scriptureList.setAdapter(scriptureAdapter);
		
		//Queries the database for any verses that have the same review date as the date when the screen was accessed.
		try{
			scriptureQuery = myDB.rawQuery(query, null);
			for(int positionOnScreen=0;positionOnScreen<scriptureQuery.getCount();positionOnScreen++){
				scriptureQuery.moveToNext();
				final int position = positionOnScreen;
				bundledScriptureList.putInt(String.valueOf(positionOnScreen), scriptureQuery.getInt(1));
				final CheckBox newCheckBox = new CheckBox(context);
				if(Date.parse(scriptureQuery.getString(2))>Calendar.getInstance().getTimeInMillis()){
					newCheckBox.setChecked(true);
				}
				newCheckBox.setText(scriptureQuery.getString(0));
				newCheckBox.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						newCheckBox.setChecked(false);
						scriptureList.performItemClick(scriptureList, position, newCheckBox.getId());
					}
					
				});
				scriptureAdapter.add(newCheckBox);
				
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
