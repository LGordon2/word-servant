package com.app.wordservant;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ScriptureBank extends FragmentActivity implements DeleteScriptureDialogFragment.DeleteScriptureDialogFragmentListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scripture_bank_fragment);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater()
				.inflate(R.menu.activity_scripture_bank_fragment, menu);
		return true;
	}

	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.activity_scripture_bank_fragment, menu);
			
	}
	public boolean onContextItemSelected(MenuItem item){
		switch(item.getItemId()){
			case R.id.deleteScriptureMenuItem:
				DialogFragment dialog = new DeleteScriptureDialogFragment();
				AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
				ListView listView = (ListView) menuInfo.targetView.getParent();
				Cursor cursor = ((SimpleCursorAdapter) listView.getAdapter()).getCursor();
				cursor.moveToPosition(menuInfo.position);
				Bundle bundle = new Bundle();
				bundle.putString("_id", cursor.getString(0));
				dialog.setArguments(bundle);
				dialog.show(getSupportFragmentManager(), "DeleteScriptureDialogFragment");
				
				return true;
			default:
				return false;
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		getSupportFragmentManager().findFragmentById(R.id.scriptureBankFragment).onStart();
	}


}
