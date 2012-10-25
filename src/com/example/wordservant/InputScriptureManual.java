package com.example.wordservant;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputScriptureManual extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_scripture_manual);
        
        Button doneButton = (Button) this.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new OnClickListener(){

			@Override
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
				ContentValues scriptureValues = new ContentValues();
				scriptureValues.put("scripture_reference", scriptureReference.getText().toString());
				scriptureValues.put("category", categoryName.getText().toString());
				scriptureValues.put("scripture", scriptureText.getText().toString());
				
				//Open the database and add the row.
				SQLiteDatabase myDB = new WordServantOpenHelper(inputScriptureView.getContext(), "wordservant_db", null, 1).getWritableDatabase();
				myDB.insert("scripture_bank", null, scriptureValues);
				myDB.close();
				
				//Go back to the scripture bank screen.
				Intent intent = new Intent(inputScriptureView.getContext(),ScriptureBank.class);
		    	startActivity(intent);
		    	finish();
			}
        	
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_input_scripture_manual, menu);
        return true;
    }

    @Override
    public void onBackPressed(){
		Intent intent = new Intent(this,ScriptureBank.class);
    	startActivity(intent);
    	finish();
    }
}
