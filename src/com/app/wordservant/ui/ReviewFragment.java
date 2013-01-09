package com.app.wordservant.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.app.wordservant.R;
import com.app.wordservant.provider.WordServantContract;

public class ReviewFragment extends SherlockFragment{
	private TextView mEditScriptureReference;
	private TextView mEditCategory;
	private TextView mEditScripture;

	void displayScriptureContent(int scriptureId) {
		try{		
			String [] columns_to_retrieve = {
					WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE,
					WordServantContract.ScriptureEntry.COLUMN_NAME_TEXT,
					WordServantContract.ScriptureEntry._ID,
					WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE};
			Cursor scriptureQuery = getActivity().getContentResolver().query(
					Uri.withAppendedPath(WordServantContract.ScriptureEntry.CONTENT_ID_URI_BASE,String.valueOf(scriptureId)), 
					columns_to_retrieve, 
					null, null, null);
			scriptureQuery.moveToFirst();
			mEditScriptureReference.setText(scriptureQuery.getString(0));

			//Set the tag text.
			/*String tagText = "";
			Cursor tagTextQuery = mDatabaseConnection.rawQuery("select "+WordServantContract.TagEntry.COLUMN_NAME_TAG_NAME+
					" from "+WordServantContract.CategoryEntry.TABLE_NAME+" c join "+
					WordServantContract.ScriptureEntry.TABLE_NAME+" s on "+
					"(c."+WordServantContract.CategoryEntry.COLUMN_NAME_SCRIPTURE_ID+"=s."+
					WordServantContract.ScriptureEntry._ID+") join "+
					WordServantContract.TagEntry.TABLE_NAME+" t on (c."+WordServantContract.CategoryEntry.COLUMN_NAME_TAG_ID+
					"=t."+WordServantContract.TagEntry._ID+") where "+
					"s."+WordServantContract.ScriptureEntry._ID+"="+scriptureQuery.getInt(2),null);
			for(int i=0;i<tagTextQuery.getCount();i++){
				tagTextQuery.moveToPosition(i);
				tagText += tagTextQuery.getString(0);
				if(i<tagTextQuery.getCount()-1)
					tagText += ", ";
			}
			editCategory.setText(tagText);*/
			mEditScripture.setText(Html.fromHtml(scriptureQuery.getString(1)));
		} catch(SQLiteException e){
			System.err.println("Database issue..");
			e.printStackTrace();
		}
	}
	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		mEditScriptureReference = (TextView) getView().findViewById(R.id.referenceText);
		mEditCategory = (TextView) getView().findViewById(R.id.scriptureTags);
		mEditScripture = (TextView) getView().findViewById(R.id.scriptureText);
	}
}