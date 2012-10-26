package com.example.wordservant;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ScriptureBank extends Activity {
	SQLiteDatabase myDB;
	private ArrayAdapter<String> scriptureAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.wordservant.R.layout.scripture_bank);
        
        myDB = new WordServantOpenHelper(this.getApplicationContext(), "wordservant_db", null, 1).getReadableDatabase();
        
        
        displayScriptureBank();
        Button inputScripture = (Button) this.findViewById(com.example.wordservant.R.id.input_scripture);
        inputScripture.setOnClickListener(new OnClickListener(){

			public void onClick(View scriptureBank) {
				// Starts a new input scripture.
				Intent intent = new Intent(scriptureBank.getContext(),InputScriptureManual.class);
		    	startActivity(intent);

				myDB.close();
			}
        	
        });
    }

	private void displayScriptureBank() {
		// TODO Auto-generated method stub
		Context context = this.getApplicationContext();
		scriptureAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
		ListView scriptureList = (ListView) findViewById(com.example.wordservant.R.id.scripture_bank_list);
		scriptureList.setAdapter(scriptureAdapter);
		
		scriptureList.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View landingScreenView, int position,long id) {
				// TODO Auto-generated method stub
				// Bundle data
				
				Intent intent = new Intent(landingScreenView.getContext(),EditScripture.class);
				intent.putExtra("database_row",position);
		    	startActivity(intent);
				
			}


			
		});
		
		
		String [] columns_to_retrieve = {"scripture_reference"};
		Cursor scriptureQuery = myDB.query("scripture_bank", columns_to_retrieve, null, null, null, null, null);
		for(int i=0;i<scriptureQuery.getCount();i++){
			scriptureQuery.moveToNext();
			scriptureAdapter.add(scriptureQuery.getString(0));
		}
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.wordservant.R.menu.scripture_bank, menu);
        return true;
    }
	
	protected void onResume(){
		super.onResume();
		scriptureAdapter.clear();
		String [] columns_to_retrieve = {"scripture_reference"};
		Cursor scriptureQuery = myDB.query("scripture_bank", columns_to_retrieve, null, null, null, null, null);
		for(int i=0;i<scriptureQuery.getCount();i++){
			scriptureQuery.moveToNext();
			scriptureAdapter.add(scriptureQuery.getString(0));
		}
	}
	
	protected void onDestroy(){
		super.onDestroy();
		myDB.close();
	}
}
