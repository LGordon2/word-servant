package com.app.wordservant.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.app.wordservant.R;
import com.app.wordservant.provider.WordServantContract;

public class StatisticsActivity extends SherlockFragmentActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_statistics, menu);
		return true;
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

	public static class StatisticsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

		public void onAttach(Activity a){
			super.onAttach(a);
			getLoaderManager().initLoader(0, null, this);
		}

		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
			return inflater.inflate(R.layout.statistics_fragment, null);
		}

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			String [] projection = {
					WordServantContract.ScriptureEntry._ID,
					WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE,
					WordServantContract.ScriptureEntry.COLUMN_NAME_CORRECTLY_REVIEWED_COUNT,
					WordServantContract.ScriptureEntry.COLUMN_NAME_INCORRECTLY_REVIEWED_COUNT,
					WordServantContract.ScriptureEntry.COLUMN_NAME_SKIPPED_REVIEW_COUNT
			};
			return new CursorLoader(getActivity(), WordServantContract.ScriptureEntry.CONTENT_URI, projection, null, null, null);
		}

		@SuppressWarnings("deprecation")
		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			// Set up references adapter
			((ListView) getView().findViewById(R.id.reference)).setAdapter(
					new SimpleCursorAdapter(getActivity(),
							R.layout.small_list_layout,
							data,
							new String []{WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE},
							new int []{R.id.list_entry}));

			//Set up the other adapters
			ArrayAdapter<String> correctReviewedAdapter = new ArrayAdapter<String>(getActivity(),R.layout.small_list_layout,R.id.list_entry,new ArrayList<String>());
			ArrayAdapter<String> incorrectReviewedAdapter = new ArrayAdapter<String>(getActivity(),R.layout.small_list_layout,R.id.list_entry,new ArrayList<String>());
			ArrayAdapter<String> missedReviewedAdapter = new ArrayAdapter<String>(getActivity(),R.layout.small_list_layout,R.id.list_entry,new ArrayList<String>());

			((ListView) getView().findViewById(R.id.reviewedCount)).setAdapter(correctReviewedAdapter);
			((ListView) getView().findViewById(R.id.missedCount)).setAdapter(incorrectReviewedAdapter);
			((ListView) getView().findViewById(R.id.skippedCount)).setAdapter(missedReviewedAdapter);

			for(int i=0;i<data.getCount();i++){
				data.moveToPosition(i);
				double totalReviewedCount = data.getInt(2)+data.getInt(3)+data.getInt(4);
				DecimalFormat formatter = new DecimalFormat("#%");
				String [] percents = {
						totalReviewedCount!=0f?formatter.format(((double) data.getInt(2)/totalReviewedCount)):"0%",
						totalReviewedCount!=0f?formatter.format(((double) data.getInt(3)/totalReviewedCount)):"0%",
						totalReviewedCount!=0f?formatter.format(((double) data.getInt(4)/totalReviewedCount)):"0%"
				};

				correctReviewedAdapter.add(percents[0]);
				incorrectReviewedAdapter.add(percents[1]);
				missedReviewedAdapter.add(percents[2]);
			}
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			//Nothing to do here...
		}
	}
}
