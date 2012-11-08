package com.app.wordservant;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.app.wordservant.R;

import android.app.Activity;
import android.content.ContentValues;
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
	private Cursor scriptureQuery;
	private int currentSelectedPosition;
	private int listCount;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scripture_review);
        
        //Get the text from the database and populate the text fields with them.
        editScriptureReference = (TextView) findViewById(R.id.dueTodayScriptureReference);
        editCategory = (TextView) findViewById(R.id.dueTodayCategory);
        editScripture = (TextView) findViewById(R.id.dueTodayScripture);
        wordservant_db = new WordServantOpenHelper(this.getApplicationContext(), getResources().getString(R.string.database_name), null, 1).getReadableDatabase();
        currentSelectedPosition = this.getIntent().getIntExtra("current_position", 0);
        final Bundle bundledScriptureList = this.getIntent().getBundleExtra("bundledScriptureList");
        listCount = this.getIntent().getIntExtra("number_of_values", 0);
        displayScriptureContent(bundledScriptureList.getInt(String.valueOf(currentSelectedPosition)));
        
        
        Button nextButton = (Button) findViewById(R.id.dueTodayNextButton);
        nextButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(listCount==1) return;
				
				if(currentSelectedPosition == listCount-1){
					currentSelectedPosition = 0;
				}else{
					currentSelectedPosition += 1;
				}
				displayScriptureContent(bundledScriptureList.getInt(String.valueOf(currentSelectedPosition)));
			}
        	
        });
        
        Button reviewedButton = (Button) findViewById(R.id.dueTodayFinishedButton);
        reviewedButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// Query the db for date values.
				String [] columns_to_retrieve = {"next_review_date", "schedule", "times_reviewed", "last_reviewed_date"};
				scriptureQuery = wordservant_db.query(getResources().getString(R.string.scripture_table_name), columns_to_retrieve, "scripture_id="+bundledScriptureList.getInt(String.valueOf(currentSelectedPosition)), null, null, null, null);
				scriptureQuery.moveToFirst();
				
				// Update the database showing that the scripture was reviewed.
				ContentValues updatedValues = new ContentValues();
				SimpleDateFormat dbDateFormat = new SimpleDateFormat("MM/dd/yyyy");
				String todaysDate = dbDateFormat.format(Calendar.getInstance().getTime());
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
				updatedValues.put("last_reviewed_date", todaysDate);
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
				
				wordservant_db.update(getResources().getString(R.string.scripture_table_name), updatedValues, "scripture_id = "+bundledScriptureList.getInt(String.valueOf(currentSelectedPosition)), null);
				
				//Select the next.
				if(listCount==1) finish();
				
				if(currentSelectedPosition == listCount-1){
					currentSelectedPosition = 0;
				}else{
					currentSelectedPosition += 1;
				}
				displayScriptureContent(bundledScriptureList.getInt(String.valueOf(currentSelectedPosition)));
			}
        	
        });
    }

    protected void displayScriptureContent(int scriptureId) {
		// TODO Auto-generated method stub
        try{
			String [] columns_to_retrieve = {"reference", "tag_id", "text"};
			scriptureQuery = wordservant_db.query(getResources().getString(R.string.scripture_table_name), columns_to_retrieve, "scripture_id="+scriptureId, null, null, null, null);
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
}
