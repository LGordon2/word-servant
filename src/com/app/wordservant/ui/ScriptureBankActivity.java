package com.app.wordservant.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.app.wordservant.R;

public class ScriptureBankActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_scripture_bank);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_scripture_bank, menu);
		return true;
	}

	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.activity_scripture_bank_context, menu);

	}
	public boolean onContextItemSelected(android.view.MenuItem item){
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
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			intent = new Intent(this, LandingScreen.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case R.id.inputScripture:
			// Starts a new input scripture.
			if(findViewById(R.id.fragmentHolder)==null){
				intent = new Intent(this,InputScriptureManual.class);
				startActivity(intent);
			}else{
				FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

				InputScriptureFragment inputScriptureFragment = new InputScriptureFragment();
				fragmentTransaction.add(R.id.fragmentHolder, inputScriptureFragment, "input_scripture");
				fragmentTransaction.commit();
			}

			break;
		case R.id.selectScripture:
			intent = new Intent(this,SelectScripture.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);

	}

}