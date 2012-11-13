package com.app.wordservant;

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
import android.widget.EditText;

public class EditScripture extends Activity {

    private SQLiteDatabase wordservant_db;
	private Cursor scriptureQuery;
	private EditText editScriptureReference;
	private EditText editCategory;
	private EditText editScripture;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_scripture_manual);
        
        //Get the text from the database and populate the text fields with them.
        editScriptureReference = (EditText) findViewById(R.id.scriptureReference);
        editCategory = (EditText) findViewById(R.id.categoryName);
        editScripture = (EditText) findViewById(R.id.scriptureText);
        wordservant_db = new WordServantOpenHelper(this.getApplicationContext(), "wordservant_db", null, 1).getReadableDatabase();
        final int selectedScriptureId = this.getIntent().getIntExtra("scripture_id", 0);
        
        try{
			String [] columns_to_retrieve = {"_id","reference", "tag_id", "text"};
			scriptureQuery = wordservant_db.query(getResources().getString(R.string.scripture_table_name), columns_to_retrieve, "_id="+selectedScriptureId, null, null, null, null);
			scriptureQuery.moveToFirst();
			editScriptureReference.setText(scriptureQuery.getString(1));
			editCategory.setText(scriptureQuery.getString(2));
			editScripture.setText(scriptureQuery.getString(3));
        } catch(SQLiteException e){
        	System.err.println("Database issue..");
        	e.printStackTrace();
        }
		
		Button editDoneButton = (Button) findViewById(R.id.doneButton);
		editDoneButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				ContentValues updatedItems = new ContentValues();
				updatedItems.put("reference", editScriptureReference.getText().toString());
				//updatedItems.put("category", editCategory.getText().toString());
				updatedItems.put("text", editScripture.getText().toString());
				wordservant_db.update(getResources().getString(R.string.scripture_table_name), updatedItems, "_id="+selectedScriptureId, null);
				wordservant_db.close();
				finish();
			}
			
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit_scripture, menu);
        return true;
    }
}
