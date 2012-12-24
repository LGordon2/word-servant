package com.app.wordservant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DeleteScriptureDialogFragment extends DialogFragment {
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Delete scripture?")
			.setTitle("Confirm")
			.setPositiveButton("Delete", new OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Delete scripture code here...
					SQLiteDatabase db = new WordServantDbHelper(getActivity(), WordServantContract.DATABASE_NAME, null, WordServantDbHelper.DATABASE_VERSION).getWritableDatabase();
					String whereClause = WordServantContract.ScriptureEntry._ID+"="+getArguments().getString("_id");
					db.delete(WordServantContract.ScriptureEntry.TABLE_NAME, whereClause, null);
					String [] columns = {
							WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE,
							WordServantContract.ScriptureEntry.COLUMN_NAME_SCHEDULE
					};
					Cursor cursor = db.query(WordServantContract.ScriptureEntry.TABLE_NAME, columns, 
							WordServantContract.ScriptureEntry.COLUMN_NAME_SCHEDULE+"='daily' AND "+
							WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE+"=date('now','localtime')", null, null, null, null);
					if(cursor.getCount()==0){
						String [] updateColumns = {
							WordServantContract.ScriptureEntry._ID,
							WordServantContract.ScriptureEntry.COLUMN_NAME_SCHEDULE
						};
						cursor = db.query(
								WordServantContract.ScriptureEntry.TABLE_NAME, 
								updateColumns, 
								WordServantContract.ScriptureEntry.COLUMN_NAME_SCHEDULE+"='daily'", 
								null, null, null, null);
						
						ContentValues updateValues = new ContentValues();
						Integer currentId;
						for(int i=0;i<cursor.getCount();i++){
							cursor.moveToPosition(i);
							currentId = cursor.getInt(0);
							updateValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE, "date('now','localtime', '+"+i*7+" days')");
							db.execSQL("update "+WordServantContract.ScriptureEntry.TABLE_NAME+
									" set "+WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE+"=date('now','localtime', '+"+i*7+" days') "+
									" where "+WordServantContract.ScriptureEntry.COLUMN_NAME_SCHEDULE+"='daily' and "+
									WordServantContract.ScriptureEntry._ID+"="+currentId);
						}
					}
					getActivity().getContentResolver().notifyChange(WordServantContract.ScriptureEntry.CONTENT_URI, null);
					db.close();
				}
				
			})
			.setNegativeButton("Cancel", new OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
				
			});
		return builder.create();
	}
}
