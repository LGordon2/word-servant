package com.example.wordservant;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class TodaysMemoryVerses extends Activity {

    private SQLiteDatabase myDB;
	private ArrayAdapter<String> scriptureAdapter;
	private Cursor scriptureQuery;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_memory_verses);
        
        myDB = new WordServantOpenHelper(this.getApplicationContext(), "wordservant_db", null, 1).getReadableDatabase();
        
        displayScriptureBank();
    }
	
	private void displayScriptureBank() {
		// TODO Auto-generated method stub
		Context context = this.getApplicationContext();
		scriptureAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
		ListView scriptureList = (ListView) findViewById(com.example.wordservant.R.id.dueToday);
		scriptureList.setAdapter(scriptureAdapter);
		
		scriptureList.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View dueTodayView, int position,long id) {
				// TODO Auto-generated method stub
				// Bundle data
				
				Intent intent = new Intent(dueTodayView.getContext(),EditScripture.class);
				intent.putExtra("database_row",position);
		    	startActivity(intent);
				
			}


			
		});
		
		
		scriptureQuery = myDB.rawQuery("SELECT scripture_reference FROM scripture_bank s "+
		"JOIN due_today d on (s.scripture_id = d.scripture_id) " +
		"WHERE d.REVIEW_DATE <= date('now');", null);
		for(int i=0;i<scriptureQuery.getCount();i++){
			scriptureQuery.moveToNext();
			scriptureAdapter.add(scriptureQuery.getString(0));
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_todays_memory_verses, menu);
        return true;
    }
}
