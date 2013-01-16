package com.app.wordservant.ui;

import java.util.ArrayList;
import java.util.Collections;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.CursorLoader;

import com.app.wordservant.provider.WordServantContract;

public class RereviewScriptureDialogFragment extends DialogFragment{

	private DialogListener mListener;
	private ArrayList<Integer> mAllIds;
	private Cursor mCursor;
	
	public interface DialogListener{
		void onPositiveButtonClicked(Integer [] ids);
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		mAllIds = new ArrayList<Integer>();
		String [] projection = {WordServantContract.ScriptureEntry._ID,
				WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE};
		String selection = WordServantContract.ScriptureEntry.COLUMN_NAME_LAST_REVIEWED_DATE+"=date('now','localtime')";
		mCursor = new CursorLoader(getActivity(), WordServantContract.ScriptureEntry.CONTENT_URI, 
				projection, selection, 
				null, null).loadInBackground();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		mListener = (DialogListener) getActivity();
		CharSequence [] items = new CharSequence[mCursor.getCount()];
		for(int i=0;i<mCursor.getCount();i++){
			mCursor.moveToPosition(i);
			items[i]=mCursor.getString(1);
		}
		builder.setMultiChoiceItems(items, null, new OnMultiChoiceClickListener(){

			

			@Override
			public void onClick(DialogInterface dialog, int which,
					boolean isChecked) {
				// TODO Auto-generated method stub
				mCursor.moveToPosition(which);
				if(isChecked)
					mAllIds.add(mCursor.getInt(0));
				else
					mAllIds.remove((Integer) mCursor.getInt(0));
			}
			
		}).setTitle("Select scriptures to rereview").setPositiveButton("Done", new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Collections.sort(mAllIds);
				mListener.onPositiveButtonClicked((Integer[]) mAllIds.toArray(new Integer[mAllIds.size()]));
			}
			
		}).setNegativeButton("Cancel", new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
			
		});
		return builder.create();
	}
}
