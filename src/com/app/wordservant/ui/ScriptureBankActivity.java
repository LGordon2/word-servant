package com.app.wordservant.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.app.wordservant.R;

public class ScriptureBankActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_scripture_bank);

	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater()
		.inflate(R.menu.activity_scripture_bank, menu);
		return true;
	}*/

	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.activity_scripture_bank, menu);

	}
	public boolean onContextItemSelected(MenuItem item){
		switch(item.getItemId()){
			case R.id.deleteScriptureMenuItem:
				DialogFragment dialog = new DeleteScriptureDialogFragment();
				AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
				Bundle bundle = new Bundle();
				bundle.putString("_id", String.valueOf(menuInfo.id));
				dialog.setArguments(bundle);
				dialog.show(getSupportFragmentManager(), "DeleteScriptureDialogFragment");

				return true;
			default:
				return false;
		}
	}
	/*@Override
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
	}*/

}
