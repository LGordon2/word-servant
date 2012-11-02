package com.example.wordservant;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputScriptureManual extends Activity {

    private SQLiteDatabase myDB;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_scripture_manual);
        
        Button doneButton = (Button) this.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new OnClickListener(){

			
			/**
			 * Provides the functionality for the done button.
			 */
			public void onClick(View inputScriptureView) {
				// Get the referenced fields.
				EditText scriptureReference = (EditText) findViewById(R.id.scriptureReference);
				EditText categoryName = (EditText) findViewById(R.id.categoryName);
				EditText scriptureText = (EditText) findViewById(R.id.scriptureText);
				
				// Check for required fields.
				if (scriptureReference.getText().toString().equals("")){
					Toast.makeText(inputScriptureView.getContext(),"Scripture Reference is a required field.", Toast.LENGTH_SHORT).show();
					return;
				}
				if (scriptureText.getText().toString().equals("")){
					Toast.makeText(inputScriptureView.getContext(),"Scripture Text is a required field.", Toast.LENGTH_SHORT).show();
					return;
				}
				
				// Map the values of the fields to columns that are used in the database.
				Calendar currentCalendar = Calendar.getInstance();
				String todaysDate = currentCalendar.get(Calendar.MONTH)+1 + "/" + currentCalendar.get(Calendar.DATE) + "/" + currentCalendar.get(Calendar.YEAR);
				ContentValues scriptureValues = new ContentValues();
				scriptureValues.put("reference", scriptureReference.getText().toString());
				scriptureValues.put("text", scriptureText.getText().toString());
				scriptureValues.put("created_date", todaysDate);
				scriptureValues.put("schedule", "daily");
				scriptureValues.put("times_reviewed", 0);
				scriptureValues.put("next_review_date", todaysDate);
				
				
				//Open the database and add the row.
				try{
					myDB = new WordServantOpenHelper(inputScriptureView.getContext(), getResources().getString(R.string.database_name), null, 1).getWritableDatabase();
					myDB.insert(getResources().getString(R.string.scripture_table_name), null, scriptureValues);
				} catch(SQLiteException e){
					System.err.println("Error with SQL statement.");
					e.printStackTrace();
				} finally{
					myDB.close();
				}
				
				
				//Go back to the scripture bank screen.
				finish();
			}
        	
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_input_scripture_manual, menu);
        return true;
    }
}
