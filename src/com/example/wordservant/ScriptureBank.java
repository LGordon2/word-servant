package com.example.wordservant;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ScriptureBank extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.wordservant.R.layout.scripture_bank);
        displayScriptureBank();
        Button inputScripture = (Button) this.findViewById(com.example.wordservant.R.id.input_scripture);
        inputScripture.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View scriptureBank) {
				// Starts a new input scripture.
				Intent intent = new Intent(scriptureBank.getContext(),InputScriptureManual.class);
		    	startActivity(intent);
		    	finish();
			}
        	
        });
    }

	private void displayScriptureBank() {
		// TODO Auto-generated method stub
		Context context = this.getApplicationContext();
		ArrayAdapter<String> scriptureAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
		ListView scriptureList = (ListView) findViewById(com.example.wordservant.R.id.scripture_bank_list);
		scriptureList.setAdapter(scriptureAdapter);
		
		SQLiteDatabase myDB = new WordServantOpenHelper(this.getApplicationContext(), "wordservant_db", null, 1).getReadableDatabase();
		String [] columns_to_retrieve = {"scripture_reference"};
		Cursor scriptureQuery = myDB.query("scripture_bank", columns_to_retrieve, null, null, null, null, null);
		for(int i=0;i<scriptureQuery.getCount();i++){
			scriptureQuery.moveToNext();
			scriptureAdapter.add(scriptureQuery.getString(0));
		}
		myDB.close();
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.wordservant.R.menu.scripture_bank, menu);
        return true;
    }
    
    @Override
    public void onBackPressed(){
		Intent intent = new Intent(this,LandingScreen.class);
    	startActivity(intent);
    	finish();
    }
    
}
