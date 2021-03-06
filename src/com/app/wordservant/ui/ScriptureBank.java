package com.app.wordservant.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.wordservant.R;
import com.app.wordservant.provider.WordServantContract;

public class ScriptureBank extends Fragment{

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

	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.scripture_bank, null);
	}
	
	
}
