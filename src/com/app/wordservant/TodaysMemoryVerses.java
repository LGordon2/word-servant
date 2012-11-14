package com.app.wordservant;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TodaysMemoryVerses extends Activity{

    private SQLiteDatabase wordservant_db;
	private ArrayAdapter<LinearLayout> scriptureAdapter;
	private Cursor scriptureQuery;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_memory_verses);
        wordservant_db = new WordServantOpenHelper(this.getApplicationContext(), getResources().getString(R.string.database_name), null, 1).getReadableDatabase();
        
    }
	
    protected void onStart(){
    	super.onStart();
    	
    	String [] queryColumns = {"REFERENCE","_ID", "NEXT_REVIEW_DATE", "LAST_REVIEWED_DATE"}; 
		SimpleDateFormat dbDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String todaysDate = dbDateFormat.format(new Date());
		TextView currentDateDisplayer = (TextView) findViewById(R.id.currentDate);
        currentDateDisplayer.setText(DateFormat.format("EEEE, MMMM dd, yyyy", Calendar.getInstance()));
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
		scriptureAdapter = new ArrayAdapter<LinearLayout>(context, android.R.layout.simple_list_item_1){
			public View getView (int position, View convertView, ViewGroup parent){
				return (View) this.getItem(position);
			}
		};
		final Bundle bundledScriptureList = new Bundle();
		final ListView scriptureList = view;
		scriptureList.setAdapter(scriptureAdapter);
		//Queries the database for any verses that have the same review date as the date when the screen was accessed.
		try{
			scriptureQuery = wordservant_db.rawQuery(query, null);
			for(int positionOnScreen=0;positionOnScreen<scriptureQuery.getCount();positionOnScreen++){
				scriptureQuery.moveToNext();
				
				//Get the layout from the XML file for checkbox lists.
				LinearLayout newLayout = (LinearLayout) this.getLayoutInflater().inflate(R.layout.checkbox_list_layout, null);
				newLayout.setOrientation(LinearLayout.HORIZONTAL);
				TextView newLabel = (TextView) newLayout.getChildAt(1);
				newLabel.setText(scriptureQuery.getString(0));
				final int position = positionOnScreen;
				bundledScriptureList.putInt(String.valueOf(positionOnScreen), scriptureQuery.getInt(1));
				
				//Display a checkbox.
				CheckBox newCheckBox = (CheckBox) newLayout.getChildAt(0);
				if(Date.parse(scriptureQuery.getString(2))>Calendar.getInstance().getTimeInMillis()){
					newCheckBox.setChecked(true);
				}
				
				newCheckBox.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						// Reset the NEXT_REVIEW_DATE if we are unchecking, otherwise mark the scripture as reviewed.
						if(!((CheckBox) v).isChecked()){
							ContentValues newValues = new ContentValues();
							SimpleDateFormat dbDateFormat = new SimpleDateFormat("MM/dd/yyyy");
							String todaysDate = dbDateFormat.format(Calendar.getInstance().getTime());
							newValues.put("NEXT_REVIEW_DATE", todaysDate);
							wordservant_db.update("scriptures", newValues, "_id="+bundledScriptureList.getInt(String.valueOf(position)), null);
						}else{
							ScriptureReview.updateReviewedScripture(getApplicationContext(), bundledScriptureList.getInt(String.valueOf(position)));
						}
					}
					
				});
				scriptureAdapter.add(newLayout);
			}
		} catch(SQLiteException e){
			System.err.println("Database issue...");
			e.printStackTrace();
		}
		
		// When any of the entries are pressed they can then be edited.
		scriptureList.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View dueTodayView, int position,long id) {
				// Sends the query row that holds the values we would like to edit.
				LinearLayout view = (LinearLayout) dueTodayView;
				CheckBox checkBox = (CheckBox) view.getChildAt(0);
				if(!checkBox.isPressed()){
					Intent intent = new Intent(dueTodayView.getContext(),ScriptureReview.class);
					intent.putExtra("bundledScriptureList",bundledScriptureList);
					intent.putExtra("positionOnScreen", position);
			    	startActivity(intent);
				}
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
		wordservant_db.close();
		scriptureQuery.close();
	}
}
