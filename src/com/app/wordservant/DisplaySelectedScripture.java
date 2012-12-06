package com.app.wordservant;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DisplaySelectedScripture extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_selected_scripture);
		TextView t = (TextView) findViewById(R.id.selectedScriptureText);
		TextView referenceView = (TextView) findViewById(R.id.scriptureReference);
		final String scriptureText = this.getIntent().getStringExtra("scripture_text");
		final String reference = this.getIntent().getStringExtra("reference");
		t.setText(scriptureText);
		referenceView.setText(reference);
		
		Button addScriptureButton = (Button) findViewById(R.id.addSelectedScripture);
		addScriptureButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SQLiteDatabase db = new WordServantDbHelper(getApplicationContext(), getResources().getString(R.string.database_name), null, 1).getWritableDatabase();
				ContentValues contentValues = new ContentValues(); 
				contentValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE, reference);
				contentValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_TEXT, scriptureText);
				db.insert(WordServantContract.ScriptureEntry.TABLE_NAME, null, contentValues);
				finish();
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_display_selected_scripture,
				menu);
		return true;
	}

}
