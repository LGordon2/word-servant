package com.app.wordservant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ScriptureReview extends Activity {

    private TextView editScriptureReference;
	private TextView editCategory;
	private TextView editScripture;
	private SQLiteDatabase wordservant_db;
	private Cursor unreviewedScriptureQuery;
	private int positionOnScreen;
	private Cursor scriptureQuery;
	private String todaysDate;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scripture_review);
        
        //Display the scripture information on the screen.
        editScriptureReference = (TextView) findViewById(R.id.dueTodayScriptureReference);
        editCategory = (TextView) findViewById(R.id.dueTodayCategory);
        editScripture = (TextView) findViewById(R.id.dueTodayScripture);
        
        //Get the date for today.
		SimpleDateFormat dbDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		todaysDate = dbDateFormat.format(Calendar.getInstance().getTime());
        
        //Open the database.
        wordservant_db = new WordServantOpenHelper(this.getApplicationContext(), getResources().getString(R.string.database_name), null, 1).getReadableDatabase();
        positionOnScreen = this.getIntent().getIntExtra("positionOnScreen", 0);
        final Bundle bundledScriptureList = this.getIntent().getBundleExtra("bundledScriptureList");
        displayScriptureContent(bundledScriptureList.getInt(String.valueOf(positionOnScreen)));
        
        //Check all unreviewedScriptures.
		String [] columnsToRetrieve = {"_id", "NEXT_REVIEW_DATE"};
		unreviewedScriptureQuery = wordservant_db.query("scriptures", columnsToRetrieve, "NEXT_REVIEW_DATE='"+todaysDate+"' OR _id="+bundledScriptureList.getInt(String.valueOf(positionOnScreen)), null, null, null, null);
		
		//Set the row in the unreviewed scripture query to match the selected item in Today's Memory Verses.
		unreviewedScriptureQuery.moveToFirst();
		while(unreviewedScriptureQuery.getInt(0)!=bundledScriptureList.getInt(String.valueOf(positionOnScreen))){
			unreviewedScriptureQuery.moveToNext();
			if(unreviewedScriptureQuery.isAfterLast()){
				unreviewedScriptureQuery.moveToFirst();
			}
		}
        
        Button nextButton = (Button) findViewById(R.id.dueTodayNextButton);
        nextButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				setNextScriptureId();
				displayScriptureContent(unreviewedScriptureQuery.getInt(0));
			}
        	
        });
        
        Button reviewedButton = (Button) findViewById(R.id.dueTodayFinishedButton);
        reviewedButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				String [] columnsToRetrieve = {"_id"};
				updateReviewedScripture(getApplicationContext(), scriptureQuery.getInt(3));
				unreviewedScriptureQuery = wordservant_db.query("scriptures", columnsToRetrieve, "NEXT_REVIEW_DATE='"+todaysDate+"'", null, null, null, null);
				
				//Select the next.
				setNextScriptureId();
				if(unreviewedScriptureQuery.getCount()==0){
					finish();
					return;
				}
				displayScriptureContent(unreviewedScriptureQuery.getInt(0));
			}
        	
        });
    }
    protected void displayScriptureContent(int scriptureId) {
		// TODO Auto-generated method stub
        try{
			String [] columns_to_retrieve = {"reference", "tag_id", "text", "_id"};
			scriptureQuery = wordservant_db.query(getResources().getString(R.string.scripture_table_name), columns_to_retrieve, "_id="+scriptureId, null, null, null, null);
			scriptureQuery.moveToFirst();
			editScriptureReference.setText(scriptureQuery.getString(0));
			editCategory.setText(scriptureQuery.getString(1));
			editScripture.setText(scriptureQuery.getString(2));
        } catch(SQLiteException e){
        	System.err.println("Database issue..");
        	e.printStackTrace();
        }
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_due_today, menu);
        return true;
    }
	private void setNextScriptureId(){
		//If there are no or just one we just need to display a message and return that scripture id.
		if(unreviewedScriptureQuery.getCount()==0){
			Toast.makeText(getApplicationContext(), "There are no unchecked scriptures.", Toast.LENGTH_SHORT).show();
		}
		else if(unreviewedScriptureQuery.getCount()==1 && unreviewedScriptureQuery.getInt(0)==scriptureQuery.getInt(3)){
			Toast.makeText(getApplicationContext(), "This is the last unchecked scripture.", Toast.LENGTH_SHORT).show();
		}
		else{
			unreviewedScriptureQuery.moveToNext();
			if(unreviewedScriptureQuery.isAfterLast()){
				unreviewedScriptureQuery.moveToFirst();
			}
		}
		return;
	}
	public static void updateReviewedScripture(Context context, int scriptureId){
		SQLiteDatabase wordservant_db = new WordServantOpenHelper(context, "wordservant_db", null, 1).getReadableDatabase();
		String [] columns_to_retrieve = {"next_review_date", "schedule", "times_reviewed", "last_reviewed_date"};
		Cursor scriptureQuery = wordservant_db.query("scriptures", columns_to_retrieve, "_id="+scriptureId, null, null, null, null);
		scriptureQuery.moveToFirst();
		
		// Update the database showing that the scripture was reviewed.
		ContentValues updatedValues = new ContentValues();
		SimpleDateFormat dbDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String todaysDate = dbDateFormat.format(Calendar.getInstance().getTime());
		
		//Update the times reviewed.
		try {
			if(scriptureQuery.getString(3) == null){
				updatedValues.put("times_reviewed", 1);
			}else if(Calendar.getInstance().getTime().before(dbDateFormat.parse(scriptureQuery.getString(3)))){
				updatedValues.put("times_reviewed", scriptureQuery.getInt(2)+1);
				if(scriptureQuery.getInt(2) == 7){
					updatedValues.put("schedule", "weekly");
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Update the last reviewed date to be today.
		updatedValues.put("last_reviewed_date", todaysDate);
		
		//Calculate when the next review should be and update that date.
		Calendar calculatedDate = Calendar.getInstance();
		if(scriptureQuery.getString(1).equals("daily")){
			calculatedDate.add(Calendar.DATE, 1);
		}else if(scriptureQuery.getString(1).equals("weekly")){
			calculatedDate.add(Calendar.DATE, 7);
		}else if(scriptureQuery.getString(1).equals("monthly")){
			calculatedDate.add(Calendar.MONTH, 1);
		}else if(scriptureQuery.getString(1).equals("yearly")){
			calculatedDate.add(Calendar.YEAR, 1);
		}
		
		updatedValues.put("next_review_date", dbDateFormat.format(calculatedDate.getTime()));
		
		wordservant_db.update("scriptures", updatedValues, "_id = "+scriptureId, null);
	
	}
}
