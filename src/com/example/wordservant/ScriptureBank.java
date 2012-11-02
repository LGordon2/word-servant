package com.example.wordservant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ScriptureBank extends Activity {
	protected SQLiteDatabase myDB;
	protected ArrayAdapter<String> scriptureAdapter;
	protected Cursor scriptureQuery;
	protected SparseIntArray allScriptures;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scripture_bank);
        allScriptures = new SparseIntArray();
        myDB = new WordServantOpenHelper(this.getApplicationContext(), getResources().getString(R.string.database_name), null, 1).getReadableDatabase();
        
    }
    
    protected void onStart(){
    	super.onStart();
    	allScriptures.clear();
    	displayScriptureBank();
    	
        Button inputScripture = (Button) this.findViewById(com.example.wordservant.R.id.input_scripture);
        inputScripture.setOnClickListener(new OnClickListener(){

			public void onClick(View scriptureBank) {
				// Starts a new input scripture.
				Intent intent = new Intent(scriptureBank.getContext(),InputScriptureManual.class);
		    	startActivity(intent);
			}
        	
        });
    }
	
	private void displayScriptureBank() {
		// TODO Auto-generated method stub
		Context context = this.getApplicationContext();
		scriptureAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
		ListView scriptureList = (ListView) findViewById(R.id.scripture_bank_list);
		scriptureList.setAdapter(scriptureAdapter);
		
		scriptureList.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View landingScreenView, int position,long id) {
				// TODO Auto-generated method stub
				// Bundle data
				
				Intent intent = new Intent(landingScreenView.getContext(),EditScripture.class);
				intent.putExtra("scripture_id",allScriptures.get(position));
		    	startActivity(intent);
				
			}
		});
		
		
		String [] columns_to_retrieve = {"reference","scripture_id"};
		scriptureQuery = myDB.query(getResources().getString(R.string.scripture_table_name), columns_to_retrieve, null, null, null, null, null);
		
		for(int positionOnScreen=0;positionOnScreen<scriptureQuery.getCount();positionOnScreen++){
			scriptureQuery.moveToNext();
			scriptureAdapter.add(scriptureQuery.getString(0));
			allScriptures.put(positionOnScreen, scriptureQuery.getInt(1));
		}
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.wordservant.R.menu.scripture_bank, menu);
        return true;
    }
	
	protected void onDestroy(){
		super.onDestroy();
		myDB.close();
		scriptureQuery.close();
	}
}
