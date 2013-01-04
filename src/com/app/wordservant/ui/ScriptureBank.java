package com.app.wordservant.ui;

import com.app.wordservant.R;
import com.app.wordservant.R.id;
import com.app.wordservant.R.layout;
import com.app.wordservant.R.menu;
import com.app.wordservant.provider.WordServantContract;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ScriptureBank extends FragmentActivity{

	public static class ScriptureBankFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
		SimpleCursorAdapter mAdapter;

		public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
			return inflater.inflate(R.layout.fragment_list_layout, null);
		}
		@SuppressWarnings("deprecation")
		public void onActivityCreated(Bundle savedInstanceState){
			super.onActivityCreated(savedInstanceState);
			String [] fromColumns = {WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE};

			mAdapter = new SimpleCursorAdapter(getActivity(), 
					R.layout.list_layout, null, 
					fromColumns, 
					new int []{R.id.list_entry});
			setListAdapter(mAdapter);
			this.setListShown(false);
			getLoaderManager().initLoader(0, null, this);
			registerForContextMenu(this.getListView());
		}
		public void setListShown(boolean shown){
			ListView listView = (ListView) this.getView().findViewById(android.R.id.list);
			((ProgressBar) this.getView().findViewById(R.id.loadingCircle)).setVisibility(shown?ProgressBar.GONE:ProgressBar.VISIBLE);
			((TextView) this.getView().findViewById(android.R.id.empty)).setVisibility(shown && listView.getCount()==0?TextView.VISIBLE:TextView.GONE);
			listView.setVisibility(shown && listView.getCount()>0?ListView.VISIBLE:ListView.GONE);
		}
		public void onListItemClick(ListView l, View v, int position, long id){
			Intent intent = new Intent(getActivity(),EditScripture.class);
			intent.putExtra("scripture_id",id);
	    	startActivity(intent);
		}
		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			// TODO Auto-generated method stub
			String [] columns_to_retrieve = {WordServantContract.ScriptureEntry._ID, WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE};
			return new CursorLoader(getActivity(), 
					WordServantContract.ScriptureEntry.CONTENT_URI, 
					columns_to_retrieve, 
					null, null, null);
		}
		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			// TODO Auto-generated method stub
			mAdapter.swapCursor(data);
			// The list should now be shown.
			setListShown(true);
		}
		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			// TODO Auto-generated method stub
			mAdapter.swapCursor(null);

		}

	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.scripture_bank);
		//Functionality for the input scripture button.
		ImageButton inputScripture = (ImageButton) this.findViewById(R.id.inputScripture);
        inputScripture.setOnClickListener(new OnClickListener(){

			public void onClick(View scriptureBank) {
				// Starts a new input scripture.
				if(findViewById(R.id.fragmentHolder)==null){
					Intent intent = new Intent(scriptureBank.getContext(),InputScriptureManual.class);
			    	startActivity(intent);
				}else{
					FragmentManager fragmentManager = getSupportFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

					InputScriptureFragment inputScriptureFragment = new InputScriptureFragment();
					fragmentTransaction.add(R.id.fragmentHolder, inputScriptureFragment, "input_scripture");
					fragmentTransaction.commit();
				}

			}

        });

        //Functionality for the selected scripture button.
        ImageButton selectScripture = (ImageButton) this.findViewById(R.id.selectScripture);
        selectScripture.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ScriptureBank.this,SelectScripture.class);
				startActivity(intent);
			}

        });
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

}
