package com.app.wordservant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
