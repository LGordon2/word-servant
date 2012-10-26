package com.example.wordservant;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;

public class EditScripture extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_scripture);
        
        EditText firstEdit = (EditText) findViewById(R.id.editText1);
        EditText secondEdit = (EditText) findViewById(R.id.editText2);
        EditText thirdEdit = (EditText) findViewById(R.id.editText3);

        SQLiteDatabase myDB = new WordServantOpenHelper(this.getApplicationContext(), "wordservant_db", null, 1).getReadableDatabase();
		String [] columns_to_retrieve = {"scripture_reference", "category", "scripture"};
		Cursor scriptureQuery = myDB.query("scripture_bank", columns_to_retrieve, null, null, null, null, null);
		scriptureQuery.moveToPosition(this.getIntent().getIntExtra("database_row", 0));
		firstEdit.setText(scriptureQuery.getString(0));
		secondEdit.setText(scriptureQuery.getString(1));
		thirdEdit.setText(scriptureQuery.getString(2));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit_scripture, menu);
        return true;
    }
}
