package com.app.wordservant;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

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
					String [] columns = {
							WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE,
							WordServantContract.ScriptureEntry.COLUMN_NAME_SCHEDULE
					};
					Cursor cursor = getActivity().getContentResolver().query(WordServantContract.ScriptureEntry.CONTENT_URI, columns, 
							WordServantContract.ScriptureEntry.COLUMN_NAME_SCHEDULE+"='daily' AND "+
							WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE+"=date('now','localtime')", null, null);
					if(cursor.getCount()==0){
						String [] updateColumns = {
							WordServantContract.ScriptureEntry._ID,
							WordServantContract.ScriptureEntry.COLUMN_NAME_SCHEDULE
						};
						cursor = getActivity().getContentResolver().query(
								WordServantContract.ScriptureEntry.CONTENT_URI, 
								updateColumns, 
								WordServantContract.ScriptureEntry.COLUMN_NAME_SCHEDULE+"='daily'", 
								null, null);
						
						ContentValues updateValues = new ContentValues();
						Integer currentId;
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
						GregorianCalendar calendar = new GregorianCalendar();
						for(int i=0;i<cursor.getCount();i++){
							cursor.moveToPosition(i);
							calendar.add(GregorianCalendar.WEEK_OF_YEAR, i==0?0:1);
							currentId = cursor.getInt(0);
							updateValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE, dateFormat.format(calendar.getTime()));
							getActivity().getContentResolver().update(Uri.withAppendedPath(WordServantContract.ScriptureEntry.CONTENT_ID_URI_BASE,String.valueOf(currentId)), 
									updateValues, WordServantContract.ScriptureEntry.COLUMN_NAME_SCHEDULE+"='daily'", null);
						}
					}
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
