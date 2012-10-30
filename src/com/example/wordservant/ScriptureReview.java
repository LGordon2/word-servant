package com.example.wordservant;

import android.app.Activity;
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
	private SQLiteDatabase myDB;
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
        myDB = new WordServantOpenHelper(this.getApplicationContext(), "wordservant_db", null, 1).getReadableDatabase();
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
    }

    protected void displayScriptureContent(int scriptureId) {
		// TODO Auto-generated method stub
        try{
			String [] columns_to_retrieve = {"scripture_reference", "category", "scripture_text"};
			scriptureQuery = myDB.query("scripture_bank", columns_to_retrieve, "scripture_id="+scriptureId, null, null, null, null);
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
