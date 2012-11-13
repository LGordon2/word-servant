package com.app.wordservant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

public class ScriptureReview extends Activity {

    private TextView editScriptureReference;
	private TextView editCategory;
	private TextView editScripture;
	private SQLiteDatabase wordservant_db;
	private Cursor unreviewedScriptureQuery;
	private int selectedScriptureId;
	private Cursor scriptureQuery;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scripture_review);
        
        //Display the scripture information on the screen.
        editScriptureReference = (TextView) findViewById(R.id.dueTodayScriptureReference);
        editCategory = (TextView) findViewById(R.id.dueTodayCategory);
        editScripture = (TextView) findViewById(R.id.dueTodayScripture);
        
        //Open the database.
        wordservant_db = new WordServantOpenHelper(this.getApplicationContext(), getResources().getString(R.string.database_name), null, 1).getReadableDatabase();
        selectedScriptureId = this.getIntent().getIntExtra("scriptureId", 0);
        final Bundle bundledScriptureList = this.getIntent().getBundleExtra("bundledScriptureList");
        displayScriptureContent(bundledScriptureList.getInt(String.valueOf(selectedScriptureId)));
        
        
        Button nextButton = (Button) findViewById(R.id.dueTodayNextButton);
        nextButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				SimpleDateFormat dbDateFormat = new SimpleDateFormat("MM/dd/yyyy");
				String todaysDate = dbDateFormat.format(Calendar.getInstance().getTime());
				String [] columnsToRetrieve = {"_id"};
				unreviewedScriptureQuery = wordservant_db.query("scriptures", columnsToRetrieve, "NEXT_REVIEW_DATE='"+todaysDate+"'", null, null, null, null);
				unreviewedScriptureQuery.moveToFirst();
				if(unreviewedScriptureQuery.getCount()==0){
					finish();
					return;
				}
				displayScriptureContent(unreviewedScriptureQuery.getInt(0));
			}
        	
        });
        
        Button reviewedButton = (Button) findViewById(R.id.dueTodayFinishedButton);
        reviewedButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				SimpleDateFormat dbDateFormat = new SimpleDateFormat("MM/dd/yyyy");
				String todaysDate = dbDateFormat.format(Calendar.getInstance().getTime());

				updateReviewedScripture(getApplicationContext(), scriptureQuery.getInt(3));
				//Select the next.
				String [] columnsToRetrieve = {"_id"};
				unreviewedScriptureQuery = wordservant_db.query("scriptures", columnsToRetrieve, "NEXT_REVIEW_DATE='"+todaysDate+"'", null, null, null, null);
				unreviewedScriptureQuery.moveToFirst();
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
