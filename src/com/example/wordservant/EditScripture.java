package com.example.wordservant;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EditScripture extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_scripture);
        
        //Get the text from the database and populate the text fields with them.
        final EditText editScriptureReference = (EditText) findViewById(R.id.editScriptureReference);
        final EditText editCategory = (EditText) findViewById(R.id.editCategory);
        final EditText editScripture = (EditText) findViewById(R.id.editScripture);
        final SQLiteDatabase myDB = new WordServantOpenHelper(this.getApplicationContext(), "wordservant_db", null, 1).getReadableDatabase();
		String [] columns_to_retrieve = {"_id","scripture_reference", "category", "scripture"};
		final Cursor scriptureQuery = myDB.query("scripture_bank", columns_to_retrieve, null, null, null, null, null);
		scriptureQuery.moveToPosition(this.getIntent().getIntExtra("database_row", 0));
		editScriptureReference.setText(scriptureQuery.getString(1));
		editCategory.setText(scriptureQuery.getString(2));
		editScripture.setText(scriptureQuery.getString(3));
		
		Button editDoneButton = (Button) findViewById(R.id.editDoneButton);
		editDoneButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				ContentValues updatedItems = new ContentValues();
				updatedItems.put("scripture_reference", editScriptureReference.getText().toString());
				updatedItems.put("category", editCategory.getText().toString());
				updatedItems.put("scripture", editScripture.getText().toString());
				myDB.update("scripture_bank", updatedItems, "_id="+scriptureQuery.getInt(0), null);
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
