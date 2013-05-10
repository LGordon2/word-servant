package com.app.wordservant.ui;

import com.app.wordservant.provider.WordServantContract;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ScriptureInfoDialogFragment extends DialogFragment {
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		Cursor cursor = getActivity().getContentResolver().query(Uri.withAppendedPath(WordServantContract.ScriptureEntry.CONTENT_ID_URI_BASE,getArguments().getString("_id")), 
				new String []{
					WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE,
					WordServantContract.ScriptureEntry.COLUMN_NAME_CREATED_DATE,
					WordServantContract.ScriptureEntry.COLUMN_NAME_TIMES_REVIEWED,
					WordServantContract.ScriptureEntry.COLUMN_NAME_SCHEDULE,
					WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE
				}, null, null, null);
		cursor.moveToFirst();
		builder.setMessage("Reference: "+ cursor.getString(0) +"\n"+
				"Created: "+cursor.getString(1)+"\n"+
				"Times reviewed: "+cursor.getString(2)+"\n"+
				"Schedule: "+cursor.getString(3)+"\n"+
				"Next review date: "+cursor.getString(4)
				)
			.setTitle("Scripture Info")
			.setPositiveButton("Done", new OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Delete scripture code here...
				}
				
			});
		return builder.create();
	}
}
