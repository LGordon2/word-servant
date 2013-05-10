package com.app.wordservant.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.app.wordservant.provider.WordServantContract;

public class DeleteScriptureDialogFragment extends DialogFragment {
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		Cursor cursor = getActivity().getContentResolver().query(Uri.withAppendedPath(WordServantContract.ScriptureEntry.CONTENT_ID_URI_BASE,getArguments().getString("_id")), 
				new String []{
					WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE
				}, null, null, null);
		cursor.moveToFirst();
		builder.setMessage("Delete \""+ cursor.getString(0) +"\"?")
			.setTitle("Confirm")
			.setPositiveButton("Delete", new OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Delete scripture code here...
					getActivity().getContentResolver().delete(Uri.withAppendedPath(WordServantContract.ScriptureEntry.CONTENT_ID_URI_BASE,getArguments().getString("_id")), 
							null, null);
				}
				
			})
			.setNegativeButton("Cancel", null);
		return builder.create();
	}
}
