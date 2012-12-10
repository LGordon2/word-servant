package com.app.wordservant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

public class DeleteScriptureDialogFragment extends DialogFragment {
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface DeleteScriptureDialogFragmentListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }
    
    // Use this instance of the interface to deliver action events
    DeleteScriptureDialogFragmentListener mListener;
    
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the DeleteScriptureDialogFragmentListener so we can send events to the host
            mListener = (DeleteScriptureDialogFragmentListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement DeleteScriptureDialogFragmentListener");
        }
    }
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Delete scripture?")
			.setTitle("Confirm")
			.setPositiveButton("Delete", new OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Delete scripture code here...
					SQLiteDatabase db = new WordServantDbHelper(getActivity(), "wordservant_db", null, WordServantDbHelper.DATABASE_VERSION).getWritableDatabase();
					String whereClause = WordServantContract.ScriptureEntry._ID+"="+getArguments().getString("_id");
					db.delete(WordServantContract.ScriptureEntry.TABLE_NAME, whereClause, null);
					db.close();
					mListener.onDialogPositiveClick(DeleteScriptureDialogFragment.this);
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
